package com.jsaw.ibreast.note.body;

import android.annotation.SuppressLint;
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
import android.view.KeyEvent;
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

import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class body4_add extends Fragment {
    private EditText edtDate;
    private EditText edtTime;
    private EditText edtRight1;
    private EditText edtLeft1;
    private TextView txtMinus1;
    private EditText edtRight2;
    private EditText edtLeft2;
    private TextView txtMinus2;
    private EditText edtRight3;
    private EditText edtLeft3;
    private TextView txtMinus3;
    private Button btnHow;
    private TextView txtWarn;
    private ImageButton btnCheck;
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private HashMap<String, String> data = new HashMap<>();
    private final DecimalFormat decimalFormat = new DecimalFormat("#.#");

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
        final View view = inflater.inflate(R.layout.activity_note_body4_add, container, false);
        setViews(view);
        btnCheck.setOnClickListener(mBtnCheck);

        btnHow = view.findViewById(R.id.btnHow);
        btnCheck = view.findViewById(R.id.btnCheck);

        //自動帶入時間
        Calendar calendar = Calendar.getInstance();
        String time = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)) + ":" + String.valueOf(calendar.get(Calendar.MINUTE));
        edtTime.setText(time);

        //自動帶日期
        String date = calendar.get(Calendar.YEAR) + "/" + (calendar.get(Calendar.MONTH) + 1) + "/" + calendar.get(Calendar.DAY_OF_MONTH);
        edtDate.setText(date);

        // 設定小日曆選擇時間
        ImageButton selectDate = view.findViewById(R.id.imgCal);
        selectDate.setOnClickListener(imgCalOnClick);

        isProgressDialogShow = false;
        progressDialog.dismiss();

        return view;

    }

    // 確認儲存按鈕
    private View.OnClickListener mBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            setData();
            saveData(edtDate.getText().toString(), edtTime.getText().toString(), data);
            isProgressDialogShow = false;
            progressDialog.dismiss();
            startActivity(new Intent().setClass(getContext(), note_body.class));
        }
    };

    // 如何測量按鈕
    private View.OnClickListener mBtnHowClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startActivity(new Intent().setClass(getContext(), body4_add_how.class));
        }
    };

    // 判斷相差是否超過2cm
    private TextWatcher mEdtAct = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            float minus1 = 0;
            float minus2 = 0;
            float minus3 = 0;
            if (!edtRight1.getText().toString().isEmpty() && !edtLeft1.getText().toString().isEmpty()) {
                minus1 = Math.abs(Float.valueOf(edtRight1.getText().toString()) - Float.valueOf(edtLeft1.getText().toString()));
                minus1 = Float.valueOf(decimalFormat.format(minus1));
                txtMinus1.setText(String.valueOf(minus1));
            }
            else {
                txtMinus1.setText("");
            }
            if (minus1 > 2) {
                txtMinus1.setTextColor(Color.RED);
            } else {
                txtMinus1.setTextColor(Color.BLACK);
            }
            if (!edtRight2.getText().toString().isEmpty() && !edtLeft2.getText().toString().isEmpty()) {
                minus2 = Math.abs(Float.valueOf(edtRight2.getText().toString()) - Float.valueOf(edtLeft2.getText().toString()));
                minus2 = Float.valueOf(decimalFormat.format(minus2));
                txtMinus2.setText(String.valueOf(minus2));
            }
            else {
                txtMinus2.setText("");
            }
            if (minus2 > 2) {
                txtMinus2.setTextColor(Color.RED);
            } else {
                txtMinus2.setTextColor(Color.BLACK);
            }
            if (!edtRight3.getText().toString().isEmpty() && !edtLeft3.getText().toString().isEmpty()) {
                minus3 = Math.abs(Float.valueOf(edtRight3.getText().toString()) - Float.valueOf(edtLeft3.getText().toString()));
                minus3 = Float.valueOf(decimalFormat.format(minus3));
                txtMinus3.setText(String.valueOf(minus3));
            }
            else {
                txtMinus3.setText("");
            }
            if (minus3 > 2) {
                txtMinus3.setTextColor(Color.RED);
            } else {
                txtMinus3.setTextColor(Color.BLACK);
            }
            if (minus1 >= 2 || minus2 >= 2 || minus3 >= 2){
                txtWarn.setVisibility(View.VISIBLE);
            } else {
                txtWarn.setVisibility(View.INVISIBLE);
            }
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

    // 儲存資料
    private void saveData(String date, String time, HashMap<String, String> data) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(date, time, data);
        if (!date.isEmpty() && !time.isEmpty() && data.size()>0) {
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

    private void setViews(View view){
        txtWarn = view.findViewById(R.id.txtWarn);
        edtDate = view.findViewById(R.id.edtDate);
        edtTime = view.findViewById(R.id.edtTime);
        edtRight1 = view.findViewById(R.id.edtRight1);
        edtLeft1 = view.findViewById(R.id.edtLeft1);
        txtMinus1 = view.findViewById(R.id.txtMinus1);
        edtRight2 = view.findViewById(R.id.edtRight2);
        edtLeft2 = view.findViewById(R.id.edtLeft2);
        txtMinus2 = view.findViewById(R.id.txtMinus2);
        edtRight3 = view.findViewById(R.id.edtRight3);
        edtLeft3 = view.findViewById(R.id.edtLeft3);
        txtMinus3 = view.findViewById(R.id.txtMinus3);
        btnHow = view.findViewById(R.id.btnHow);
        btnHow.setOnClickListener(mBtnHowClick);
        btnCheck = view.findViewById(R.id.btnCheck);
        edtRight1.addTextChangedListener(mEdtAct);
        edtLeft1.addTextChangedListener(mEdtAct);
        edtRight2.addTextChangedListener(mEdtAct);
        edtLeft2.addTextChangedListener(mEdtAct);
        edtRight3.addTextChangedListener(mEdtAct);
        edtLeft3.addTextChangedListener(mEdtAct);

    }

    private void setData(){
        data.put("(1)右手", edtRight1.getText().toString());
        data.put("(1)左手", edtLeft1.getText().toString());
        data.put("(1)相差", txtMinus1.getText().toString());
        data.put("(2)右手", edtRight2.getText().toString());
        data.put("(2)左手", edtLeft2.getText().toString());
        data.put("(2)相差", txtMinus2.getText().toString());
        data.put("(3)右手", edtRight3.getText().toString());
        data.put("(3)左手", edtLeft3.getText().toString());
        data.put("(3)相差", txtMinus3.getText().toString());
    }

}
