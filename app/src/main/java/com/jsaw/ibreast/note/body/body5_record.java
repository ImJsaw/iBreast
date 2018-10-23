package com.jsaw.ibreast.note.body;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.jsaw.ibreast.R;

public class body5_record extends AppCompatActivity {
    private Button btnOne;
    private Button btnTwo;
    private Button btnThree;
    private Button btnFour;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_body5_record);

        Intent intent = getIntent();
        TextView txtSign = findViewById(R.id.txtSign);

        String sign = intent.getStringExtra("sign");
        txtSign.setText(sign);

        setBtnText(sign);
    }

    public void onClick(View v){
        Intent intent = new Intent();
        String level = "-1";
        switch (v.getId()){
            case R.id.btnNone:
                level = "第 0 級";
                break;
            case R.id.btnOne:
                level = "第 1 級";
                break;
            case R.id.btnTwo:
                level = "第 2 級";
                break;
            case R.id.btnThree:
                level = "第 3 級";
                break;
            case R.id.btnFour:
                level = "第 4 級";
                break;
            case R.id.btn_cancel:   // 返回按鈕
                body5_record.super.onBackPressed();
                break;
        }
        intent.putExtra("level", level);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private void setBtnText(String sign) {
        setViews();
        switch (sign) {
            case "噁心":
                btnOne.setText("沒有食慾，但未改變飲食習慣");
                btnTwo.setText("攝取量減少，但沒有明顯體重減輕、脫水、營養不良");
                btnThree.setText("攝取卡路與水分不足，需要鼻胃管灌食或營養針注射");
                btnFour.setVisibility(View.INVISIBLE);
                break;
            case "嘔吐":
                btnOne.setText("24小時內發生1-2次");
                btnTwo.setText("24小時內發生3-5次");
                btnThree.setText("24小時內發生大於6次，需要鼻胃管灌食或營養針注射");
                btnFour.setText("危及生命，需要緊急處理");
                break;
            case "腹瀉":
                btnOne.setText("每天比平日增加解便次數小於4次");
                btnTwo.setText("每天比平日增加解便次數4-6次");
                btnThree.setText("每天比平日增加解便次數大於7次，或失禁，且影響自我日常生活照顧功能，如穿衣、吃飯、洗澡等");
                btnFour.setText("危及生命，需要緊急處理");
                break;
            case "便祕":
                btnOne.setText("偶爾發生，偶爾需要使用軟便劑、瀉藥或灌腸緩解");
                btnTwo.setText("持續發生，規則使用軟便劑或瀉藥緩解。影響獨立生活功能，如烹煮、購物、打掃等");
                btnThree.setText("需要用手挖出糞便，影響自我日常生活照顧功能，如穿衣、吃飯、洗澡等");
                btnFour.setText("危及生命，需要緊急處理");
                break;
            case "掉髮":
                btnOne.setText("落髮小於50%，落髮量不明顯，只需要使用不同髮型來掩飾");
                btnTwo.setText("落髮大於50%，落髮量明顯，且需要假髮或髮片掩飾。\n");
                btnThree.setVisibility(View.INVISIBLE);
                btnFour.setVisibility(View.INVISIBLE);
                break;
            case "疲倦":
                btnOne.setText("休息後可緩解");
                btnTwo.setText("休息也無法緩解，限影響獨立生活功能，如烹煮、購物、打掃等");
                btnThree.setText("休息也無法緩解，影響自我日常生活照顧功能，如穿衣、吃飯、洗澡等");
                btnFour.setVisibility(View.INVISIBLE);
                break;
            case "食慾不振":
                btnOne.setText("沒有食慾，但未改變飲食習慣");
                btnTwo.setText("攝取量改變，但沒有明顯體重減輕或營養不良");
                btnThree.setText("明顯的體重減輕或營養不良，需要鼻胃管灌食或注射營養針");
                btnFour.setText("危及生命，需要緊急處理");
                break;
            case "口腔炎":
                btnOne.setText("症狀輕微，不需要處理");
                btnTwo.setText("中度疼痛，不影響由口進食");
                btnThree.setText("嚴重疼痛，影響由口進食");
                btnFour.setText("危及生命，需要緊急處理");
                break;
            case "手足症候群":
                btnOne.setText("輕微皮膚發紅、水腫，但不會痛");
                btnTwo.setText("皮膚脫皮、水泡、出血、水腫，會痛，影響日常生活");
                btnThree.setText("嚴重皮膚脫皮、水泡、出血、水腫，非常痛，甚至影響行動能力");
                btnFour.setVisibility(View.INVISIBLE);
                break;
        }
    }

    private void setViews() {
        btnOne = findViewById(R.id.btnOne);
        btnTwo = findViewById(R.id.btnTwo);
        btnThree = findViewById(R.id.btnThree);
        btnFour = findViewById(R.id.btnFour);
    }
}
