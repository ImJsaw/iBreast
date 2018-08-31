package com.jsaw.ibreast.note;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class note_my extends AppCompatActivity {
    private EditText edtBirth;
    private EditText edtDate;
    private EditText edtHeight;
    private static final int[] IDS = new int[]{R.id.checkbox_listview, R.id.chitxt_listview};
    private static final String[] STRINGS = new String[]{"checkbox", "chiName"};
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.note_my);


    }

    // 設定小日曆選擇時間
    public void onImgClick(final View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog;

        switch (view.getId()) {
            case R.id.imgBirth:
                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtBirth.setText(year - 1911 + "年" + (month + 1) + "月");
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.imgCal:
                datePickerDialog = new DatePickerDialog(this,
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtDate.setText(year - 1911 + "年" + (month + 1) + "月");
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
        }
    }

    public void myClick(View v) {
        switch (v.getId()) {
            case R.id.Btn_my:
                setContentView(R.layout.note_my);
                break;
            case R.id.Btn_edit:
                setContentView(R.layout.note_my_add);
                onEdit();

                break;
        }
    }

    public void onEdit() {
        Button btnSave = findViewById(R.id.Btn_save);
        edtBirth = findViewById(R.id.edtBirth);
        edtDate = findViewById(R.id.edtDate);
        edtHeight = findViewById(R.id.edtHeight);
        setDialog();
        getMethodData();
        getProgramData();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //判斷是否有空的欄位

            }
        });
    }

    private void getMethodData() {
        FirebaseDatabase.getInstance().getReference("my").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> method_items = new ArrayList<>();
                ListView listMethod = findViewById(R.id.list_method);

//                CheckboxListviewAdapter checkboxListviewAdapter = new CheckboxListviewAdapter(note_my.this);
//                listMethod.setAdapter(checkboxListviewAdapter);

                dataSnapshot = dataSnapshot.child("method");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("chiName", data.getValue().toString());
                    method_items.add(item);
                }

//                checkboxListviewAdapter.setItems(method_items);

                SimpleAdapter sa = new SimpleAdapter(note_my.this, method_items, R.layout.note_list_view,
                        STRINGS, IDS);
                listMethod.setAdapter(sa);

                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void getProgramData() {
        FirebaseDatabase.getInstance().getReference("my").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> program_items = new ArrayList<>();

                ListView listProgram = findViewById(R.id.list_program);

                dataSnapshot = dataSnapshot.child("program");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("chiName", data.getValue().toString());
                    program_items.add(item);

                    progressDialog.dismiss();
                    isProgressDialogShow = false;
                }

                SimpleAdapter sa = new SimpleAdapter(note_my.this, program_items, R.layout.note_list_view,
                        STRINGS, IDS);
                listProgram.setAdapter(sa);

//                progressDialog.dismiss();
//                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setDialog() {
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
    }

    private boolean checkBlank() {
        if (edtBirth.getText().toString().trim().equals("")) {
            Toast.makeText(note_my.this, "生日欄位不可為空白", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtDate.getText().toString().trim().equals("")) {
            Toast.makeText(note_my.this, "手術年月欄位不可為空白", Toast.LENGTH_SHORT).show();
            return false;
        } else if (edtHeight.getText().toString().trim().equals("")) {
            Toast.makeText(note_my.this, "身高欄位不可為空白", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
