package com.jsaw.ibreast.note.mood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class mood extends AppCompatActivity {
    private static final int[] IDS = new int[]{R.id.txtDate, R.id.txtScore, R.id.txtWords};
    private static final String[] STRINGS = new String[]{"date", "score", "words"};
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private List<String> words = new LinkedList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood);
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
                    Toast.makeText(mood.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);

        firebaseGetData();

        ImageButton btnCreate = findViewById(R.id.btnCreate);
        btnCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(mood.this, mood_add.class));
            }
        });
    }

    private void firebaseGetData() {
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> items = new ArrayList<>();
                ListView listView = findViewById(R.id.list_mood);
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                dataSnapshot = dataSnapshot.child(user).child("心情");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("date", data.child("date").getValue().toString());
                    item.put("score", data.child("score").getValue().toString());
                    words.add(data.child("words").getValue().toString());
                    items.add(item);
                }

//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                SimpleAdapter sa = new SimpleAdapter(mood.this, items, R.layout.mood_listview_item,
                        STRINGS, IDS);
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