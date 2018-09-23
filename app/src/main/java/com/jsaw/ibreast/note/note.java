package com.jsaw.ibreast.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.jsaw.ibreast.R;
import com.jsaw.ibreast.note.body.note_body;
import com.jsaw.ibreast.note.mood.mood;
import com.jsaw.ibreast.note.mood.mood_add;
import com.jsaw.ibreast.note.treat.note_treat_main;

import java.util.Objects;

public class note extends AppCompatActivity {
    Button mBtnMy;
    Button mBtnTreat;
    Button mBtnBody;
    Button mBtnMood;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        mBtnMy = findViewById(R.id.Btn_my);
        mBtnMy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_my.class));
            }
        });

        mBtnTreat = findViewById(R.id.Btn_treat);
        mBtnTreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_treat_main.class));
            }
        });

        mBtnBody = findViewById(R.id.Btn_body);
        mBtnBody.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_body.class));
            }
        });

        mBtnMood = findViewById(R.id.Btn_mood);
        mBtnMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, mood.class));
            }
        });
    }
}
