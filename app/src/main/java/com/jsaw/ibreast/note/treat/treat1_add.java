package com.jsaw.ibreast.note.treat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class treat1_add extends AppCompatActivity {
    private static final int[] IDS = new int[]{R.id.checkbox_listview, R.id.engtxt_listview, R.id.chitxt_listview};
    private static final String[] STRINGS = new String[]{"checkbox", "engName", "chiName"};
    private EditText edtDate;
    private EditText edtOther;
    private CheckBox ckbOther;
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private String message;
    private CheckBox checkBox;
    private List<String> item = new ArrayList<>();
    private ImageButton btnCheck;


    private static class Record {
        public List<String> part;

        Record(List<String> part) {
            this.part = part;
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_treat1_add);
        setDialog();
        edtDate = findViewById(R.id.edtDate);
        edtOther = findViewById(R.id.edtOther);
        btnCheck = findViewById(R.id.imgCheack);
        ckbOther = findViewById(R.id.ckbOther);
        btnCheck.setOnClickListener(mBtnCheck);
        ckbOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ckbOther.isChecked() && !edtOther.getText().toString().isEmpty()) {
                    item.add(edtOther.getText().toString());
                } else {
                    item.remove(edtOther.toString());
                }
            }
        });

        firebaseGetData();
        setButtons();
//        設定小日曆選擇時間
        ImageButton selectDate = findViewById(R.id.imgCal);
        selectDate.setOnClickListener(imgCalOnClick);


    }

    private void firebaseGetData() {
        FirebaseDatabase.getInstance().getReference("treat_add").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> items = new ArrayList<>();
                ListView listView = findViewById(R.id.list_noteAdd);

                dataSnapshot = dataSnapshot.child("treat1_add");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("engName", data.child("engName").getValue().toString());
                    item.put("chiName", data.child("chiName").getValue().toString());
                    items.add(item);
                }
//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                SimpleAdapter sa = new SimpleAdapter(treat1_add.this, items, R.layout.note_list_view,
                        STRINGS, IDS) {
                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = super.getView(position, convertView, parent);
                        final HashMap<String, Object> map = (HashMap<String, Object>) this.getItem(position);
                        checkBox = v.findViewById(R.id.checkbox_listview);
                        checkBox.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View vv) {
                                map.put("isSelect", ((CheckBox) vv).isChecked());
                                if (((CheckBox) vv).isChecked()) {
//                                    Toast.makeText(activity_note_my.this, "選中了" + map.get("item"), Toast.LENGTH_SHORT).show();
                                    item.add((String) map.get("engName"));
                                } else {
                                    item.remove(map.get("engName"));
                                }
                            }
                        });
                        return v;
                    }
                };
                listView.setAdapter(sa);
                setListViewHeightBasedOnChildren(listView);
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    // 儲存按鈕
    private View.OnClickListener mBtnCheck = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (checkBlank()) {
                saveData();

            } else {
                Toast.makeText(treat1_add.this, message, Toast.LENGTH_SHORT).show();
            }
        }
    };

    private View.OnClickListener imgCalOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(treat1_add.this,
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

    //Save Data
    private void saveData() {
        setDialog();
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                Record part_record = new Record(item);
                String count = String.valueOf(dataSnapshot.child(user).child("化療").getChildrenCount() + 1);
                mDatabase.child(user).child("化療").child(count).setValue(part_record);
                mDatabase.child(user).child("化療").child(count).child("date").setValue(edtDate.getText().toString());
                ckbOther.setChecked(false);
                item.clear();
                Toast.makeText(treat1_add.this, "儲存成功", Toast.LENGTH_SHORT).show();
                startActivity(new Intent().setClass(treat1_add.this, treat1.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        progressDialog.dismiss();
        isProgressDialogShow = false;

    }

    private void setDialog() {
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
                    Toast.makeText(treat1_add.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
    }

    /**
     * 是否有為填選欄位
     *
     * @return true:沒有空白欄位
     */
    private boolean checkBlank() {
        if (edtDate.getText().toString().trim().equals("")) {
            message = "日期不可為空白";
            return false;
        } else if (item.size() == 0) {
            message = "請勾選部位";
            return false;
        } else {
            return true;
        }
    }

    /**
     * scrollView 中嵌listView造成View高度異常，根據子View高度重置listVIew高度。
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        if (listView == null) return;
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    private void setButtons() {
        Button btn1 = findViewById(R.id.btn1);
        Button btn2 = findViewById(R.id.btn2);
        Button btn3 = findViewById(R.id.btn3);
        Button btn4 = findViewById(R.id.btn4);
        Button btn5 = findViewById(R.id.btn5);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1_add.this, treat1.class));
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1_add.this, treat2.class));
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1_add.this, treat3.class));
            }
        });
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1_add.this, treat4.class));
            }
        });
        btn5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent().setClass(treat1_add.this, treat5.class));
            }
        });

    }
}
