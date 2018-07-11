package com.jsaw.ibreast.link;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import com.jsaw.ibreast.R;

public class link extends AppCompatActivity {
    ImageButton center,economic,foundation,resource;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_link);
        center = findViewById(R.id.center);
        economic = findViewById(R.id.economic);
        foundation = findViewById(R.id.foundation);
        resource = findViewById(R.id.resource);

        center.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), link_center.class));
            }
        });
        economic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), link_economic.class));
            }
        });
        foundation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), link_foundation.class));
            }
        });
        resource.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), link_resource.class));
            }
        });
    }
}
