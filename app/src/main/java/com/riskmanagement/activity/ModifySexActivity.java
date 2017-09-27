package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
public class ModifySexActivity extends BaseActivity implements View.OnClickListener{

    private RadioGroup rg_modify_sex;
    private RadioButton rb_modify_man;
    private RadioButton rb_modify_woman;
    private Button btn_modify_sex;

    //性别 0男 1女
    private String sex = "0";
    String errString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_activity_modify_sex);
        initView();
    }

    private void initView() {
        initTop("性别修改");
        rg_modify_sex = (RadioGroup)findViewById(R.id.rg_modify_sex);
        rb_modify_man = (RadioButton)findViewById(R.id.rb_modify_man);
        rb_modify_woman = (RadioButton)findViewById(R.id.rb_modify_woman);
        btn_modify_sex = (Button)findViewById(R.id.btn_modify_sex);
        rb_modify_man.setOnClickListener(this);
        rb_modify_woman.setOnClickListener(this);
        btn_modify_sex.setOnClickListener(this);
        rb_modify_man.setChecked(true);

        /*if(!StringUtil.isNullOrEmpty(((GetUserInfo.Data) getIntent().getSerializableExtra("UserInfo")).getSex())) {
            if(((GetUserInfo.Data) getIntent().getSerializableExtra("UserInfo")).getSex().equals("女")) {
                rb_modify_woman.setChecked(true);
            } else {
                rb_modify_man.setChecked(true);
            }
        }*/


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rb_modify_man:
                rb_modify_man.setChecked(true);
                break;
            case R.id.rb_modify_woman:
                rb_modify_woman.setChecked(true);
                break;
            case R.id.btn_modify_sex:

                if(rb_modify_man.isChecked()) {
                    sex = "0";
                } else if(rb_modify_woman.isChecked()) {
                    sex = "1";
                }
                updateData();
            default:
                break;
        }
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    Toast.makeText(ModifySexActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent();
                    intent.putExtra("sex",sex);
                    setResult(501,intent);
                    finish();
                    break;
                case 20:
                    Toast.makeText(ModifySexActivity.this, errString, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    private void updateData(){
        HashMap<String, String> params = new HashMap<>();
        params.put("userId", MyApplication.data);
        params.put("sex",sex);

        Call<Login> loginCall= HttpHelper.editSex(params,new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
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
    }



   /* private void updateData() {
        HashMap params = new HashMap();
        params.put("UserID",(String)getIntent().getStringExtra("UserID"));
        params.put("RealName",((GetUserInfo.Data)getIntent().getSerializableExtra("UserInfo")).getRealName());
        params.put("Sex", sex);
        params.put("PhotoPath", ((GetUserInfo.Data) getIntent().getSerializableExtra("UserInfo")).getPhotoPath());

        showDialog();
        KsoapUtil ksoapUtil = new KsoapUtil("UpdateUser", params, new PostCallback() {
            @Override
            public void onSuccess(String result) {

                commProgressDialog.dismiss();
                UpdateUser updateUser = JSON.parseObject(result, UpdateUser.class);
                Log.i("UpdateUser", "UpdateUser" + updateUser);
                if(updateUser.isSuccess()) {
                    setResult(2, new Intent(ModifySexActivity.this, PersonalDataActivity.class));
                    finish();
                } else {
                    ToastUtils.show(ModifySexActivity.this, updateUser.getMessage(), 1);
                }
            }

            @Override
            public void onFail(Exception e) {
                commProgressDialog.dismiss();
                ToastUtils.show(ModifySexActivity.this, "访问异常", 1);
                e.printStackTrace();
            }
        });
        ksoapUtil.execute();
    }*/

//    private void showDialog() {
//        if (commProgressDialog == null) {
//            commProgressDialog = CommProgressDialog.createDialog(ModifySexActivity.this, R.anim.my_anim);
//        }
//        commProgressDialog.setCanceledOnTouchOutside(false);
//        commProgressDialog.setMessage("加载中...");
//        commProgressDialog.show();
//    }
}
