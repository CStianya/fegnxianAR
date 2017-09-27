package com.riskmanagement.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.riskmanagement.R;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.ForgetPassword;
import com.riskmanagement.utils.Boolean_Mobile;
import com.riskmanagement.utils.ClearEditText;
import com.riskmanagement.utils.StringUtil;
import com.riskmanagement.utils.ToastUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.jmhz.ailou.ilou.model.JsonModel;
//import com.jmhz.ailou.ilou.widget.Boolean_Mobile;


/**
 * Created by xiaozhi on 2016/3/10.
 */
public class ForgetActivity extends BaseActivity implements View.OnClickListener {
    private ClearEditText forget_phonenumber = null;
    private ClearEditText forget_phonecode = null;
    private ClearEditText forget_password = null;
    private ClearEditText forget_repassword = null;
    private Button btn_forgetPassword_code;
    private Button btn_forgetPassword_submit;


    private String phoneNumber;
    private String phoneNumberLatest;//输入框最后一次的电话号码
    private String verificationCode;
    private String verificationCodeReceive;
    private String newPassword;
    private String newPasswordConfirm;
    private TimeCount time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        initview();
        initEvent();
    }

    private void initEvent() {
        btn_forgetPassword_code.setOnClickListener(this);
        btn_forgetPassword_submit.setOnClickListener(this);
    }

    private void initview() {
        initTop("忘记密码");
        forget_phonenumber = (ClearEditText) findViewById(R.id.forget_phonenumber);
        forget_phonecode = (ClearEditText) findViewById(R.id.forget_phonecode);
        forget_password = (ClearEditText) findViewById(R.id.forget_password);
        forget_repassword = (ClearEditText) findViewById(R.id.forget_repassword);
        btn_forgetPassword_code = (Button) findViewById(R.id.btn_forgetPassword_code);
        btn_forgetPassword_submit = (Button) findViewById(R.id.btn_forgetPassword_submit);
        time = new TimeCount(60000, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_forgetPassword_code:
                //获取本机号码并下发验证码！
                phoneNumber = forget_phonenumber.getText().toString();
                if (StringUtil.isEmpty(phoneNumber)) {
                    ToastUtils.show(ForgetActivity.this, "请输入手机号", 1);
                } else if(Boolean_Mobile.isMobileNO(phoneNumber)){
                    time.start();// 开始计时
                    sendVerificationCode();
                } else {
                    ToastUtils.show(ForgetActivity.this, "请重新手机号", 1);
                }
                break;
            case R.id.btn_forgetPassword_submit:
                forgetPassword();
                break;
            default:
                break;
        }
    }

    private void forgetPassword() {
        phoneNumberLatest = forget_phonenumber.getText().toString();
        /*verificationCode = forget_phonecode.getText().toString();*/
        verificationCode = "123456";
        newPassword = forget_password.getText().toString();
        newPasswordConfirm = forget_repassword.getText().toString();

        if (StringUtil.isEmpty(phoneNumberLatest)) {
            ToastUtils.show(ForgetActivity.this, "手机号不能为空", 1);
        } else if (StringUtil.isNullOrEmpty(verificationCode)) {
            ToastUtils.show(ForgetActivity.this, "验证码不能为空", 1);
        } else if (!Boolean_Mobile.isMobileNO(phoneNumberLatest)) {
            ToastUtils.show(ForgetActivity.this, "请输入正确的手机号", 1);
        } /*else if (!verificationCode.equals(verificationCodeReceive) || !phoneNumber.equals(phoneNumberLatest)) {
            ToastUtils.show(ForgetActivity.this, "验证码不正确", 1);
        } */else if (StringUtil.isNullOrEmpty(newPassword)) {
            ToastUtils.show(ForgetActivity.this, "密码不能为空", 1);
        } else if (!Boolean_Mobile.isPassWord(newPassword)) {
            ToastUtils.show(ForgetActivity.this, "密码长度应为6~15个字符，请重新输入", 1);
        } else if (StringUtil.isNullOrEmpty(newPasswordConfirm)) {
            ToastUtils.show(ForgetActivity.this, "请再次输入密码", 1);
        } else if (!newPassword.equals(newPasswordConfirm)) {
            ToastUtils.show(ForgetActivity.this, "两次输入的密码不一致，请重新输入", 1);
        } else {
            HashMap<String, String> params = new HashMap<>();
            params.put("phoneNO", phoneNumberLatest);
            params.put("codeMes", verificationCode);
            params.put("password", newPassword);
            params.put("rPassword", newPasswordConfirm);
            Call<ForgetPassword> forgetPasswordCall= HttpHelper.forgetPassword(params, new Callback<ForgetPassword>() {
                @Override
                public void onResponse(Call<ForgetPassword> call, Response<ForgetPassword> response) {
                    if(response.body().getSuccess().equals("yes")){
                        ToastUtils.show(ForgetActivity.this,"修改密码成功", Toast.LENGTH_LONG);
                        finish();
                    }else{
                        ToastUtils.show(ForgetActivity.this,response.body().getMessage(), Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<ForgetPassword> call, Throwable t) {
                    ToastUtils.show(ForgetActivity.this,"网络访问异常", Toast.LENGTH_LONG);
                }
            });
        }
    }

    /**
     * 获取验证码
     */
    private void sendVerificationCode() {


    }

    /**
     * 计时器
     */
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onFinish() {// 计时完毕
            btn_forgetPassword_code.setText("发送验证码");
            btn_forgetPassword_code.setClickable(true);
        }

        @Override
        public void onTick(long millisUntilFinished) {// 计时过程
            btn_forgetPassword_code.setClickable(false);//防止重复点击
            btn_forgetPassword_code.setText(millisUntilFinished / 1000 + "s后可重新获取");
        }
    }


}
