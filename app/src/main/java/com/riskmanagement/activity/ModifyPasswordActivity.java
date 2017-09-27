package com.riskmanagement.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.riskmanagement.MyApplication;
import com.riskmanagement.R;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.Login;
import com.riskmanagement.utils.StringUtil;
import com.riskmanagement.utils.ToastUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by codingWw on 2016/7/4.
 */
public class ModifyPasswordActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_loginPassword_old;
    private EditText et_loginPassword_new;
    private EditText et_loginPassword_confirm;
    private Button btn_modify_password;


    private String passwordOld;
    private String passwordNew;
    private String passwordConfirm;
    private String errString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_activity_modify_password);
        initView();
        initEvent();
    }

    private void initEvent() {
        btn_modify_password.setOnClickListener(this);
    }

    private void initView() {
        initTop("修改密码");
        et_loginPassword_old = (EditText)findViewById(R.id.et_loginPassword_old);
        et_loginPassword_new = (EditText)findViewById(R.id.et_loginPassword_new);
        et_loginPassword_confirm = (EditText)findViewById(R.id.et_loginPassword_confirm);
        btn_modify_password = (Button)findViewById(R.id.btn_modify_password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_modify_password:
                passwordOld = et_loginPassword_old.getText().toString();
                passwordNew = et_loginPassword_new.getText().toString();
                passwordConfirm = et_loginPassword_confirm.getText().toString();

                if (StringUtil.isNullOrEmpty(passwordOld)) {
                    ToastUtils.show(ModifyPasswordActivity.this, "原始密码不能为空", 1);
                } else if (StringUtil.isNullOrEmpty(passwordNew)) {
                    ToastUtils.show(ModifyPasswordActivity.this, "新密码不能为空", 1);
                }
//                else if (!Boolean_Mobile.isPassWord(passwordNew)) {
//                    ToastUtils.show(ModifyPasswordActivity.this, "密码长度应为6~15个字符，请重新输入", 1);
//                }
                else if (StringUtil.isNullOrEmpty(passwordConfirm)) {
                    ToastUtils.show(ModifyPasswordActivity.this, "请再次输入密码", 1);
                } else if (!passwordNew.equals(passwordConfirm)) {
                    ToastUtils.show(ModifyPasswordActivity.this, "两次输入的新密码不一致，请重新输入", 1);
                } else {
                    updateData();
                }
                break;

            default:
                break;
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    Toast.makeText(ModifyPasswordActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    finish();
                    break;
                case 20:
                    Toast.makeText(ModifyPasswordActivity.this, errString, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void updateData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", MyApplication.data);
        params.put("oldPassword", passwordOld);
        params.put("newPassword1", passwordNew);
        params.put("newPassword2", passwordConfirm);

        Call<Login> loginCall = HttpHelper.editPassword(params, new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
//                try{
                if (response.body().getSuccess().equals("yes")) {
                    mHandler.sendEmptyMessage(10);
                } else {
                    errString = response.body().getMessage();
                    mHandler.sendEmptyMessage(20);
                }
//                }catch (Exception e){
//                    mHandler.sendEmptyMessage(20);
//                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                mHandler.sendEmptyMessage(20);
            }
        });
    }


    private void confirmOldPassword(String oldPassword) {

        /*HashMap<String, String> params = new HashMap<>();
        params.put("Mobile", (String) SPUtils.get(ModifyPasswordActivity.this, MyApplication.getUserMobile(), ""));
        params.put("Pwd", oldPassword);
        showDialog();
        KsoapUtil ksoapUtil = new KsoapUtil("Login", params, new PostCallback() {
            @Override
            public void onSuccess(String result) {
                commProgressDialog.dismiss();
                Login login = JSON.parseObject(result, Login.class);
                if (login.isSuccess()) {
                    resetPassWord();
                } else {
                    ToastUtils.show(ModifyPasswordActivity.this, "原密码输入错误，请重新输入", 1);
                }
            }
            @Override
            public void onFail(Exception e) {
                commProgressDialog.dismiss();
                ToastUtils.show(ModifyPasswordActivity.this, "网络异常", 1);
                e.printStackTrace();
            }
        });
        ksoapUtil.execute();*/
    }

    private void resetPassWord() {
       /* HashMap<String, String> params = new HashMap<>();
        params.put("Mobile", (String) SPUtils.get(ModifyPasswordActivity.this, MyApplication.getUserMobile(), ""));
        params.put("Pwd",passwordNew);
        params.put("NewPwd", passwordConfirm);
        showDialog();
        KsoapUtil ksoapUtil = new KsoapUtil("ReSetPwd", params, new PostCallback() {
            @Override
            public void onSuccess(String result) {
                commProgressDialog.dismiss();
                JsonModel jsonModel = JSON.parseObject(result, JsonModel.class);
                Log.i("JsonModel", "jsonModel" + jsonModel);
                if (jsonModel.isSuccess()) {
                    //startActivity(new Intent(ModifyPasswordActivity.this, PersonalDataActivity.class));
                    //用户信息存到本地
                    SPUtils.put(ModifyPasswordActivity.this, MyApplication.getUserPassword(),et_loginPassword_new.getText().toString() );
                    ToastUtils.show(ModifyPasswordActivity.this, "修改成功", 1);
                    finish();

                } else {
                    ToastUtils.show(ModifyPasswordActivity.this, jsonModel.getMessage(), 1);
                }
            }

            @Override
            public void onFail(Exception e) {
                commProgressDialog.dismiss();
                ToastUtils.show(ModifyPasswordActivity.this, "访问异常", 1);
                e.printStackTrace();
            }
        });
        ksoapUtil.execute();*/
    }

}
