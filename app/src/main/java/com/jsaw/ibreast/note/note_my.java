package com.jsaw.ibreast.note;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class note_my extends AppCompatActivity {
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_my);
        
        progressDialog = new ProgressDialog(note_my.this);
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isProgressDialogShow) {
                    progressDialog.dismiss();
                    Toast.makeText(note_my.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        
        readData();

        Button btn_edit = findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(mBtnEdit);
    }

    // 編輯按鈕
    View.OnClickListener mBtnEdit = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent().setClass(note_my.this, note_my_add.class));
        }
    };

    //讀取資料
    private void readData() {
        final Map<String, TextView> myTextView = new HashMap<>();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                myTextView.put("birth", (TextView) findViewById(R.id.txtInputBirth));
                myTextView.put("height", (TextView) findViewById(R.id.txtInputHeight));
                myTextView.put("stopBleed", (TextView) findViewById(R.id.txtInputStopBleeding));
                myTextView.put("surgeryDate", (TextView) findViewById(R.id.txtSurgeryDate));
                myTextView.put("surgeryMethod", (TextView) findViewById(R.id.txtSurgeryMethodName));
                myTextView.put("cell", (TextView) findViewById(R.id.txtCellType));
                myTextView.put("er", (TextView) findViewById(R.id.txtInputEr));
                myTextView.put("pr", (TextView) findViewById(R.id.txtInputPr));
                myTextView.put("her", (TextView) findViewById(R.id.txtHerType));
                myTextView.put("fish", (TextView) findViewById(R.id.txtFishType));
                myTextView.put("program", (TextView) findViewById(R.id.txtProgramName));
                dataSnapshot = dataSnapshot.child("Users");
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                dataSnapshot = dataSnapshot.child(user);
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    String dataName = data.getKey();
                    if (myTextView.containsKey(dataName)) {
                        if (dataName.equals("surgeryMethod") || dataName.equals("program")) {
                            StringBuilder str = new StringBuilder();
                            for (DataSnapshot ds : data.child("record").getChildren()) {
                                str.append(ds.getValue()).append("\n");
                            }
                            // 刪除最後一個\n
                            str = str.deleteCharAt(str.length()-1);
                            myTextView.get(dataName).setText(str.toString());
                        } else {
                            String dataValue = Objects.requireNonNull(data.getValue()).toString();
                            myTextView.get(dataName).setText(dataValue);
                        }

                    }
                }
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
