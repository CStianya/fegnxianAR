package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.riskmanagement.R;

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
        initDatas();
    }
    //初始化Views
    private void initViews(){
        btn_ARShow_next = (Button) findViewById(R.id.btn_ARShow_next);
        btn_ARShow_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Form2Activity.this, ARShowActivity.class));
                finish();
            }
        });
    }
    //初始化数据
    private void initDatas() {

    }
}
