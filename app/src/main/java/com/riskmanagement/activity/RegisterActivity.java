package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.riskmanagement.R;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.Register;
import com.riskmanagement.utils.Boolean_Mobile;
import com.riskmanagement.utils.ClearEditText;
import com.riskmanagement.utils.Timer;
import com.riskmanagement.utils.ToastUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.jmhz.ailou.ilou.model.JsonModel;
//import com.jmhz.ailou.ilou.widget.Boolean_Mobile;

/**
 * Created by xiaozhi on 2016/3/9.
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private ClearEditText et_register_username = null;
    private ClearEditText et_register_phone_num = null;
    private ClearEditText et_register_password = null;
    private ClearEditText et_register_code = null;

    private Button btn_register_button;
    private Button btn_register_sendVerificationCode;
    private String userName;
    private String passWord;
    private String phoneNumber;
    private String phoneNumberLatest;
    private String verificationCode;
    private String verificationCodeReceive;
    private Timer time;
    private TextView tv_register_protocol;

    private Response<Register> response;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initView();
        initEvent();
    }

    private void initView() {

        initTop("注册");
        et_register_username = (ClearEditText) findViewById(R.id.et_register_username);
        et_register_phone_num = (ClearEditText) findViewById(R.id.et_register_phone_num);
        et_register_password = (ClearEditText) findViewById(R.id.et_register_password);
        et_register_code = (ClearEditText) findViewById(R.id.et_register_code);

        btn_register_button = (Button) findViewById(R.id.btn_register_button);
        btn_register_sendVerificationCode = (Button) findViewById(R.id.btn_register_sendVerificationCode);
        tv_register_protocol = (TextView) findViewById(R.id.tv_register_protocol);
        tv_register_protocol.setOnClickListener(this);
        time = new Timer(60000, 1000,btn_register_sendVerificationCode);
    }

    private void initEvent() {
        btn_register_button.setOnClickListener(this);
        btn_register_sendVerificationCode.setOnClickListener(this);
    }

    private void sendVerificationCode() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register_sendVerificationCode:
                //获取本机号码并下发验证码！
                phoneNumber = et_register_phone_num.getText().toString();
                if( Boolean_Mobile.isMobileNO(phoneNumber)){
                    time.start();// 开始计时
                    sendVerificationCode();
                } else {
                    ToastUtils.show(RegisterActivity.this, "请重新输入手机号", 1);
                }
                break;
            case R.id.btn_register_button:
                register();
                break;
            case R.id.tv_register_protocol:
                startActivity(new Intent(RegisterActivity.this,UserProtocol.class));
                break;
            default:
                break;

        }
    }

    private void register() {
        userName = et_register_username.getText().toString();
        phoneNumberLatest = et_register_phone_num.getText().toString();
        passWord = et_register_password.getText().toString();
        verificationCode = et_register_code.getText().toString();
        verificationCode="123456";
       /* if (StringUtil.isNullOrEmpty(userName)) {
            ToastUtils.show(RegisterActivity.this, "用户名不能为空", 1);
        } else if (userName.length() > 20) {
            ToastUtils.show(RegisterActivity.this, "用户名最多不能超过20个字符", 1);
        } else if (StringUtil.isNullOrEmpty(phoneNumberLatest)) {
            ToastUtils.show(RegisterActivity.this, "手机号不能为空", 1);
        } else if (StringUtil.isNullOrEmpty(verificationCode)) {
            ToastUtils.show(RegisterActivity.this, "验证码不能为空", 1);
        } else if (!Boolean_Mobile.isMobileNO(phoneNumberLatest)) {
            ToastUtils.show(RegisterActivity.this, "请输入正确的手机号", 1);
        } *//*else if (!verificationCode.equals(verificationCodeReceive) || !phoneNumber.equals(phoneNumberLatest)) {
            ToastUtils.show(RegisterActivity.this, "验证码不正确", 1);
        }*//* else if (StringUtil.isNullOrEmpty(passWord)) {
            ToastUtils.show(RegisterActivity.this, "密码不能为空", 1);
        } else if (!Boolean_Mobile.isPassWord(passWord)) {
            ToastUtils.show(RegisterActivity.this, "密码长度应为6~15个字符，请重新输入", 1);
        } else {*/
            HashMap<String,String> params=new HashMap<>();
            params.put("userName",userName);
            params.put("password",passWord);
            params.put("phoneNO",phoneNumberLatest);
            params.put("codeMes",verificationCode);
            Call<Register> registerCall= HttpHelper.register(params, new Callback<Register>() {
                @Override
                public void onResponse(Call<Register> call, Response<Register> response) {
                    Log.e("ssh",response.toString());
                    if(response!=null &&response.body().getSuccess().equals("yes")){
                        mHandler.sendEmptyMessage(10);
                        Log.e("ssh","aaa=="+response.toString());
                    }else{
                        try{
                            RegisterActivity.this.response = response;
                            mHandler.sendEmptyMessage(20);
                        }catch (Exception e){mHandler.sendEmptyMessage(20);}

                    }

                }
                @Override
                public void onFailure(Call<Register> call, Throwable t) {
                    mHandler.sendEmptyMessage(30);
                }
            });
        /*}*/

    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    ToastUtils.show(RegisterActivity.this,"注册成功", Toast.LENGTH_LONG);
                    finish();
                    break;
                case 20:
                    ToastUtils.show(RegisterActivity.this,response.body().getMessage(), Toast.LENGTH_LONG);
                    break;
                case 30:
                    ToastUtils.show(RegisterActivity.this,"网络访问异常", Toast.LENGTH_LONG);
                    break;
            }
        }
    };


}
