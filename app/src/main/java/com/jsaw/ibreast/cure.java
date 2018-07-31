package com.jsaw.ibreast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class cure extends AppCompatActivity {
    private String url[] = new String[50];
    private List<listAdapter.listData> mData = null;
    private listAdapter mAdapter = null;
    private ListView listView;
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        listView = findViewById(R.id.list_cure);
        //waiting dialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isProgressDialogShow) {
                    progressDialog.dismiss();
                    Toast.makeText(cure.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        getData();
    }

    private void getData() {
        mData = new LinkedList<>();
        FirebaseDatabase.getInstance().getReference("cure").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    listAdapter.listData c = new listAdapter.listData();
                    c.title = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.url = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("url").getValue()).toString();
                    url[i] = c.url;
                    mData.add(c);
                }
                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //listView
        mAdapter = new listAdapter((LinkedList<listAdapter.listData>) mData, cure.this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("URL:", url[position]);
                startActivity(new Intent().setClass(cure.this, open_pdf.class).putExtra("URL", url[position]));
            }
        });
    }

    //action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, Objects.requireNonNull(upIntent))) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return true;
        }
    }
}
