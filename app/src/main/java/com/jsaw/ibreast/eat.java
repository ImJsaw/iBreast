package com.jsaw.ibreast;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class eat extends AppCompatActivity {
    private String url[] = new String[50];
    private List<cureData> mData = null;
    private listAdapter mAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        ListView listView =  findViewById(R.id.list_cure);

        mData = new LinkedList<>();
        FirebaseDatabase.getInstance().getReference("eat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(int i = 0; i < dataSnapshot.getChildrenCount();i++){
                    cureData c = new cureData();
                    c.title = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.url = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("url").getValue()).toString();
                    url[i] = c.url;
                    mData.add(c);
                    mAdapter.notifyDataSetChanged();
                }
                mAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mAdapter = new listAdapter((LinkedList<cureData>) mData, eat.this);
        listView.setAdapter(mAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d("URL:",url[position]);
                startActivity(new Intent().setClass(eat.this,open_pdf.class).putExtra("URL",url[position]));
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
