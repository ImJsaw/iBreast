package com.jsaw.ibreast.note.treat;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class note_treat3_add extends Fragment implements View.OnClickListener{
    private static final int[] IDS = new int[]{R.id.checkbox_listview, R.id.engtxt_listview, R.id.chitxt_listview};
    private static final String[] STRINGS = new String[]{"checkbox", "engName", "chiName"};
    private EditText edtStartDate;
    private EditText edtEndDate;
    private ImageButton imgCalStart;
    private ImageButton imgCalEnd;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.note_treat3_add, container, false);
        edtStartDate = view.findViewById(R.id.edtStartDate);
        edtEndDate = view.findViewById(R.id.edtEndDate);
        imgCalStart = view.findViewById(R.id.imgCalStart);
        imgCalEnd = view.findViewById(R.id.imgCalEnd);
        imgCalStart.setOnClickListener(this);
        imgCalEnd.setOnClickListener(this);

        firebaseGetData(view);

        return view;
    }





    public void onClick(final View view) {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog;
        switch (view.getId()) {
            case R.id.imgCalStart:
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtStartDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
            case R.id.imgCalEnd:
                datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                edtEndDate.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                            }
                        }, year, month, day);
                datePickerDialog.show();
                break;
        }
    }

    private void firebaseGetData(final View view) {
        FirebaseDatabase.getInstance().getReference("treat_add").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                List<HashMap<String, Object>> items = new ArrayList<>();
                ListView listView = view.findViewById(R.id.list_noteAdd);

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.getKey().equals("treat3_add")) {
                        for (DataSnapshot data : ds.getChildren()) {
                            HashMap<String, Object> item = new HashMap<>();
                            item.put("engName", data.child("engName").getValue().toString());
                            item.put("chiName", data.child("chiName").getValue().toString());
                            items.add(item);
                        }

                    }
//                1. Context context 執行環境
//                2. List<? extends Map<String, ?>> data 帶入的資料
//                3. int resource Layout位置
//                4. String[] from data帶入資料的Key
//                5. int[] to Key的值要帶到哪個元件
                    SimpleAdapter sa = new SimpleAdapter(getContext(), items, R.layout.note_list_view,
                            STRINGS, IDS);
                    listView.setAdapter(sa);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
