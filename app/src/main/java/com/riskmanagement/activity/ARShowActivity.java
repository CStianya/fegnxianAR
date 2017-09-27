package com.riskmanagement.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.zhaoshuang.weixinrecordeddemo.SMainActivity;
import com.riskmanagement.R;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.UpProject;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Administrator on 2017/6/4.
 */

public class ARShowActivity extends BaseActivity {
    private Button btn_ARShow_submit;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String id;
    private EditText et_arShow_content;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_show);
        initView();
    }

    private void initView() {
        initTop("AR演示");
        sharedPreferences=this.getSharedPreferences("myData",MODE_PRIVATE);
        id=sharedPreferences.getString("id","");
        btn_ARShow_submit=(Button)findViewById(R.id.btn_ARShow_submit);
        et_arShow_content=(EditText)findViewById(R.id.et_arShow_content);
        btn_ARShow_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openApp();
                //submit();
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

    private void submit(){

        HashMap<String,String> params=new HashMap<>();
        params.put("id",id);
//        params.put("status","1");
//        params.put("arDesc",et_arShow_content.getText().toString());
        Call<UpProject> upProjectCall= HttpHelper.upProject(params, new Callback<UpProject>() {
            @Override
            public void onResponse(Call<UpProject> call, Response<UpProject> response) {
                try{
                    if("yes".equals(response.body().getSuccess())){
                        Log.e("ssh"," aac ="+response.body().getMessage());
                        handler.sendEmptyMessage(10);
                    }else{
                        Log.e("ssh"," com.riskmanagement.aa cc="+response.body().getMessage());
                        handler.sendEmptyMessage(20);
                    }
                }catch (Exception e){
                    Log.e("ssh"," com.riskmanagement.aa vvv="+response.body().getMessage());
                    handler.sendEmptyMessage(20);
                }
                Log.e("ssh"," com.riskmanagement.aa ="+response.body().getMessage());
            }

            @Override
            public void onFailure(Call<UpProject> call, Throwable t) {
                handler.sendEmptyMessage(20);
            }
        });
    }
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    Toast.makeText(ARShowActivity.this,"提交成功",Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(ARShowActivity.this, SMainActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case 20:
                    Toast.makeText(ARShowActivity.this,"网络访问异常",Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

}
