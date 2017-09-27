package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.riskmanagement.MyApplication;
import com.riskmanagement.R;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.Login;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by codingWw on 2016/7/4.
 */
public class ModifyNicknameActivity extends BaseActivity implements View.OnClickListener{

    private EditText et_modify_nickname;
    private Button btn_modify_nickname;
    private String newNickName;
    String errString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_activity_modify_nickname);
        initView();
        initData();
    }

    private void initView() {
        initTop("修改昵称");
        setTitle("修改昵称");
        et_modify_nickname = (EditText) findViewById(R.id.et_modify_nickname);
        btn_modify_nickname = (Button)findViewById(R.id.btn_modify_nickname);
        btn_modify_nickname.setOnClickListener(this);
//        et_modify_nickname.setText(((GetUserInfo.Data) getIntent().getSerializableExtra("UserInfo")).getUserName());
    }

    private void initData() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_modify_nickname:

                newNickName = et_modify_nickname.getText().toString();
                if("".equals(newNickName)){
                    Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
                }else{
                    Log.e("ssh","aaaaa");
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
                    Toast.makeText(ModifyNicknameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("nickname",newNickName);
                    setResult(500,intent);
                    finish();
                    break;
                case 20:
                    Toast.makeText(ModifyNicknameActivity.this, errString, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    public Response<Login> responsea;
    private void updateData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", MyApplication.data);
        params.put("nickname",newNickName);
        Log.e("ssh",MyApplication.data);
        Call<Login> loginCall= HttpHelper.editNickname(params,new Callback<Login>() {

            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                responsea = response;
//                try{
                if ( response.body().getSuccess().equals("yes")){
                    mHandler.sendEmptyMessage(10);
                }else{
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




      /*  HashMap params = new HashMap();
        params.put("UserID", getIntent().getStringExtra("UserID"));
        params.put("RealName",newNickName);
        params.put("Sex", ((GetUserInfo.Data) getIntent().getSerializableExtra("UserInfo")).getSex());
        params.put("PhotoPath", ((GetUserInfo.Data) getIntent().getSerializableExtra("UserInfo")).getPhotoPath());

        showDialog();
        KsoapUtil ksoapUtil = new KsoapUtil("UpdateUser", params, new PostCallback() {
            @Override
            public void onSuccess(String result) {
                commProgressDialog.dismiss();
                UpdateUser updateUser = JSON.parseObject(result, UpdateUser.class);
                Log.i("UpdateUser", "UpdateUser" + updateUser);
                if(updateUser.isSuccess()) {
                    setResult(1, new Intent(ModifyNicknameActivity.this, PersonalDataActivity.class));
                    SPUtils.put(ModifyNicknameActivity.this, MyApplication.getUserRealName(),newNickName);
                    finish();
                } else {
                    ToastUtils.show(ModifyNicknameActivity.this, updateUser.getMessage(), 1);
                }
            }

            @Override
            public void onFail(Exception e) {
                commProgressDialog.dismiss();
                ToastUtils.show(ModifyNicknameActivity.this, "访问异常", 1);
                e.printStackTrace();
            }
        });
        ksoapUtil.execute();*/
    }

}
