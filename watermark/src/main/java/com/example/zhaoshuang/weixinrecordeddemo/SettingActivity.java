package com.example.zhaoshuang.weixinrecordeddemo;

/**
 * Created by Administrator on 2017/3/23.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yixia.camera.util.Log;

/**
 * Created by Administrator on 2017/3/23.
 */

public class SettingActivity extends BaseActivity {
    private Button btn_ok;
    private RadioButton rb_640480;
    private RadioButton rb_800480;
    private RadioButton rb_1280720;
    private RadioButton rb_400;
    private RadioButton rb_600;
    private RadioButton rb_1M;
    private int height;
    private int width;
    private int ma;
    private int swidth;
    private int sma;
    private RadioGroup rg_fen;
    private RadioGroup rg_ma;
    private Bundle bundle;
    private EditText et_fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_setting);
        initView();
        initWidget();
    }

    private void initView() {
        btn_ok = (Button) findViewById(R.id.btn_ok);
        rb_640480 = (RadioButton) findViewById(R.id.rb_640480);
        rb_800480 = (RadioButton) findViewById(R.id.rb_800480);
        rb_1280720 = (RadioButton) findViewById(R.id.rb_1280720);
        rb_400 = (RadioButton) findViewById(R.id.rb_400);
        rb_600 = (RadioButton) findViewById(R.id.rb_600);
        rb_1M = (RadioButton) findViewById(R.id.rb_1M);
        rg_fen = (RadioGroup) findViewById(R.id.rg_fen);
        rg_ma = (RadioGroup) findViewById(R.id.rg_ma);
        et_fileName = (EditText) findViewById(R.id.et_name);
        bundle = new Bundle();
        setting();
    }

    private void initWidget() {
        rg_fen.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_640480){
                    width = 640;
                    height = 480;
                }else if(checkedId == R.id.rb_800480){
                    width = 800;
                    height = 480;
                }else if(checkedId == R.id.rb_1280720){
                    width = 1280;
                    height = 720;
                }
                bundle.putInt("height", height);
                bundle.putInt("width", width);
            }});
        rg_ma.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rb_400) {
                    ma = 400;
                }else if(checkedId == R.id.rb_600) {
                    ma = 600;
                }else if(checkedId == R.id.rb_1M){
                        ma = 1024;
                }
                bundle.putInt("ma", ma);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("sma", String.valueOf(bundle.getInt("ma")));
                Intent intent = new Intent();
                bundle.putString("fileName", String.valueOf(et_fileName.getText()));
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();

            }
        });
    }

    private void setting() {
        Intent intent = getIntent();
        Bundle sbundle = intent.getExtras();
        swidth = sbundle.getInt("swidth");
        sma = sbundle.getInt("sma");
        if (swidth == 640) {
            rb_640480.setChecked(true);
            height = 480;
        } else {
            if (swidth == 800) {
                rb_800480.setChecked(true);
                height = 480;
            } else if (swidth == 1280) {
                rb_1280720.setChecked(true);
                height = 720;
            }else {
                rb_1280720.setChecked(true);
                height = 720;
            }
        }
        if (sma == 400) rb_400.setChecked(true);
        else {
            if (sma == 600) rb_600.setChecked(true);
            else if (sma == 1024) rb_1M.setChecked(true);
        }
        bundle.putInt("ma", sma);
        bundle.putInt("height", height);
        bundle.putInt("width", swidth);
    }
}
