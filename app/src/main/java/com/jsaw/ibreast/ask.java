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
    final int askCount = 3;
    private String[] title;
    private String[] ans ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        title = getResources().getStringArray(R.array.ask_title);
        ans = getResources().getStringArray(R.array.ask_ans);
        setContentView(R.layout.activity_ask);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        listView = findViewById(R.id.list_ask);

        getData();
    }

    private void getData() {
        mData = new LinkedList<>();
        for (int i = 0;i < askCount;i++){
            askAdapter.askData a = new askAdapter.askData();
            a.title = title[i];
            a.text = ans[i];
            mData.add(a);
        }
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
