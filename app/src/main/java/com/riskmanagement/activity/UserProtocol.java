package com.riskmanagement.activity;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;

import com.riskmanagement.R;

/**
 * Created by Administrator on 2017/6/5.
 */

public class UserProtocol extends BaseActivity{
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userprotocol);
        initTop("XX用户协议");
    }
}
