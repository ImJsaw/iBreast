package com.jsaw.ibreast;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.jsaw.ibreast.laugh.laugh;
import com.jsaw.ibreast.link.link;
import com.jsaw.ibreast.talk.talk;

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
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(false);

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
                startActivity(new Intent().setClass(main.this, cure.class));
            }
        });

        Btn_eat= findViewById(R.id.Btn_eat);
        Btn_eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(main.this, eat.class));
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
                startActivity(new Intent().setClass(main.this, link.class));
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
                startActivity(new Intent().setClass(main.this, move.class));
            }
        });

        Btn_talk= findViewById(R.id.Btn_talk);
        Btn_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(main.this, talk.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1,1,1,"關於");
        menu.add(1,2,2,"個人頁面");
        menu.add(1,3,3,"登出");
        menu.add(1,4,4,"退出APP");
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case 1:
                //open about page
                break;
            case 2:
                //open personal page
                break;
            case 3:
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent().setClass(main.this, login.class));
                finish();
                break;
            case 4:
                System.exit(0);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //雙擊退出
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            System.exit(0);
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
