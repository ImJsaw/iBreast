package com.jsaw.ibreast.note.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;
import com.jsaw.ibreast.note.mood.mood;
import com.jsaw.ibreast.note.mood.mood_add;
import com.jsaw.ibreast.note.mood.mood_edit;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class note_activity extends AppCompatActivity {
    private static final int[] IDS = new int[]{R.id.txtDate, R.id.txtTime, R.id.txtEvent};
    private static final String[] STRINGS = new String[]{"date", "time", "event"};
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private List<HashMap<String, Object>> items;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act);
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
                    Toast.makeText(note_activity.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);

        firebaseGetData();

        ImageButton btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(note_activity.this, note_activity_add.class));
            }
        });

    }

    private void firebaseGetData() {
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items = new ArrayList<>();
                ListView listView = findViewById(R.id.list_activity);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                dataSnapshot = dataSnapshot.child(user).child("活動");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    DataSnapshot mRecord = data.child("record");
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("date", mRecord.child("startDate").getValue().toString());
                    item.put("time", mRecord.child("startTime").getValue().toString());
                    item.put("event", mRecord.child("event").getValue().toString());
                    items.add(item);
                }

//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                SimpleAdapter sa = new SimpleAdapter(note_activity.this, items,
                        R.layout.act_listview_item, STRINGS, IDS);
                listView.setAdapter(sa);
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
