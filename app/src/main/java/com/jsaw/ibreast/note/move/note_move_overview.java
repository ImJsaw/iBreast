package com.jsaw.ibreast.note.move;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jsaw.ibreast.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class note_move_overview extends Fragment {

    LineChartData data = new LineChartData();
    Axis axisY = new Axis();
    Axis axisX = new Axis();
    private LineChartView lineChart;
    String[] monthDate = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};
    String[] dayDate = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};
    int[] monthData= {50,42,90,33,10,74,22,18,79,20};
    int[] dayData= {50,42,90,33,10,74,22,18,79,20};
    String[] date = {};
    int[] cal = {};
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();
    final int MONTH_MODE = 0;
    final int DAY_MODE = 1;
    private int mode = 0;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        return inflater.inflate(R.layout.fragment_note_graph_overview, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lineChart = getView().findViewById(R.id.food_graph);
        getData();

        ToggleButton viewToggle = getView().findViewById(R.id.toggleWeekButton);
        viewToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                  mode = DAY_MODE;
                  switchDay();
                }
                else {
                    mode = MONTH_MODE;
                    switchMonth();
                }
            }
        });


        getAxisXLables();
        getAxisPoints();
        initLineChart();

    }

    private void getAxisXLables(){
        mAxisXValues.clear();
        for (int i = 0; i < date.length; i++) {
            if(date[i] == null) break;
            mAxisXValues.add(new AxisValue(i).setLabel(date[i]));
        }
    }
    private void getAxisPoints(){
        mPointValues.clear();
        for (int i = 0; i < cal.length; i++) {
            if(cal[i] == 0) break;
            Log.d("axis",String.valueOf(cal[i]));
            mPointValues.add(new PointValue(i, cal[i]));
        }
    }

    private void switchMonth(){
        date = monthDate;
        cal = monthData;
        getAxisXLables();
        getAxisPoints();
        initLineChart();
    }

    private void switchDay(){
        date = dayDate;
        cal = dayData;
        getAxisXLables();
        getAxisPoints();
        initLineChart();
    }

    private void initLineChart(){
        Line line = new Line(mPointValues).setColor(Color.parseColor("#FFCD41")); //string color
        List<Line> lines = new ArrayList<>();
        lines.add(line);

        data.setLines(lines);
        //bind line data
        axisX.setTextColor(Color.BLACK);
        axisX.setTextSize(10);
        axisX.setMaxLabelChars(8);
        axisX.setValues(mAxisXValues);
        data.setAxisXBottom(axisX);

        axisY.setName("kcal");
        axisY.setTextColor(Color.BLACK);
        axisY.setTextSize(10);
        data.setAxisYLeft(axisY);
        lineChart.setLineChartData(data);
        //set chart mode
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);
        lineChart.setMaxZoom(2);
        lineChart.setContainerScrollEnabled(true, ContainerScrollType.HORIZONTAL);
        lineChart.setVisibility(View.VISIBLE);
        Viewport v = new Viewport(lineChart.getMaximumViewport()); v.left = 0; v.right= 7; lineChart.setCurrentViewport(v);
    }

    private void getData(){
        final String user = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users").child(user).child("moveCal");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                monthDate = new String[50];
                dayDate = new String[200];
                monthData = new int[50];
                dayData = new int[200];
                int monthIndex = -1, dayIndex = -1;

                for (int year = 2017; year < 2030; year++) if ( dataSnapshot.hasChild(String.valueOf(year)) ) {
                    for (int month = 1; month <= 12; month++) if ( dataSnapshot.child(String.valueOf(year)).hasChild(String.valueOf(month))){
                        //month loop
                        monthIndex++;
                        monthDate[monthIndex] = year + "/" + month;
                        monthData[monthIndex] = 0;
                        for (int day = 1; day <= 31; day++) if ( dataSnapshot.child(String.valueOf(year)).child(String.valueOf(month)).hasChild(String.valueOf(day))){
                            //day loop
                            dayIndex++;
                            dayDate[dayIndex] = month + "/" + day;
                            dayData[dayIndex] = 0;
                            DataSnapshot db = dataSnapshot.child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(day));
                            for (int i = 0; i < db.getChildrenCount(); i++){
                                monthData[monthIndex] += Integer.parseInt(Objects.requireNonNull(db.child(String.valueOf(i)).child("cal").getValue()).toString());
                                dayData[dayIndex] += Integer.parseInt(Objects.requireNonNull(db.child(String.valueOf(i)).child("cal").getValue()).toString());
                            }
                        }
                    }
                }
            if(mode == MONTH_MODE) switchMonth();
            if (mode == DAY_MODE) switchDay();
            Log.d("day", Arrays.toString(dayData));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}