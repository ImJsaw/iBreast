package com.jsaw.ibreast.note.body;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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
import com.jsaw.ibreast.note.note;
import com.jsaw.ibreast.note.note_my;

import java.util.Calendar;


public class body1_add extends Fragment {
    private EditText edtDate;
    private EditText edtWeight;
    private TextView txtBMI;
    private TextView txtResult;
    private TextView txtAdvice;
    private ImageButton btnCheck;
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private double height = 1;

    private static class Record {
        public String BMI;
        public String weight;
        public String result;
        public String date;

        Record(String bmi, String weight, String result, String date) {
            this.BMI = bmi;
            this.weight = weight;
            this.result = result;
            this.date = date;
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
        final View view = inflater.inflate(R.layout.activity_note_body1_add, container, false);
        edtDate = view.findViewById(R.id.edtDate);
        edtWeight = view.findViewById(R.id.edtWeight);
        txtBMI = view.findViewById(R.id.txtBMI);
        txtResult = view.findViewById(R.id.txtResult);
        txtAdvice = view.findViewById(R.id.txtAdvice);
        btnCheck = view.findViewById(R.id.btnCheck);
        btnCheck.setOnClickListener(mBtnCheck);
        edtWeight.addTextChangedListener(mEdtAct);
        getHeight();

        // 設定小日曆選擇時間
        ImageButton selectDate = view.findViewById(R.id.imgCal);
        selectDate.setOnClickListener(imgCalOnClick);

        isProgressDialogShow = false;
        progressDialog.dismiss();

        return view;

    }

    //TextChange
    private TextWatcher mEdtAct = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String weight = edtWeight.getText().toString();
            if (!weight.isEmpty() && weight.length() >=2 ) {
                if (height != 1) {
                    double BMI = Double.parseDouble(weight) / Math.pow(height / 100, 2);
                    String BMIstr = String.valueOf(BMI);
                    // 取小數第一位
                    BMIstr = BMIstr.substring(0, BMIstr.indexOf(".") + 2);
                    txtBMI.setText(BMIstr);
                    txtResult.setText(getResult(Double.parseDouble(BMIstr)));
                    txtAdvice.setText(getAdvice(Double.parseDouble(BMIstr)));
                }
            } else {
                txtBMI.setText("");
                txtResult.setText("");
                txtAdvice.setText("");
            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    // 確認按鈕
    private View.OnClickListener mBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            saveData(txtBMI.getText().toString(), edtWeight.getText().toString(),
                    txtResult.getText().toString(), edtDate.getText().toString());
            isProgressDialogShow = false;
            progressDialog.dismiss();
            Toast.makeText(getContext(), "儲存成功", Toast.LENGTH_SHORT).show();
            startActivity(new Intent().setClass(getContext(), note_body.class));
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
    private void saveData(String bmi, String weight, String result, String date) {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = new Record(bmi, weight, result, date);
        if (!bmi.equals("")) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser users = auth.getCurrentUser();
                    String user = users.getUid();
                    String count = String.valueOf(dataSnapshot.child(user).child("體重").getChildrenCount() + 1);
                    mDatabase.child(user).child("體重").child(count).setValue(record);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(getContext(), "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }

    // 顯示結果
    private String getResult(double bmi) {
        if (bmi >= 0 & bmi < 18.5) {
            return "體重過輕";
        } else if (bmi >= 18.5 & bmi < 24) {
            return "體重正常";
        } else if (bmi >= 24 & bmi < 27) {
            return "體重過重";
        } else if (bmi >= 27 & bmi < 30) {
            return "輕度肥胖";
        } else if (bmi >= 30 & bmi < 35) {
            return "中度肥胖";
        } else {
            return "重度肥胖";
        }
    }

    // 顯示建議
    private String getAdvice(double bmi) {
        if (bmi >= 0 & bmi < 18.5) {
            return "您需要再吃營養些，讓自己重一些！";
        } else if (bmi >= 18.5 & bmi < 24) {
            return "很不錯喔，很標準，請您繼續保持！";
        } else if (bmi >= 24 & bmi < 27) {
            return "您得控制一下飲食了，請加油！";
        } else {
            return "肥胖容易引起疾病，您得要多多注意自己的健康囉！";
        }
    }

    private void getHeight() {
        DatabaseReference mGetHeight = FirebaseDatabase.getInstance().getReference("Users");
        mGetHeight.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                if (dataSnapshot.child(user).child("我的").hasChild("height")) {
                    dataSnapshot = dataSnapshot.child(user).child("我的").child("height");
                    height = Double.parseDouble((String) dataSnapshot.getValue());
                } else {
                    Toast.makeText(getContext(), "請先至'記吧 > 我的'頁面輸入身高。", Toast.LENGTH_LONG).show();
                    startActivity(new Intent().setClass(getContext(), note_my.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}
