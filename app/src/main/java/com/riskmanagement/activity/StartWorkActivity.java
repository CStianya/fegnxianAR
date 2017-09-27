package com.riskmanagement.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.riskmanagement.R;

/**
 * Created by Administrator on 2017/6/4.
 */

public class StartWorkActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_startwork);
        initView();
    }

    private void initView() {
        initTop("开始作业");
    }
}
