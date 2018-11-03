package com.jsaw.ibreast.note.food;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.Objects;

public class note_food_edit extends Fragment {

    private TextView dateText;
    private EditText timeText,foodText,calText;
    private TimePickerDialog fromTimePickerDialog;
    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String yearStr,monthStr,dayStr;
    private LinkedList<foodCalRecord> foodData;
    private TableLayout foodTable;
    private Context mContext;

    private static class foodCalRecord {
        public String time;
        public String food;
        public String cal;
        foodCalRecord(){}
        foodCalRecord(String time, String food, String cal) {
            this.time = time;
            this.food = food;
            this.cal = cal;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        return inflater.inflate(R.layout.fragment_note_food_edit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton openCalender = getView().findViewById(R.id.imgCal);
        Button addRecord = getView().findViewById(R.id.addFoodDay);

        Toast.makeText(getContext(),user,Toast.LENGTH_LONG).show();
        dateText = getView().findViewById(R.id.edtDate);
        foodTable = getView().findViewById(R.id.food_daytable);
        //default day set to today
        yearStr = String.valueOf(Calendar.getInstance().get(Calendar.YEAR));
        monthStr = String.valueOf(Calendar.getInstance().get(Calendar.MONTH)+1);
        dayStr = String.valueOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        dateText.setText(yearStr + "/" + monthStr + "/" + dayStr);
        getData();

        View.OnClickListener setDateListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(mContext,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateText.setText(year + "/" + (month + 1) + "/" + dayOfMonth);
                                yearStr = String.valueOf(year);
                                monthStr = String.valueOf(month+1);
                                dayStr = String.valueOf(dayOfMonth);
                                getData();
                            }
                        }, year, month, day);
                datePickerDialog.show();

            }
        };

        openCalender.setOnClickListener(setDateListener);
        dateText.setOnClickListener(setDateListener);

        addRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                final View dialoglayout = inflater.inflate(R.layout.dialog_note_food_add,null);
                //Building dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                timeText = dialoglayout.findViewById(R.id.EditTextTime);
                foodText = dialoglayout.findViewById(R.id.EditTextFood);
                calText = dialoglayout.findViewById(R.id.EditTextFoodCalories);

                timeText.setInputType(InputType.TYPE_NULL);
                timeText.requestFocus();
                setTime();
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData(timeText.getText().toString(),foodText.getText().toString(),calText.getText().toString());
                        // refresh
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.setView(dialoglayout).create();
                dialog.show();
            }
        });


    }

    private void setTime(){
        timeText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fromTimePickerDialog.show();
            }
        });

        Calendar newCalendar = Calendar.getInstance();
        fromTimePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String minuteString = " : ";
                Calendar newDate = Calendar.getInstance();
                newDate.set(newDate.get(Calendar.YEAR), newDate.get(Calendar.MONTH), newDate.get(Calendar.DAY_OF_MONTH),hourOfDay,minute);
                if (minute < 10) minuteString += ("0" + minute);
                else minuteString += minute;
                timeText.setText(hourOfDay + minuteString);
            }
        },newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MONTH),true);


    }

    private void saveData(String time,String food,String cal) {

        final foodCalRecord record = new foodCalRecord(time, food, cal);

        if (!time.equals("") && !food.equals("") && !cal.equals("")) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String count = String.valueOf(dataSnapshot.child(user).child("foodCal").child(yearStr).child(monthStr).child(dayStr).getChildrenCount());
                    mDatabase.child(user).child("foodCal").child(yearStr).child(monthStr).child(dayStr).child(count).setValue(record);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                }
            });
        } else {
            Toast.makeText(getContext(), "儲存失敗", Toast.LENGTH_SHORT).show();
        }
    }

    private void refresh(){
        for (int i = 2; i < foodTable.getChildCount(); i++) {
            View child = foodTable.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        for (int i = 0; i <foodData.size(); i++) addTableRow(foodTable, foodData.get(i));
    }

    private void getData(){

        final DatabaseReference dbRef = mDatabase.child(user).child("foodCal").child(yearStr).child(monthStr).child(dayStr);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                foodData = new LinkedList<>();
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    foodCalRecord c = new foodCalRecord();
                    c.time = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("time").getValue()).toString();
                    c.food = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("food").getValue()).toString();
                    c.cal = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("cal").getValue()).toString();
                    foodData.add(c);
                }
                refresh();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addTableRow(TableLayout tl, final foodCalRecord foodData){
        LayoutInflater inflater = LayoutInflater.from(mContext);
        TableRow tr = (TableRow)inflater.inflate(R.layout.table_note_food, tl, false);
        TextView textViewTime = tr.findViewById(R.id.TextViewTime);
        TextView textViewFood = tr.findViewById(R.id.TextViewFood);
        TextView textViewCalories = tr.findViewById(R.id.TextViewCalories);
        textViewTime.setText(foodData.time);
        textViewFood.setText(foodData.food);
        textViewCalories.setText(foodData.cal);

        final String s = "確定刪除"+monthStr+":"+ dayStr+"的"+foodData.food+"嗎？";

        tr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog d = new AlertDialog.Builder(mContext)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //deleteRecord
                            }
                        })
                        .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setMessage( s )
                        .setTitle("刪除")
                        .create();
                d.show();
                return false;
            }
        });
        tl.addView(tr);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mContext = null;
    }



}