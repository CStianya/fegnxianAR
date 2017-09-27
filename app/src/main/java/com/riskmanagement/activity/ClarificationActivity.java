package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.riskmanagement.R;

/**
 * Created by Administrator on 2017/6/4.
 */

public class ClarificationActivity extends BaseActivity {
    private Button btn_clarification_AR;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clarification);
        initView();
    }

    private void initView() {
        initTop("作业交底");
        btn_clarification_AR=(Button)findViewById(R.id.btn_clarification_AR);
        btn_clarification_AR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ClarificationActivity.this,ARShowActivity.class));
                finish();
            }
        });
    }
}
