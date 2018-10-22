package com.jsaw.ibreast.note.body;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;


public class body5_add extends Fragment implements View.OnClickListener {
    private EditText edtDate;
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private HashMap<String, String> data = new HashMap<>();
    private EditText chosenView;
    private EditText edtOne;
    private EditText edtTwo;
    private EditText edtThree;
    private EditText edtFour;
    private EditText edtFive;
    private EditText edtSix;
    private EditText edtSeven;
    private EditText edtEight;
    private EditText edtNine;
    private EditText edtHurt;

    private static class Record {
        public String date;
        public String time;
        public HashMap<String, String> record;

        Record(String date, String time, HashMap<String, String> record) {
            this.date = date;
            this.time = time;
            this.record = record;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("處理中,請稍候...");
        progressDialog.show();
        isProgressDialogShow = true;
        //connect time out
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (isProgressDialogShow) {
                    progressDialog.dismiss();
                    Toast.makeText(getContext(), "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_note_body5_add, container, false);
        setViews(view);

        //自動帶日期
        Calendar calendar = Calendar.getInstance();
        String time = String.valueOf(calendar.get(Calendar.YEAR)) + "/" + String.valueOf(calendar.get(Calendar.MONTH) + "/" + String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
        edtDate.setText(time);

        // 設定小日曆選擇時間
        ImageButton selectDate = view.findViewById(R.id.imgCal);
        selectDate.setOnClickListener(imgCalOnClick);

        isProgressDialogShow = false;
        progressDialog.dismiss();

        return view;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String level = data.getStringExtra("level");
                chosenView.setText(level);
            }
        }
    }

    // 確認儲存按鈕
    private View.OnClickListener mBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            saveData(edtDate.getText().toString(), edtTime.getText().toString(), data);
            isProgressDialogShow = false;
            progressDialog.dismiss();
        }
    };

    //日曆按鈕
    private View.OnClickListener imgCalOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            edtDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                        }
                    }, year, month, day);
//                顯示從目前時間開始選
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    };

    // 疼痛分數按鈕
    private View.OnClickListener mBtnHurt = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getContext(), body5_hurt.class);
            startActivity(intent);
        }
    };

    // 儲存資料
    private void saveData(String date, String time, HashMap<String, String> data) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(date, time, data);
        if (!date.isEmpty() && !time.isEmpty() && data.size() > 0) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser users = auth.getCurrentUser();
                    String user = users.getUid();
                    String count = String.valueOf(dataSnapshot.child(user).child("手臂").getChildrenCount() + 1);
                    mDatabase.child(user).child("手臂").child(count).setValue(record);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
            isProgressDialogShow = false;
            progressDialog.dismiss();
            Toast.makeText(getContext(), "儲存成功", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }

    // 症狀分級選擇
    public void onClick(View v) {
        Intent intent = new Intent(getContext(), body5_record.class);
        switch (v.getId()) {
            case R.id.edtOne:
                intent.putExtra("sign", "噁心");
                chosenView = edtOne;
                break;
            case R.id.edtTwo:
                intent.putExtra("sign", "嘔吐");
                chosenView = edtTwo;
                break;
            case R.id.edtThree:
                intent.putExtra("sign", "腹瀉");
                chosenView = edtThree;
                break;
            case R.id.edtFour:
                intent.putExtra("sign", "便祕");
                chosenView = edtFour;
                break;
            case R.id.edtFive:
                intent.putExtra("sign", "掉髮");
                chosenView = edtFive;
                break;
            case R.id.edtSix:
                intent.putExtra("sign", "疲倦");
                chosenView = edtSix;
                break;
            case R.id.edtSeven:
                intent.putExtra("sign", "食慾不振");
                chosenView = edtSeven;
                break;
            case R.id.edtEight:
                intent.putExtra("sign", "口腔炎");
                chosenView = edtEight;
                break;
            case R.id.edtNine:
                intent.putExtra("sign", "手足症候群");
                chosenView = edtNine;
                break;
        }
        startActivityForResult(intent, 1);
    }

    private void setViews(View view) {
        edtDate = view.findViewById(R.id.edtDate);
        edtOne = view.findViewById(R.id.edtOne);
        edtTwo = view.findViewById(R.id.edtTwo);
        edtThree = view.findViewById(R.id.edtThree);
        edtFour = view.findViewById(R.id.edtFour);
        edtFive = view.findViewById(R.id.edtFive);
        edtSix = view.findViewById(R.id.edtSix);
        edtSeven = view.findViewById(R.id.edtSeven);
        edtEight = view.findViewById(R.id.edtEight);
        edtNine = view.findViewById(R.id.edtNine);
        edtHurt = view.findViewById(R.id.edtHurt);
        ImageButton btnCheck = view.findViewById(R.id.btnCheack);
        edtOne.setOnClickListener(this);
        edtTwo.setOnClickListener(this);
        edtThree.setOnClickListener(this);
        edtFour.setOnClickListener(this);
        edtFive.setOnClickListener(this);
        edtSix.setOnClickListener(this);
        edtSeven.setOnClickListener(this);
        edtEight.setOnClickListener(this);
        edtNine.setOnClickListener(this);
        edtHurt.setOnClickListener(mBtnHurt);
        btnCheck.setOnClickListener(mBtnCheck);
    }
}
