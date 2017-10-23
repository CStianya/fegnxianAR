package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.riskmanagement.R;

/**
 * Created by CS on 2017/10/23.
 */

public class Form1Activity extends BaseActivity {

    private Button btn_form2_next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form_1);
        initTop("跨越高速公路作业B票");
        initViews();
        initDatas();
    }
    //初始化Views
    private void initViews(){
        btn_form2_next = (Button) findViewById(R.id.btn_form2_next);
        btn_form2_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Form1Activity.this, Form2Activity.class));
                finish();
            }
        });
    }
    //初始化数据
    private void initDatas() {

    }

}
