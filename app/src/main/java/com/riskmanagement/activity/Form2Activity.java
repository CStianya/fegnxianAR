package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.zhaoshuang.weixinrecordeddemo.SMainActivity;
import com.example.zhaoshuang.weixinrecordeddemo.StartActivity;
import com.example.zhaoshuang.weixinrecordeddemo.session.SessionKeeper;
import com.riskmanagement.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * Created by CS on 2017/10/23.
 */

public class Form2Activity extends BaseActivity {

    public Button btn_ARShow_next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_2);
        initTop("交底记录");
        initViews();
    }
    //初始化Views
    private void initViews(){
        btn_ARShow_next = (Button) findViewById(R.id.btn_ARShow_next);
        btn_ARShow_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionKeeper.setInstitution("9f28cacfb6e54c2798587b00bbbffaa5", getApplicationContext());
                SessionKeeper.setWarranty(createWarranty(), getApplicationContext());
                SessionKeeper.setUseName("admin", getApplicationContext());
                startActivity(new Intent(Form2Activity.this, SMainActivity.class));
                finish();
            }
        });
    }

    private String createWarranty() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        String time = formatter.format(curDate);
        Log.e("curTime", time);
        StringBuffer sb = new StringBuffer();
        sb.append(time);
        byte length = 5;
        String base = "0123456789";
        Random random = new Random();

        for(int i = 0; i < length; ++i) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();
    }

}
