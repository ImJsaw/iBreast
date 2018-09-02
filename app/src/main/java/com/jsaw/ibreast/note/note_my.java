package com.jsaw.ibreast.note;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.Map;

public class note_my extends AppCompatActivity {
    private EditText edtBirth;
    private EditText edtDate;
    private EditText edtHeight;
    private CheckBox checkBox;
    private static final int[] IDS = new int[]{R.id.checkbox_listview, R.id.chitxt_listview};
    private static final String[] STRINGS = new String[]{"checkbox", "chiName"};
    private Boolean isProgressDialogShow = false;
    private ProgressDialog progressDialog;
    private List<String> method = new ArrayList<>();
    private List<String> program = new ArrayList<>();
    private String message;
    private Map<String, String> map = new HashMap<>();

    private static class Record {
        public String Birth;
        public String Height;
        public String StopBleed;
        public String SugeryDate;
        public String SugeryMethod;
        public String Cell;
        public String Horm;
        public String Her;
        public String Fish;
        public String Program;

        Record(HashMap<String, String> record) {
            this.Birth = record.get("birth");
            this.Height = record.get("height");
            this.StopBleed = record.get("stopBleed");
            this.SugeryDate = record.get("sugeryDate");
            this.SugeryMethod = record.get("sugeryMethod");
            this.Cell = record.get("cell");
            this.Horm = record.get("horm");
            this.Her = record.get("her");
            this.Fish = record.get("fish");
            this.Program = record.get("program");
        }
    }

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
                // 空白，回到note_my
                if (!checkBlank()) {
                    setContentView(R.layout.note_my);
                } else {
                    // 有資料則跳 dialog 詢問是否儲存
                    Toast.makeText(note_my.this, "是否要儲存", Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.Btn_edit:
                setContentView(R.layout.test);
                onEdit();
                break;
            case R.id.Btn_save:
                //儲存資料
                if (checkBlank()) {
                    Toast.makeText(note_my.this, "ready to save", Toast.LENGTH_SHORT).show();
                    setDialog();

                    setContentView(R.layout.note_my); //儲存完回到 note_my
                } else {
                    Toast.makeText(note_my.this, message, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void onEdit() {
        edtBirth = findViewById(R.id.edtBirth);
        edtDate = findViewById(R.id.edtDate);
        edtHeight = findViewById(R.id.edtHeight);
        setDialog();
        getMethodData();
        getProgramData();

    }

    private void getMethodData() {
        FirebaseDatabase.getInstance().getReference("my").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> method_items = new ArrayList<>();
                ListView listMethod = findViewById(R.id.list_method);

                dataSnapshot = dataSnapshot.child("method");
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    HashMap<String, Object> item = new HashMap<>();
                    item.put("chiName", data.getValue().toString());
                    method_items.add(item);
                }
                SimpleAdapter sa = new SimpleAdapter(note_my.this, method_items, R.layout.note_list_view,
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
                                    Toast.makeText(note_my.this, "選中了" + map.get("chiName"), Toast.LENGTH_SHORT).show();
                                    method.add((String) map.get("chiName"));
                                } else {
                                    method.remove(map.get("chiName"));
                                }
                            }
                        });
                        return v;
                    }
                };
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
                }

                SimpleAdapter sa = new SimpleAdapter(note_my.this, program_items, R.layout.note_list_view,
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
                                    Toast.makeText(note_my.this, "選中了" + map.get("chiName"), Toast.LENGTH_SHORT).show();
                                    program.add((String) map.get("chiName"));
                                } else {
                                    program.remove(map.get("chiName"));
                                }
                            }
                        });
                        return v;
                    }
                };
                listProgram.setAdapter(sa);
                progressDialog.dismiss();
                isProgressDialogShow = false;
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

    private void saveData(){
        final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
        final Record record = null;

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser users = auth.getCurrentUser();
                String user = users.getUid();
                String count = String.valueOf(dataSnapshot.child(user).child("weight").getChildrenCount() + 1);
                mDatabase.child(user).child("weight").child(count).setValue(record);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //將資料製作成 Map
    private void setData() {
        RadioButton rdbYes = findViewById(R.id.rdbYes);
        Spinner spCell = findViewById(R.id.spinnerCell);
        Spinner spEr = findViewById(R.id.spinnerEr);
        Spinner spPr = findViewById(R.id.spinnerPr);
        Spinner spHer = findViewById(R.id.spinnerHer);
        Spinner spFish = findViewById(R.id.spinnerFish);


        map.put("birth", edtBirth.getText().toString());
        map.put("height", edtHeight.getText().toString());
        map.put("stopBleed", rdbYes.isChecked() ? "是" : "否");
        map.put("sugeryMethod", edtDate.getText().toString());
        map.put("cell", spCell.toString());

    }

    // 是否有為填選欄位
    private boolean checkBlank() {
        if (edtBirth.getText().toString().trim().equals("")) {
            message = "生日欄位不可為空白";
            return false;
        } else if (edtDate.getText().toString().trim().equals("")) {
            message = "手術年月欄位不可為空白";
            return false;
        } else if (edtHeight.getText().toString().trim().equals("")) {
            message = "身高欄位不可為空白";
            return false;
        } else if (method.size() == 0) {
            message = "請勾選述式";
            return false;
        } else if (program.size() == 0) {
            message = "請勾選療程計畫";
            return false;
        } else {
            return true;
        }
    }
}
