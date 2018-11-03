package com.jsaw.ibreast.note.food;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ToggleButton;

import com.jsaw.ibreast.R;
import java.util.ArrayList;
import java.util.List;
import lecho.lib.hellocharts.gesture.ContainerScrollType;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;


public class note_food_overview extends Fragment {

    LineChartData data = new LineChartData();
    Axis axisY = new Axis();
    Axis axisX = new Axis();
    private ToggleButton Viewtoggle;
    private LineChartView lineChart;
    String[] date = {"10-22","11-22","12-22","1-22","6-22","5-23","5-22","6-22","5-23","5-22"};//X轴的标注
    int[] score= {50,42,90,33,10,74,22,18,79,20};//图表的数据点
    private List<PointValue> mPointValues = new ArrayList<>();
    private List<AxisValue> mAxisXValues = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate layout for this fragment
        return inflater.inflate(R.layout.fragment_note_food_all, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        lineChart = getView().findViewById(R.id.food_graph);
        Viewtoggle = getView().findViewById(R.id.toggleWeekButton);
        Viewtoggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    axisY.setTextColor(Color.GREEN);
                    axisY.setTextSize(10);
                    data.setAxisYLeft(axisY);
                    lineChart.setLineChartData(data);
                }
                else {
                    axisY.setTextColor(Color.BLACK);
                    axisY.setTextSize(10);
                    data.setAxisYLeft(axisY);
                    lineChart.setLineChartData(data);
                }
            }
        });
        getAxisXLables();
        getAxisPoints();
        initLineChart();

    }

    private void getAxisXLables(){ for (int i = 0; i < date.length; i++) { mAxisXValues.add(new AxisValue(i).setLabel(date[i])); } }
    private void getAxisPoints(){ for (int i = 0; i < score.length; i++) { mPointValues.add(new PointValue(i, score[i])); }}

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




}