package com.riskmanagement.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.riskmanagement.R;

/**
 * Created by Administrator on 2017/6/4.
 */

public class SuggestionActivity extends BaseActivity {
    private EditText et_suggestion_content;
    private Button btn_suggestion_submit;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion);
        initView();
    }

    private void initView() {
        initTop("意见反馈");
        et_suggestion_content=(EditText)findViewById(R.id.et_suggestion_content);
        btn_suggestion_submit=(Button)findViewById(R.id.btn_suggestion_submit);
        btn_suggestion_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
