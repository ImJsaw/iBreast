package com.jsaw.ibreast.note.move;

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

public class note_move_edit extends Fragment {

    private TextView dateText;
    private EditText timeText,moveText,calText,durationText;
    private TimePickerDialog fromTimePickerDialog;
    final DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private String yearStr,monthStr,dayStr;
    private LinkedList<moveCalRecord> moveData;
    private TableLayout moveTable;
    private Context mContext;

    private static class moveCalRecord {
        public String time;
        public String move;
        public String duration;
        public String cal;
        moveCalRecord(){}
        moveCalRecord(String time, String move, String duration,String cal) {
            this.time = time;
            this.move = move;
            this.duration = duration;
            this.cal = cal;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        return inflater.inflate(R.layout.fragment_note_move_edit, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ImageButton openCalender = getView().findViewById(R.id.imgCal);
        Button addRecord = getView().findViewById(R.id.addMoveDay);

        //Toast.makeText(getContext(),user,Toast.LENGTH_LONG).show();
        dateText = getView().findViewById(R.id.edtDate);
        moveTable = getView().findViewById(R.id.move_daytable);
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
                final View dialoglayout = inflater.inflate(R.layout.dialog_note_move_add,null);
                //Building dialog
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                timeText = dialoglayout.findViewById(R.id.EditTextTime);
                moveText = dialoglayout.findViewById(R.id.EditTextMove);
                durationText = dialoglayout.findViewById(R.id.EditTextMoveDuration);
                calText = dialoglayout.findViewById(R.id.EditTextMoveCalories);


                timeText.setInputType(InputType.TYPE_NULL);
                timeText.requestFocus();
                setTime();
                builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveData(timeText.getText().toString(),moveText.getText().toString(),durationText.getText().toString(),calText.getText().toString());
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

    private void saveData(String time,String move,String duration,String cal) {

        final moveCalRecord record = new moveCalRecord(time, move, duration,cal);

        if (!time.equals("") && !move.equals("") && !cal.equals("")) {
            mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String count = String.valueOf(dataSnapshot.child(user).child("moveCal").child(yearStr).child(monthStr).child(dayStr).getChildrenCount());
                    mDatabase.child(user).child("moveCal").child(yearStr).child(monthStr).child(dayStr).child(count).setValue(record);
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
        for (int i = 2; i < moveTable.getChildCount(); i++) {
            View child = moveTable.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
        for (int i = 0; i <moveData.size(); i++) addTableRow(moveTable, moveData.get(i),i);
    }

    private void getData(){

        final DatabaseReference dbRef = mDatabase.child(user).child("moveCal").child(yearStr).child(monthStr).child(dayStr);
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                moveData = new LinkedList<>();
                for (int i = 0; i < dataSnapshot.getChildrenCount(); i++) {
                    moveCalRecord c = new moveCalRecord();
                    c.time = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("time").getValue()).toString();
                    c.move = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("move").getValue()).toString();
                    c.duration = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("duration").getValue()).toString();
                    c.cal = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i)).child("cal").getValue()).toString();
                    moveData.add(c);
                }
                refresh();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void deleteData(final int tag) {
        final DatabaseReference dbRef = mDatabase.child(user).child("moveCal").child(yearStr).child(monthStr).child(dayStr);
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //implement delete data
                //  **do not just delete**   json array need to be in order
                for (int i = tag; i < dataSnapshot.getChildrenCount()-1 ; i++) {
                    //move next data to current
                    moveCalRecord c = new moveCalRecord();
                    c.time = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i+1)).child("time").getValue()).toString();
                    c.move = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i+1)).child("move").getValue()).toString();
                    c.duration = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i+1)).child("duration").getValue()).toString();
                    c.cal = Objects.requireNonNull(dataSnapshot.child(String.valueOf(i+1)).child("cal").getValue()).toString();
                    mDatabase.child(user).child("moveCal").child(yearStr).child(monthStr).child(dayStr).child(String.valueOf(i)).setValue(c);
                }
                //remove the last one
                mDatabase.child(user).child("moveCal").child(yearStr).child(monthStr).child(dayStr).child(String.valueOf(dataSnapshot.getChildrenCount()-1)).removeValue();
                refresh();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void addTableRow(TableLayout tl, final moveCalRecord moveData,final int index){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        TableRow tr = (TableRow)inflater.inflate(R.layout.table_note_move, tl, false);
        tr.setTag(index);//save for delete data
        TextView textViewTime = tr.findViewById(R.id.TextViewTime);
        TextView textViewMove = tr.findViewById(R.id.TextViewMove);
        TextView textViewMoveDuration = tr.findViewById(R.id.TextViewMoveDuration);
        TextView textViewCalories = tr.findViewById(R.id.TextViewCalories);
        textViewTime.setText(moveData.time);
        textViewMove.setText(moveData.move);
        textViewMoveDuration.setText(moveData.duration);
        textViewCalories.setText(moveData.cal);

        final String s = "確定刪除"+monthStr+"/"+ dayStr+"的"+moveData.move+"嗎？";

        tr.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final AlertDialog d = new AlertDialog.Builder(mContext)
                        .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                //deleteRecord
                                deleteData(index);
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