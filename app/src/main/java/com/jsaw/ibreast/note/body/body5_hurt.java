package com.jsaw.ibreast.note.body;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.jsaw.ibreast.R;

public class body5_hurt extends AppCompatActivity {
    private TextView txtScore;
    private SeekBar seekBar;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_body5_hurt);

        txtScore = findViewById(R.id.txtScore);
        seekBar = findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtScore.setText(progress + "分");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btnFace1:
                txtScore.setText("0分");
                seekBar.setProgress(0);
                break;
            case R.id.btnFace2:
                txtScore.setText("2分");
                seekBar.setProgress(2);
                break;
            case R.id.btnFace3:
                txtScore.setText("4分");
                seekBar.setProgress(4);
                break;
            case R.id.btnFace4:
                txtScore.setText("6分");
                seekBar.setProgress(6);
                break;
            case R.id.btnFace5:
                txtScore.setText("8分");
                seekBar.setProgress(8);
                break;
            case R.id.btnFace6:
                txtScore.setText("10分");
                seekBar.setProgress(10);
                break;
            case R.id.btn_cancel:
                body5_hurt.super.onBackPressed();
                break;
            case R.id.btnSave:
                Intent intent = new Intent(body5_hurt.this, note_body5.class);
                intent.putExtra("score", txtScore.getText());
                setResult(Activity.RESULT_OK, intent);
                finish();
                break;
        }
    }


}
