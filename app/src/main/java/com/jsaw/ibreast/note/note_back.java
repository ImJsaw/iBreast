package com.jsaw.ibreast.note;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.service.autofill.Dataset;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class note_back extends AppCompatActivity {
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseUser users = auth.getCurrentUser();
    private final String user = users.getUid();
    private ImageButton imgCal;
    private TextView txtDate;
    private TextView txtTreatment;
    private TextView txtWeight;
    private TextView txtTemp;
    private TextView txtArm;
    private TextView txtSign;
    private TextView txtOther;
    private TextView txtMood;
    private TextView txtRemark;
    private EditText edtDate;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/dd");

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_back);
        setViews();

        //自動帶入日期
        Date date = new Date();
        edtDate.setText(sdf.format(date));
        txtDate.setText(edtDate.getText());


    }

    private void showInfo(String date) {
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users").child(user);
        Query query = mDatabase.child("體重").orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(weightListener);
        query = mDatabase.child("體溫").orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(tempListener);
        query = mDatabase.child("化療").orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(treatmentListener);
        query = mDatabase.child("手臂").orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(armListener);
        query = mDatabase.child("症狀").orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(signListener);
        query = mDatabase.child("其他").orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(otherListener);
        query = mDatabase.child("心情").orderByChild("date").equalTo(date);
        query.addListenerForSingleValueEvent(moodListener);
    }

    //日曆按鈕
    private View.OnClickListener imgCalOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(note_back.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            edtDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                        }
                    }, year, month, day);
            datePickerDialog.show();
        }
    };

    TextWatcher dateChangeListener = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            txtDate.setText(s.toString());
            showInfo(s.toString());
        }
    };

    public void onClick(View view) {
        try {
            Date date = sdf.parse(edtDate.getText().toString());
            switch (view.getId()) {
                case R.id.btn_last:
                    Date nextDate = new Date(date.getTime() - (1000 * 60 * 60 * 24));
                    edtDate.setText(sdf.format(nextDate));
                    break;
                case R.id.btn_next:
                    Date lastDate = new Date(date.getTime() + (1000 * 60 * 60 * 24));
                    edtDate.setText(sdf.format(lastDate));
                    break;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    ValueEventListener treatmentListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            StringBuilder show = new StringBuilder();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                data = data.child("part");
                for (DataSnapshot cData : data.getChildren()) {
                    show.append(cData.getValue()).append("\n");
                }
            }
            if (show.length() == 0) {
                show.append("查無資訊");
            } else {
                show = show.deleteCharAt(show.length() - 1);
            }
            txtTreatment.setText(show);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener weightListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            StringBuilder show = new StringBuilder();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                for (DataSnapshot cData : data.getChildren()) {
                    if (cData.getKey().equals("weight")) {
                        show.append(cData.getValue() + "kg");
                    }
                }
            }
            if (show.length() == 0) {
                show.append("查無資訊");
            }
            txtWeight.setText(show);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener tempListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            List<HashMap<String, String>> show = new LinkedList<>();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                HashMap<String, String> record = new HashMap<>();
                for (DataSnapshot cData : data.getChildren()) {
                    String key = cData.getKey();
                    if (key.equals("temp")) {
                        record.put("temp", cData.getValue().toString());
                    } else if (key.equals("time")) {
                        record.put("time", cData.getValue().toString());
                    }
                }
                show.add(record);

            }

            if (show.size() == 0) {
                txtTemp.setText("查無資訊");
            } else {
                StringBuilder showup = new StringBuilder();
                for (HashMap<String, String> record : show) {
                    showup.append(record.get("time") + " ");
                    showup.append(record.get("temp") + "度\n");
                }
                showup = showup.deleteCharAt(showup.length() - 1);
                txtTemp.setText(showup);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener armListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Map<String, String> record = new LinkedHashMap<>();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                data = data.child("record");
                for (DataSnapshot cData : data.getChildren()) {
                    String key = cData.getKey();
                    String value = cData.getValue().toString();
                    record.put(key, value);
                }
            }

            if (record.size() == 0) {
                txtArm.setText("查無資訊");
            } else {
                StringBuilder show = new StringBuilder();
                int count = 0;
                for (Map.Entry entry : record.entrySet()) {
                    if (count % 3 == 0 && show.length() != 0) {
                        show.append("\n").append(entry.getKey()).append(entry.getValue());
                    } else {
                        show.append(entry.getKey()).append(entry.getValue());
                    }
                    count++;
                }
                txtArm.setText(show);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener signListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            String[] myKey = {"噁心", "嘔吐", "腹瀉", "便祕", "掉髮", "疲倦", "食慾不振", "口腔炎", "手足症候群", "疼痛指數"};
            Map<String, String> record = new LinkedHashMap<>();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                data = data.child("record");
                for (DataSnapshot cData : data.getChildren()) {
                    String key = cData.getKey();
                    String value = cData.getValue().toString();
                    record.put(key, value);
                }
            }
            if (record.size() == 0) {
                txtSign.setText("查無資訊");
            } else {
                StringBuilder show = new StringBuilder();
                int count = 0;
                for (Map.Entry entry : record.entrySet()) {
                    show.append(myKey[count]).append(entry.getValue()).append("\n");
                    count++;
                }
                show = show.deleteCharAt(show.length() - 1);
                txtSign.setText(show);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    ValueEventListener otherListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Map<String, String> record = new LinkedHashMap<>();
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                data = data.child("record");
                for (DataSnapshot cData : data.getChildren()) {
                    String key = cData.getKey();
                    String value = cData.getValue().toString();
                    record.put(key, value);
                }
            }
            if (record.size() == 0) {
                txtOther.setText("查無資訊");
            } else {
                StringBuilder show = new StringBuilder();
                for (Map.Entry entry : record.entrySet()) {
                    show.append(entry.getValue()).append("\n");
                }
                show = show.deleteCharAt(show.length() - 1);
                txtOther.setText(show);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
        }
    };

    ValueEventListener moodListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            int show = -1;
            for (DataSnapshot data : dataSnapshot.getChildren()) {
                for (DataSnapshot cData : data.getChildren()) {
                    if (cData.getKey().equals("score")) {
                        show = Integer.valueOf(cData.getValue().toString());
                        break;
                    }
                }
            }

            if (show == -1) {
                txtMood.setText("查無資訊");
            } else {
                String str;
                if (show >= 0 && show <= 5) {
                    str = "(正常)";
                } else if (show >= 6 && show <= 9) {
                    str = "(輕度)";
                } else if (show >= 10 && show < 15) {
                    str = "(中度)";
                } else {
                    str = "(重度)";
                }
                txtMood.setText(show + "分" + str);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    };

    private void setViews() {
        txtDate = findViewById(R.id.txtDate);
        txtTreatment = findViewById(R.id.txtTreatment);
        txtArm = findViewById(R.id.txtArm);
        txtMood = findViewById(R.id.txtMood);
        txtTemp = findViewById(R.id.txtTemp);
        txtOther = findViewById(R.id.txtOther);
        txtRemark = findViewById(R.id.txtRemark);
        txtSign = findViewById(R.id.txtSign);
        txtWeight = findViewById(R.id.txtWeight);
        imgCal = findViewById(R.id.imgCal);
        imgCal.setOnClickListener(imgCalOnClick);
        edtDate = findViewById(R.id.edtDate);
        edtDate.addTextChangedListener(dateChangeListener);
    }
}
