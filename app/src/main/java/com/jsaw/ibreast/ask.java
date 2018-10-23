package com.jsaw.ibreast;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.NavUtils;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
public class ask extends AppCompatActivity {
    private LinkedList<askAdapter.askData> eatData, diagnosisData, diseaseData, chemData,hormoneData,otherData,radiationData,surgeryData,targetData,trackData;
    private askAdapter eatAdapter, diagnosisAdapter,diseaseAdapter,chemAdapter,hormoneAdapter,otherAdapter,radiationAdapter,surgeryAdapter,targetAdapter,trackAdapter;
    private ListView listView_eat,listView_diagnosis,listView_disease,listView_chem,listView_hormone,listView_other,listView_radiation,listView_surgery,listView_target,listView_track;
    private ProgressDialog progressDialog;
    private Boolean isProgressDialogShow = false;
    private Boolean isEatShow,isDiagnosisShow, isDiseaseShow,isChemShow,isHormoneShow,isOtherShow,isRadiationShow,isSurgeryShow,isTargetShow,isTrackShow;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ask);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);//display "back" on action bar
        listView_eat = findViewById(R.id.list_eat);
        listView_diagnosis = findViewById(R.id.list_diagnosis);
        listView_disease = findViewById(R.id.list_disease);
        listView_chem = findViewById(R.id.list_chem);
        listView_hormone = findViewById(R.id.list_hormone);
        listView_other = findViewById(R.id.list_other);
        listView_radiation = findViewById(R.id.list_radiation);
        listView_surgery = findViewById(R.id.list_surgery);
        listView_target = findViewById(R.id.list_target);
        listView_track = findViewById(R.id.list_track);

        isEatShow = isDiagnosisShow = isDiseaseShow = isChemShow = isHormoneShow = isOtherShow = isRadiationShow = isSurgeryShow = isTargetShow = isTrackShow = true;

        //waiting dialog
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
                    Toast.makeText(ask.this, "連線逾時", Toast.LENGTH_SHORT).show();
                }
            }
        }, 5000);
        getData();

        findViewById(R.id.eat_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEatShow) listView_eat.setVisibility(View.GONE);
                else listView_eat.setVisibility(View.VISIBLE);
                isEatShow = !isEatShow;
            }
        });
        findViewById(R.id.diagnosis_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDiagnosisShow) listView_diagnosis.setVisibility(View.GONE);
                else listView_diagnosis.setVisibility(View.VISIBLE);
                isDiagnosisShow = !isDiagnosisShow;
            }
        });
        findViewById(R.id.disease_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDiseaseShow) listView_disease.setVisibility(View.GONE);
                else listView_disease.setVisibility(View.VISIBLE);
                isDiseaseShow = !isDiseaseShow;
            }
        });
        findViewById(R.id.chem_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChemShow) listView_chem.setVisibility(View.GONE);
                else listView_chem.setVisibility(View.VISIBLE);
                isChemShow = !isChemShow;
            }
        });
        findViewById(R.id.hormone_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isHormoneShow) listView_hormone.setVisibility(View.GONE);
                else listView_hormone.setVisibility(View.VISIBLE);
                isHormoneShow = !isHormoneShow;
            }
        });
        findViewById(R.id.other_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOtherShow) listView_other.setVisibility(View.GONE);
                else listView_other.setVisibility(View.VISIBLE);
                isOtherShow = !isOtherShow;
            }
        });
        findViewById(R.id.radiation_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isRadiationShow) listView_radiation.setVisibility(View.GONE);
                else listView_radiation.setVisibility(View.VISIBLE);
                isRadiationShow = !isRadiationShow;
            }
        });
        findViewById(R.id.surgery_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSurgeryShow) listView_surgery.setVisibility(View.GONE);
                else listView_surgery.setVisibility(View.VISIBLE);
                isSurgeryShow = !isSurgeryShow;
            }
        });
        findViewById(R.id.target_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTargetShow) listView_target.setVisibility(View.GONE);
                else listView_target.setVisibility(View.VISIBLE);
                isTargetShow = !isTargetShow;
            }
        });
        findViewById(R.id.track_show_area).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTrackShow) listView_track.setVisibility(View.GONE);
                else listView_track.setVisibility(View.VISIBLE);
                isTrackShow = !isTrackShow;
            }
        });

    }

    private void getData() {
        eatData = new LinkedList<>();
        diagnosisData = new LinkedList<>();
        diseaseData = new LinkedList<>();
        chemData = new LinkedList<>();
        hormoneData = new LinkedList<>();
        otherData = new LinkedList<>();
        radiationData = new LinkedList<>();
        surgeryData = new LinkedList<>();
        targetData  = new LinkedList<>();
        trackData = new LinkedList<>();
        FirebaseDatabase.getInstance().getReference("ask").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                DataSnapshot eat = dataSnapshot.child("eat");
                DataSnapshot diagnosis = dataSnapshot.child("diagnose");
                DataSnapshot disease = dataSnapshot.child("disease");
                DataSnapshot chem = dataSnapshot.child("chemotherapy");
                DataSnapshot hormone = dataSnapshot.child("hormone");
                DataSnapshot other = dataSnapshot.child("other");
                DataSnapshot radiation = dataSnapshot.child("radiation");
                DataSnapshot surgery = dataSnapshot.child("surgery");
                DataSnapshot target = dataSnapshot.child("target");
                DataSnapshot track = dataSnapshot.child("track");

                for (int i = 0; i < eat.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(eat.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(eat.child(String.valueOf(i)).child("text").getValue()).toString();
                    eatData.add(c);
                }
                for (int i = 0; i < diagnosis.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(diagnosis.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(diagnosis.child(String.valueOf(i)).child("text").getValue()).toString();
                    diagnosisData.add(c);
                }
                for (int i = 0; i < disease.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(disease.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(disease.child(String.valueOf(i)).child("text").getValue()).toString();
                    diseaseData.add(c);
                }
                for (int i = 0; i < chem.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(chem.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(chem.child(String.valueOf(i)).child("text").getValue()).toString();
                    chemData.add(c);
                }
                for (int i = 0; i < hormone.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(hormone.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(hormone.child(String.valueOf(i)).child("text").getValue()).toString();
                    hormoneData.add(c);
                }
                for (int i = 0; i < other.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(other.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(other.child(String.valueOf(i)).child("text").getValue()).toString();
                    otherData.add(c);
                }
                for (int i = 0; i < radiation.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(radiation.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(radiation.child(String.valueOf(i)).child("text").getValue()).toString();
                    radiationData.add(c);
                }
                for (int i = 0; i < surgery.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(surgery.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(surgery.child(String.valueOf(i)).child("text").getValue()).toString();
                    surgeryData.add(c);
                }
                for (int i = 0; i < target.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(target.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(target.child(String.valueOf(i)).child("text").getValue()).toString();
                    targetData.add(c);
                }
                for (int i = 0; i < track.getChildrenCount(); i++) {
                    askAdapter.askData c = new askAdapter.askData();
                    c.title = Objects.requireNonNull(track.child(String.valueOf(i)).child("title").getValue()).toString();
                    c.text = Objects.requireNonNull(track.child(String.valueOf(i)).child("text").getValue()).toString();
                    trackData.add(c);
                }
                diagnosisAdapter.notifyDataSetChanged();
                eatAdapter.notifyDataSetChanged();
                diseaseAdapter.notifyDataSetChanged();
                chemAdapter.notifyDataSetChanged();
                hormoneAdapter.notifyDataSetChanged();
                otherAdapter.notifyDataSetChanged();
                radiationAdapter.notifyDataSetChanged();
                surgeryAdapter.notifyDataSetChanged();
                targetAdapter.notifyDataSetChanged();
                trackAdapter.notifyDataSetChanged();
                progressDialog.dismiss();
                isProgressDialogShow = false;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        //listView
        eatAdapter = new askAdapter( eatData, ask.this);
        diagnosisAdapter = new askAdapter( diagnosisData, ask.this);
        diseaseAdapter = new askAdapter( diseaseData, ask.this);
        hormoneAdapter = new askAdapter( hormoneData, ask.this);
        chemAdapter = new askAdapter( chemData, ask.this);
        otherAdapter = new askAdapter( otherData, ask.this);
        radiationAdapter = new askAdapter( radiationData, ask.this);
        surgeryAdapter = new askAdapter( surgeryData, ask.this);
        targetAdapter = new askAdapter( targetData, ask.this);
        trackAdapter = new askAdapter( trackData, ask.this);
        listView_eat.setAdapter(eatAdapter);
        listView_diagnosis.setAdapter(diagnosisAdapter);
        listView_disease.setAdapter(diseaseAdapter);
        listView_hormone.setAdapter(hormoneAdapter);
        listView_chem.setAdapter(chemAdapter);
        listView_other.setAdapter(otherAdapter);
        listView_radiation.setAdapter(radiationAdapter);
        listView_surgery.setAdapter(surgeryAdapter);
        listView_target.setAdapter(targetAdapter);
        listView_track.setAdapter(trackAdapter);
    }

    //action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent upIntent = NavUtils.getParentActivityIntent(this);
                if (NavUtils.shouldUpRecreateTask(this, Objects.requireNonNull(upIntent))) {
                    TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(upIntent)
                            .startActivities();
                } else {
                    upIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    NavUtils.navigateUpTo(this, upIntent);
                }
                return true;
            default:
                return true;
        }
    }
}
