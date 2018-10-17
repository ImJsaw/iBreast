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

public class ask extends AppCompatActivity {
    private LinkedList<askAdapter.askData> mData = null;
    private askAdapter mAdapter = null;
    private ListView listView;
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        listView = findViewById(R.id.list_ask);

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
                    Toast.makeText(ask.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);

        getData();
    }

    private void getData() {
        mData = new LinkedList<>();
        FirebaseDatabase.getInstance().getReference("ask").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("text").getValue()).toString();
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
        mAdapter = new askAdapter( mData, ask.this);
        listView.setAdapter(mAdapter);
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
