package com.jsaw.ibreast.link;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;
import java.util.ArrayList;

public class link_center extends AppCompatActivity {
    class Center{
        String name;
        String phone;
        String address;
    }
    public static final ArrayList<Center> north=new ArrayList<>();
    public static final ArrayList<Center> center=new ArrayList<>();
    public static final ArrayList<Center> south=new ArrayList<>();
    public static final ArrayList<Center> east=new ArrayList<>();
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_link_center);
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isProgressDialogShow){
                    progressDialog.dismiss();
                    Toast.makeText(link_center.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        getData();
        Log.d("link","center getData");
    }

    private void addTableRow(TableLayout tl, String name, String phone, String address){

        LayoutInflater inflater = this.getLayoutInflater();
        TableRow tr = (TableRow)inflater.inflate(R.layout.table_row, tl, false);

        // Add First Column
        TextView Name = (TextView)tr.findViewById(R.id.Name);
        Name.setText(name);

        // Add the 3rd Column
        TextView Phone = (TextView)tr.findViewById(R.id.Phone);
        Phone.setText(phone);

        TextView Address = (TextView)tr.findViewById(R.id.Address);
        Address.setText(address);

        tl.addView(tr);
    }

    private void dataProcess(DataSnapshot data, ArrayList<Center> target){
        for (int i = 0; i < data.getChildrenCount(); i++) {
            Center temp = new Center();
            temp.name = String.valueOf(data.child(String.valueOf(i)).child("unit").getValue());
            temp.phone = String.valueOf(data.child(String.valueOf(i)).child("phone").getValue());
            temp.address = String.valueOf(data.child(String.valueOf(i)).child("address").getValue());
            target.add(temp);
        }
    }

    private void getData(){
        FirebaseDatabase.getInstance().getReference("link/center").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                Log.d("link",ds.child(String.valueOf(0)).child(String.valueOf(0)).toString());
                north.clear();
                center.clear();
                south.clear();
                east.clear();
                for(int k=0;k<ds.getChildrenCount();k++) {
                    DataSnapshot areaData = ds.child(String.valueOf(k));
                    // Log.i("link_center", String.valueOf(ds.child(String.valueOf(k)))+ k);
                    if (k == 0) dataProcess(areaData,north);
                    else if(k==1) dataProcess(areaData,center);
                    else if(k==2) dataProcess(areaData,south);
                    else if(k==3) dataProcess(areaData,east);
                }
                TableLayout ll =  findViewById(R.id.NorthTable);
                TableLayout cl =  findViewById(R.id.CenterTable);
                TableLayout sl =  findViewById(R.id.SouthTable);
                TableLayout el =  findViewById(R.id.EastTable);
                for (int i = 0; i <north.size(); i++) addTableRow(ll,north.get(i).name,north.get(i).phone,north.get(i).address);
                for (int i = 0; i <center.size(); i++) addTableRow(cl,center.get(i).name,center.get(i).phone,center.get(i).address);
                for (int i = 0; i < south.size(); i++) addTableRow(sl,south.get(i).name,south.get(i).phone,south.get(i).address);
                for (int i = 0; i <east.size(); i++) addTableRow(el,east.get(i).name,east.get(i).phone,east.get(i).address);
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

}


