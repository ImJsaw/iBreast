package com.jsaw.ibreast.note.treat;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
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
import java.util.List;

public class treat1 extends AppCompatActivity {
    private static final int[] IDS = new int[]{R.id.txtDate, R.id.txtResult};
    private static final String[] STRINGS = new String[]{"date", "part"};
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser users = auth.getCurrentUser();
    private String user = users.getUid();
    private int position;
    private ListView listView;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_treat1);
        listView = findViewById(R.id.list_treat1);
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
                    Toast.makeText(treat1.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                setPosition(position);
                final AlertDialog d = new AlertDialog.Builder(treat1.this)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //deleteRecord
                                deleteData();

                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage("確定刪除此筆資料嗎？")
                        .setTitle("刪除")
                        .create();
                d.show();

                return false;
            }
        });

        setButtons();
        firebaseGetData();
    }


    private void firebaseGetData() {
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> items = new ArrayList<>();

                dataSnapshot = dataSnapshot.child(user).child("化療");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("date", data.child("date").getValue().toString());
                    StringBuilder str = new StringBuilder();
                    for (DataSnapshot ds : data.child("part").getChildren()) {
                        str.append(ds.getValue()).append("\n");
                    }
                    // 刪除最後一個\n
                    str = str.deleteCharAt(str.length() - 1);
                    item.put("part", str);
//                    }
                    items.add(item);
                }

//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                SimpleAdapter sa = new SimpleAdapter(treat1.this, items, R.layout.treat1_listview_item,
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

    //長按刪除資料
    private void deleteData() {
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int position = getPosition();
                dataSnapshot = dataSnapshot.child(user).child("化療");
                int count = 0;
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (count == position) {
                        data.getRef().removeValue();
                        System.out.println(data);
                        break;
                    }
                    count++;
                }
                Toast.makeText(treat1.this, "刪除成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent().setClass(treat1.this, treat1.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setButtons() {
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        Button btn_check = findViewById(R.id.btn_edit);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1.this, treat1.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1.this, treat2.class));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1.this, treat3.class));
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1.this, treat4.class));
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1.this, treat5.class));
            }
        });
        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1.this, treat1_add.class));
            }
        });
    }
}
