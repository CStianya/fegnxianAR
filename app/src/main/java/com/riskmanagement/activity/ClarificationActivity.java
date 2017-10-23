package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.riskmanagement.R;

/**
 * Created by Administrator on 2017/6/4.
 */

public class ClarificationActivity extends BaseActivity {
    private Button btn_clarification_AR;

    private Button btn_form_next;

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
                openApp();
            }
        });

        btn_form_next = (Button) findViewById(R.id.btn_form1_next);
        btn_form_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ClarificationActivity.this, Form1Activity.class));
                finish();
            }
        });
    }

    // 打开另外一个APP
    private void openApp() {
        Intent intent = getPackageManager().getLaunchIntentForPackage("com.unity");
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "对不起，你尚未安装该应用！", Toast.LENGTH_LONG).show();
        }
    }

}
