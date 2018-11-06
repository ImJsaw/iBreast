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
import android.widget.LinearLayout;
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

public class link_economic extends AppCompatActivity {
    class PublicCenter{
        String name;
        String eligibility;
        String url;
        String unit;
        String info;
    }
    class PrivateCenter{
        String name;
        String phone;
        String address;
        String url;
        String info;
    }

    public static final ArrayList<PublicCenter> publicCenters=new ArrayList<>();
    public static final ArrayList<PrivateCenter> privateCenters=new ArrayList<>();
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;
    TableLayout publicTable,privateTable;
    boolean publicShow,privateShow;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_link_economic);
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
                    Toast.makeText(link_economic.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        getData();
        Log.d("link","eco getData");

        LinearLayout publicShowArea = findViewById(R.id.public_showArea);
        LinearLayout privateShowArea = findViewById(R.id.private_showArea);
        publicShow = privateShow = false;

        publicShowArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(publicShow) publicTable.setVisibility(View.GONE);
                else publicTable.setVisibility(View.VISIBLE);
                publicShow = !publicShow;
            }
        });
        privateShowArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(privateShow) privateTable.setVisibility(View.GONE);
                else privateTable.setVisibility(View.VISIBLE);
                privateShow = !privateShow;
            }
        });
    }

    private void addPublicTableRow(final Context context, TableLayout tl, final String name, final String phone, final String address, final String url, final String info){
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
        tl.addView(tr);
    }

    private void addPrivateTableRow(final Context context, TableLayout tl, final String name, final String phone, final String address, final String url, final String info){
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
        tl.addView(tr);
    }

    private void getData(){
        FirebaseDatabase.getInstance().getReference("link/economic").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                publicCenters.clear();
                privateCenters.clear();
                Log.d("link",ds.toString());
                for(int k=0;k<ds.getChildrenCount();k++) {
                    if (k == 0) {
                        for (int i = 0; i < ds.child(String.valueOf(k)).getChildrenCount(); i++) {
                            DataSnapshot data = ds.child(String.valueOf(k)).child(String.valueOf(i));
                            PublicCenter temp = new PublicCenter();
                            temp.name = String.valueOf(data.child("item").getValue());
                            temp.eligibility = String.valueOf(data.child("eligibility").getValue());
                            temp.unit = String.valueOf(data.child("unit").getValue());
                            temp.url = String.valueOf(data.child("url").getValue());
                            temp.info = String.valueOf(data.child("information").getValue());
                            publicCenters.add(temp);
                        }
                    }
                    else if(k==1){
                        for (int i = 0; i < ds.child(String.valueOf(k)).getChildrenCount(); i++) {
                            DataSnapshot data = ds.child(String.valueOf(k)).child(String.valueOf(i));
                            PrivateCenter temp = new PrivateCenter();
                            temp.name = String.valueOf(data.child("unit").getValue());
                            temp.phone = String.valueOf(data.child("phone").getValue());
                            temp.address = String.valueOf(data.child("address").getValue());
                            temp.url = String.valueOf(data.child("url").getValue());
                            temp.info = String.valueOf(data.child("introduction").getValue());
                            privateCenters.add(temp);
                        }
                    }
                }

                publicTable =  findViewById(R.id.public_hide_table);
                privateTable =  findViewById(R.id.private_hide_table);
                for (int i = 0; i <publicCenters.size(); i++)
                    addPublicTableRow(link_economic.this,publicTable,publicCenters.get(i).name,publicCenters.get(i).eligibility,publicCenters.get(i).unit,
                            publicCenters.get(i).url,publicCenters.get(i).info);
                for (int i = 0; i <privateCenters.size(); i++)
                    addPrivateTableRow(link_economic.this, privateTable,privateCenters.get(i).name,privateCenters.get(i).phone,privateCenters.get(i).address,
                            privateCenters.get(i).url,privateCenters.get(i).info);
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