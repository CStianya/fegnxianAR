package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.riskmanagement.MyApplication;
import com.riskmanagement.R;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.Login;
import com.riskmanagement.utils.ClearEditText;
import com.riskmanagement.utils.ToastUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by xiaozhi on 2016/3/10.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private ClearEditText login_username = null;
    private ClearEditText login_password = null;
    private Button i_login_bt = null;
    private TextView i_register_tv = null;
    private TextView i_forget_password = null;

    private Intent intent = null;
    private String userName;
    private String userPassword;
    private String errString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
//        back = getIntent().getStringExtra("state");
//        ToastUtils.showShort(this, "请先登入！");
        initview();
        initWidget();
    }

    private void initWidget() {
        i_login_bt.setOnClickListener(this);
        i_register_tv.setOnClickListener(this);
        i_forget_password.setOnClickListener(this);
//        SharedPreferences preferences = getSharedPreferences("loginInfo", MODE_PRIVATE);

//        name = preferences.getString("username", "");
//        password = preferences.getString("password", "");
//        if (!name.equals("")){
//            login_username.setText(name);
//            login_password.setText(password);
//        }


//        ll_topBack.setOnClickListener(this);
//        login_username.setOnClickListener(this);
//        login_password.setOnClickListener(this);

    }

    private void initview() {
        initTop("用户登录");
//        login_username = (EditText) findViewById(R.id.login_username);
//        login_password = (EditText) findViewById(R.id.login_password);
        login_username = (ClearEditText) findViewById(R.id.login_username);
        login_password = (ClearEditText) findViewById(R.id.login_password);
        i_login_bt = (Button) findViewById(R.id.i_login_bt);
        i_register_tv = (TextView) findViewById(R.id.i_register_tv);
        i_forget_password = (TextView) findViewById(R.id.i_forget_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.i_register_tv:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

                break;
            case R.id.i_login_bt:
                userName = login_username.getText().toString();
                userPassword = login_password.getText().toString();
                if (TextUtils.isEmpty(userName)) {
                    ToastUtils.show(LoginActivity.this, "手机号不能为空", 1);
                } else if (TextUtils.isEmpty(userPassword)) {
                    ToastUtils.show(LoginActivity.this, "密码不能为空", 1);
                }else {
                    loginIn(userName, userPassword);
                }
                break;

            case R.id.i_forget_password:
                startActivityForResult(new Intent(LoginActivity.this, ForgetActivity.class), 0);
                break;

            default:
                break;
        }
    }

    private void loginIn(final String userName, final String passWord) {
        if (!TextUtils.isEmpty(userName) && !TextUtils.isEmpty(passWord)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("username", userName);
            params.put("password", passWord);
            Call<Login> loginCall= HttpHelper.login(params, new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {

                    try{
                        if (response.body()!=null && response.body().getSuccess().equals("yes")){
                            MyApplication.data = response.body().getData();
                            handler.sendEmptyMessage(10);
                        }else{
                            errString = response.body().getMessage();
                            handler.sendEmptyMessage(20);
                        }
                    }catch (Exception e){
                        handler.sendEmptyMessage(20);
                    }
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    ToastUtils.show(LoginActivity.this,"网络访问异常", Toast.LENGTH_LONG);
                }
            });
        }
    }

    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {

            switch (msg.what){
                case 10:
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    break;
                case 20:
                    Toast.makeText(LoginActivity.this,errString,Toast.LENGTH_LONG).show();
                    break;
            }


        }
    };


    public boolean isEmpty(String str) {
        if (str == null || str.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
        }
        return false;
    }


}
