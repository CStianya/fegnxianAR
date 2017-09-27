package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.riskmanagement.R;

/**
 * Created by Administrator on 2017/6/1.
 */

public class ProjectStatueActivity extends BaseActivity implements View.OnClickListener{
    private ImageView iv_project_preliminary;
    private ImageView iv_project_review;
    private ImageView iv_project_clarification;
    private int statu;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_statue);
        initTop("项目状态");

        Bundle bundle=getIntent().getExtras();
        statu=Integer.parseInt(bundle.getString("statu"));
        Log.e("ssh","statu  ==== "+statu);
        initView();
        initWidget();
    }

    private void initView() {
        iv_project_clarification=(ImageView)findViewById(R.id.iv_project_clarification);
        iv_project_preliminary=(ImageView)findViewById(R.id.iv_project_preliminary);
        iv_project_review=(ImageView)findViewById(R.id.iv_project_review);
    }
    private void initWidget(){
        iv_project_review.setOnClickListener(this);
        iv_project_preliminary.setOnClickListener(this);
        iv_project_clarification.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            //作业初勘
            case R.id.iv_project_preliminary:
                Intent intent = new Intent(this,PreliminaryActivity.class);
                intent.putExtra("statu",statu+"");
                startActivity(intent);
                finish();
                break;
            //作业前复查
            case R.id.iv_project_review:
                if(statu==1 || statu==2) {
                    startActivity(new Intent(this,ReviewWorkActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(ProjectStatueActivity.this,"请先完成作业初勘",Toast.LENGTH_LONG).show();
                }
                break;
            //作业交底
            case  R.id.iv_project_clarification:
                if(statu==2) {
                    startActivity(new Intent(this,ClarificationActivity.class));
                    finish();
                }
                else {
                    Toast.makeText(ProjectStatueActivity.this,"请先完成作业初勘和作业前复查",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }
}
