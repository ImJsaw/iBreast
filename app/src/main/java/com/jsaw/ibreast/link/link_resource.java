package com.jsaw.ibreast.link;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
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
import java.util.Objects;

public class link_resource extends AppCompatActivity {
    class Center{
        String name;
        String phone;
        String address;
        String url;
        String info;
    }
    public static final ArrayList<Center> wig=new ArrayList<>();
    public static final ArrayList<Center> bra=new ArrayList<>();
    public static final ArrayList<Center> cuff=new ArrayList<>();
    public static final ArrayList<Center> home=new ArrayList<>();
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_link_resource);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
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
                    Toast.makeText(link_resource.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        getData();
        Log.d("link","resource getData");
    }

    private void addTableRow(final Context context, TableLayout tl, final String name, final String phone, final String address, final String url, final String info){
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
        tr.setClickable(true);

        tr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final SpannableString s;
                if(url!=null && info!=null)
                    s = new SpannableString("電話：" + phone + "\n地址：" + address + "\n\n" + info + "\n\n" + url);
                else if(url!=null)
                    s = new SpannableString("電話：" + phone + "\n地址：" + address + "\n\n" + url);
                else if(info!=null)
                    s = new SpannableString("電話：" + phone + "\n地址：" + address + "\n\n" + info);
                else
                    s = new SpannableString("電話：" + phone + "\n地址：" + address);
                    Linkify.addLinks(s, Linkify.WEB_URLS);
                    final AlertDialog d = new AlertDialog.Builder(context)
                            .setPositiveButton(android.R.string.ok, null)
                            .setMessage( s )
                            .setTitle(name)
                            .create();
                    d.show();

                    // Make the textview clickable. Must be called after show()
                    ((TextView)d.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            }
        });
//your code...


        tl.addView(tr);
    }

    private void dataProcess(DataSnapshot data, ArrayList<Center> target){
        for (int i = 0; i < data.getChildrenCount(); i++) {
            Center temp = new Center();
            temp.name = String.valueOf(data.child(String.valueOf(i)).child("unit").getValue());
            temp.phone = String.valueOf(data.child(String.valueOf(i)).child("phone").getValue());
            temp.address = String.valueOf(data.child(String.valueOf(i)).child("address").getValue());
            temp.url = String.valueOf(data.child(String.valueOf(i)).child("url").getValue());
            temp.info = String.valueOf(data.child(String.valueOf(i)).child("intro").getValue());
            target.add(temp);
        }
    }

    private void getData(){
        FirebaseDatabase.getInstance().getReference("link/resource").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                Log.d("link",ds.child(String.valueOf(0)).child(String.valueOf(0)).toString());
                wig.clear();
                bra.clear();
                cuff.clear();
                home.clear();
                for(int k=0;k<ds.getChildrenCount();k++) {
                    DataSnapshot areaData = ds.child(String.valueOf(k));
                   // Log.i("link_res", String.valueOf(ds.child(String.valueOf(k)))+ k);
                    if (k == 0) dataProcess(areaData,wig);
                    else if(k==1) dataProcess(areaData,bra);
                    else if(k==2) dataProcess(areaData,cuff);
                    else if(k==3) dataProcess(areaData,home);
                }

                TableLayout ll =  findViewById(R.id.WigTable);
                TableLayout cl =  findViewById(R.id.BraTable);
                TableLayout sl =  findViewById(R.id.CuffTable);
                TableLayout hl =  findViewById(R.id.HomeTable);
                for (int i = 0; i < wig.size(); i++)
                    addTableRow(link_resource.this,ll,wig.get(i).name,wig.get(i).phone,wig.get(i).address,wig.get(i).url,wig.get(i).info);
                for (int i = 0; i < bra.size(); i++)
                    addTableRow(link_resource.this,cl,bra.get(i).name,bra.get(i).phone,bra.get(i).address,bra.get(i).url,bra.get(i).info);
                for (int i = 0; i <  cuff.size(); i++)
                    addTableRow(link_resource.this,sl,cuff.get(i).name,cuff.get(i).phone,cuff.get(i).address,cuff.get(i).url,cuff.get(i).info);
                for (int i = 0; i < home.size(); i++)
                    addTableRow(link_resource.this,hl,home.get(i).name,home.get(i).phone,home.get(i).address,home.get(i).url,home.get(i).info);
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
    }

    //action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;

            default:
                return true;
        }
    }
}