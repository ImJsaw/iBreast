package com.jsaw.ibreast.note.mood;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.jsaw.ibreast.R;

public class mood_edit extends AppCompatActivity{
    String position;
    private EditText edtWords;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_edit);

        ImageButton btnSave = findViewById(R.id.btnSave);
        edtWords = findViewById(R.id.edtWords);
        btnSave.setOnClickListener(mBtnSave);

        Intent intent = getIntent();
        String words = intent.getStringExtra("words");
        edtWords.setText(words);
        position = intent.getStringExtra("position");

    }

    View.OnClickListener mBtnSave = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };
}
