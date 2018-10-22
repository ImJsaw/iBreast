package com.jsaw.ibreast.note.mood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.HashMap;
import java.util.List;

public class mood_edit extends AppCompatActivity {
    private String position;
    private EditText edtWords;
    private HashMap<String, Object> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_edit);

        ImageButton btnSave = findViewById(R.id.btnSave);
        edtWords = findViewById(R.id.edtWords);
        btnSave.setOnClickListener(mBtnSave);

        Intent intent = getIntent();
//        String words = intent.getStringExtra("words");
//        edtWords.setText(words);
        position = String.valueOf(intent.getIntExtra("position", 0));

        items = (HashMap<String, Object>) intent.getSerializableExtra("data");
        edtWords.setText(items.get("words").toString());

    }

    View.OnClickListener mBtnSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            updateData(position, edtWords.getText().toString());
            items.put("words", edtWords.getText().toString());
            updateData();

        }
    };

    // 儲存資料
    private void updateData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser users = auth.getCurrentUser();
        String user = users.getUid();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance()
                .getReference("Users")
                .child(user)
                .child("心情")
                .child(position);
        mDatabase.setValue(items);
        startActivity(new Intent().setClass(mood_edit.this, mood.class));
    }
}
