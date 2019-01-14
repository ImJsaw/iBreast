package com.jsaw.ibreast.note;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import com.jsaw.ibreast.R;
import com.jsaw.ibreast.note.activity.note_activity;
import com.jsaw.ibreast.note.activity.note_activity_add;
import com.jsaw.ibreast.note.body.note_body;
import com.jsaw.ibreast.note.food.note_food;
import com.jsaw.ibreast.note.mood.mood;
import com.jsaw.ibreast.note.move.note_move;
import com.jsaw.ibreast.note.treat.note_treat_main;
import com.jsaw.ibreast.note.treat.treat1;

import java.util.Objects;

public class note extends AppCompatActivity {
    Button mBtnMy;
    Button mBtnTreat;
    Button mBtnBody;
    Button mBtnMood;
    Button mBtnFood;
    Button mBtnMove;
    Button mBtnActivity;
    Button mBtnBack;

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
                startActivity(new Intent().setClass(note.this, treat1.class));
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

        mBtnFood = findViewById(R.id.Btn_food);
        mBtnFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_food.class));
            }
        });

        mBtnMove = findViewById(R.id.Btn_sport);
        mBtnMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_move.class));
            }
        });


        mBtnActivity = findViewById(R.id.Btn_activity);
        mBtnActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_activity.class));
            }
        });

        mBtnBack = findViewById(R.id.Btn_back);
        mBtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent().setClass(note.this, note_back.class));
            }
        });
    }
}
