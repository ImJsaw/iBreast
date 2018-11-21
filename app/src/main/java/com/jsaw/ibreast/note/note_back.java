package com.jsaw.ibreast.note;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class note_back extends AppCompatActivity{
    ImageButton imgCal;
    TextView txtDate;
    TextView txtTreatment;
    TextView txtWeight;
    TextView txtArm;
    TextView txtSign;
    TextView txtOther;
    TextView txtMood;
    TextView txtRemark;
    EditText edtDate;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_back);
        setViews();

        //自動帶入日期
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        edtDate.setText(sdf.format(date));
    }

    public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_test:
                final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
                Query testQuery = mDatabase.orderByChild("Date").startAt(edtDate.getText().toString())
                        .endAt(edtDate.getText().toString());
                break;
        }
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
//                顯示從目前時間開始選
//                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
            datePickerDialog.show();
        }
    };

    private void firebaseGetData() {
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> items = new ArrayList<>();
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                dataSnapshot = dataSnapshot.child(user);
                Query testQuery = mDatabase.orderByChild("Date").startAt(edtDate.getText().toString())
                        .endAt(edtDate.getText().toString());
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("date", data.child("date").getValue().toString());
                    item.put("wbc", data.child("wbc").getValue().toString());
                    item.put("hb", data.child("hb").getValue().toString());
                    item.put("plt", data.child("plt").getValue().toString());
                    items.add(item);
                }

//                progressDialog.dismiss();
//                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setViews(){
        txtDate = findViewById(R.id.txtDate);
        txtTreatment = findViewById(R.id.txtTreatment);
        txtArm = findViewById(R.id.txtArm);
        txtMood = findViewById(R.id.txtMood);
        txtOther = findViewById(R.id.txtOther);
        txtRemark = findViewById(R.id.txtRemark);
        txtSign = findViewById(R.id.txtSign);
        txtWeight = findViewById(R.id.txtWeight);
        imgCal = findViewById(R.id.imgCal);
        imgCal.setOnClickListener(imgCalOnClick);
        edtDate = findViewById(R.id.edtDate);

    }
}
