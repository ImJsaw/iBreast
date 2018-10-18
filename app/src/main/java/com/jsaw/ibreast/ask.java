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
import android.widget.LinearLayout;
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
    private LinkedList<askAdapter.askData> eatData, diagnosisData;
    private askAdapter eatAdapter, diagnosisAdapter;
    private ListView listView_eat,listView_diagnosis;
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;
    private Boolean isEatShow,isDiagnosisShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        listView_eat = findViewById(R.id.list_ask);
        listView_diagnosis = findViewById(R.id.list_diagnosis);

        isEatShow = isDiagnosisShow = true;

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
        LinearLayout eatShowArea = findViewById(R.id.eat_show_area);
        LinearLayout diagnosisShowArea = findViewById(R.id.diagnosis_show_area);

        eatShowArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEatShow) listView_eat.setVisibility(View.GONE);
                else listView_eat.setVisibility(View.VISIBLE);
                isEatShow = !isEatShow;
            }
        });

        diagnosisShowArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDiagnosisShow) listView_diagnosis.setVisibility(View.GONE);
                else listView_diagnosis.setVisibility(View.VISIBLE);
                isDiagnosisShow = !isDiagnosisShow;
            }
        });

    }

    private void getData() {
        eatData = new LinkedList<>();
        diagnosisData = new LinkedList<>();
        FirebaseDatabase.getInstance().getReference("ask").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot eat = dataSnapshot.child("eat");
                DataSnapshot diagnosis = dataSnapshot.child("diagnose");
                for (int i = 0; i < eat.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(eat.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(eat.child(String.valueOf(i)).child("text").getValue()).toString();
                    eatData.add(c);
                }
                for (int i = 0; i < diagnosis.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(diagnosis.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(diagnosis.child(String.valueOf(i)).child("text").getValue()).toString();
                    diagnosisData.add(c);
                }
                diagnosisAdapter.notifyDataSetChanged();
                eatAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //listView
        eatAdapter = new askAdapter( eatData, ask.this);
        diagnosisAdapter = new askAdapter( diagnosisData, ask.this);
        listView_eat.setAdapter(eatAdapter);
        listView_diagnosis.setAdapter(diagnosisAdapter);
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
