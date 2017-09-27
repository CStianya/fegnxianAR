package com.riskmanagement.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.riskmanagement.R;

/**
 * Created by Administrator on 2017/6/1.
 */

public class BaseActivity extends FragmentActivity {
    private ImageView iv_back;
    private TextView tv_title;
    public void initTop(String text) {
        iv_back=(ImageView)findViewById(R.id.iv_back);
        tv_title=(TextView)findViewById(R.id.tv_title);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        tv_title.setText(text);
    }

}
