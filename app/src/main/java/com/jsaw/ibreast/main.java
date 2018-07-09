package com.jsaw.ibreast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import com.jsaw.ibreast.laugh.laugh;

import java.util.Objects;

public class main extends AppCompatActivity {
    ImageButton Btn_ask;
    ImageButton Btn_link;
    ImageButton Btn_note;
    ImageButton Btn_move;
    ImageButton Btn_talk;
    ImageButton Btn_eat;
    ImageButton Btn_laugh;
    ImageButton Btn_cure;

    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Btn_laugh= findViewById(R.id.Btn_laugh);
        Btn_laugh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(main.this, laugh.class));
            }
        });

        Btn_cure= findViewById(R.id.Btn_cure);
        Btn_cure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent().setClass(main.this, main.class));
            }
        });

        Btn_eat= findViewById(R.id.Btn_eat);
        Btn_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent().setClass(main.this, main.class));
            }
        });

        Btn_ask= findViewById(R.id.Btn_ask);
        Btn_ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent().setClass(main.this, main.class));
            }
        });

        Btn_link= findViewById(R.id.Btn_link);
        Btn_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent().setClass(main.this, main.class));
            }
        });

        Btn_note= findViewById(R.id.Btn_note);
        Btn_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent().setClass(main.this, main.class));
            }
        });

        Btn_move= findViewById(R.id.Btn_move);
        Btn_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent().setClass(main.this, main.class));
            }
        });

        Btn_talk= findViewById(R.id.Btn_talk);
        Btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent().setClass(main.this, main.class));
            }
        });
    }

    //雙擊退出
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "再按一次返回退出APP", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed( new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

}
