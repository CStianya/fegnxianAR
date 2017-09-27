package com.riskmanagement.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.riskmanagement.MyApplication;
import com.riskmanagement.R;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.UserMessage;
import com.riskmanagement.utils.ToastUtils;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by codingWw on 2016/7/4.
 */
public class PersonalDataActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_personalData_headImage;
    private ImageView iv_personalData_head;
    private ImageView iv_personalData_nickname;
    private TextView tv_personalData_nickname;
    private ImageView iv_personalData_sex;
    private TextView tv_personalData_sex;
    private TextView tv_personalData_account;
    private ImageView iv_personalData_modify;
    private Button i_exit_bt;

    private LinearLayout ll_personalData_headImage;
    private LinearLayout ll_personalData_nickname;
    private LinearLayout ll_personalData_sex;
    private LinearLayout ll_personalData_modify;

    //获取用户信息
    private String userId = "";
    private Intent intent = new Intent();
    private Bundle bundle = new Bundle();

    private final static int PDA_INDEX = 0;

    String errString = ""; // 错误信息
    String phoneNo = "";
    String sex = "";
    String headImg = "";
    String nickname  = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.i_activty_personal_data);
        /*userId = (String) SPUtils.get(PersonalDataActivity.this, MyApplication.getUserId(), "");*/
        initView();
        initEvent();
        loadData();
    }

    private void initView() {
        initTop("我的账户");
        getMessge();
        iv_personalData_headImage = (ImageView) findViewById(R.id.iv_personalData_headImage);
        iv_personalData_head = (ImageView) findViewById(R.id.iv_personalData_head);
        iv_personalData_nickname = (ImageView) findViewById(R.id.iv_personalData_nickname);
        iv_personalData_sex = (ImageView) findViewById(R.id.iv_personalData_sex);
        iv_personalData_modify = (ImageView) findViewById(R.id.iv_personalData_modify);
        tv_personalData_nickname = (TextView) findViewById(R.id.tv_personalData_nickname);
        tv_personalData_sex = (TextView) findViewById(R.id.tv_personalData_sex);
        tv_personalData_account = (TextView) findViewById(R.id.tv_personalData_account);

        ll_personalData_headImage = (LinearLayout)findViewById(R.id.ll_personalData_headImage);
        ll_personalData_nickname = (LinearLayout)findViewById(R.id.ll_personalData_nickname);
        ll_personalData_sex = (LinearLayout)findViewById(R.id.ll_personalData_sex);
        ll_personalData_modify = (LinearLayout)findViewById(R.id.ll_personalData_modify);
    }


    public void getMessge(){

        HashMap<String, String> params = new HashMap<>();
        params.put("userId", MyApplication.data);

        Call<UserMessage> loginCall= HttpHelper.getUserMessage(params,new Callback<UserMessage>() {
            @Override
            public void onResponse(Call<UserMessage> call, Response<UserMessage> response) {
//                try{
                    if ( response.body().getSuccess().equals("yes")){

                        phoneNo = response.body().getData().getPhoneNo();
                        sex = response.body().getData().getSex();
                        headImg = response.body().getData().getHeadImg();
                        nickname = response.body().getData().getNickname();

                        Log.e("ssh",phoneNo);
                        Log.e("ssh",sex);
                        Log.e("ssh",headImg);
                        Log.e("ssh",nickname);
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
            public void onFailure(Call<UserMessage> call, Throwable t) {
                mHandler.sendEmptyMessage(20);
            }
        });
    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    if(!"".equals(nickname)){
                        tv_personalData_nickname.setText(nickname);
                    }
                    if("0".equals(sex)){
                        tv_personalData_sex.setText("男");
                    }else if("1".equals(sex)){
                        tv_personalData_sex.setText("女");
                    }
                    if(!"".equals(phoneNo)){
                        tv_personalData_account.setText(phoneNo);
                    }
                    if(!"".equals(headImg)){
                        Glide.with(PersonalDataActivity.this)
                                .load(MyApplication.domainName+headImg)
                                .centerCrop()
                                .crossFade()
                                .error(R.mipmap.ic_launcher)
                                .into(iv_personalData_headImage);
                    }
                    break;
                case 20:
                    if("".equals(errString)){
                        ToastUtils.show(PersonalDataActivity.this,"网络异常,获取信息失败", Toast.LENGTH_LONG);
                    }else{
                        ToastUtils.show(PersonalDataActivity.this,errString, Toast.LENGTH_LONG);
                    }
                    break;
            }
        }
    };


    private void initEvent() {
        ll_personalData_headImage.setOnClickListener(this);
        ll_personalData_nickname.setOnClickListener(this);
        ll_personalData_sex.setOnClickListener(this);
        ll_personalData_modify.setOnClickListener(this);

    }

    private void loadData() {
        HashMap<String, String> params = new HashMap<>();
        params.put("UserID", userId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id. ll_personalData_headImage:
                intent.setClass(PersonalDataActivity.this, ModifyHeadImageActivity.class);
                startActivityForResult(intent, PDA_INDEX);
                break;
            case R.id.ll_personalData_nickname:
                intent.setClass(PersonalDataActivity.this, ModifyNicknameActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, PDA_INDEX);
                break;
            case R.id.ll_personalData_sex:
                intent.setClass(PersonalDataActivity.this, ModifySexActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, PDA_INDEX);
                break;
            case R.id.ll_personalData_modify:
                intent.setClass(PersonalDataActivity.this, ModifyPasswordActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, PDA_INDEX);
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case 500:
                tv_personalData_nickname.setText(data.getStringExtra("nickname"));
                break;
            case 501:
                if("0".equals(data.getStringExtra("sex"))){
                    tv_personalData_sex.setText("男");
                }else if("1".equals(data.getStringExtra("sex"))){
                    tv_personalData_sex.setText("女");
                }
                Log.e("ssh","返回的性别 = "+data.getStringExtra("sex"));
                break;
            case 503:
                Glide.with(PersonalDataActivity.this)
                        .load(data.getStringExtra("HeadImage"))
                        .centerCrop()
                        .crossFade()
                        .error(R.mipmap.ic_launcher)
                        .into(iv_personalData_headImage);
        }


        super.onActivityResult(requestCode, resultCode, data);
    }

    /*private void exitAlert() {
        LayoutInflater inflater = LayoutInflater.from(this);
        RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.i_view_exit_alertdialog, null);
        final Dialog exitDialog = new AlertDialog.Builder(PersonalDataActivity.this).create();
        exitDialog.setCancelable(false);
        exitDialog.show();
        exitDialog.getWindow().setContentView(layout);
        WindowManager.LayoutParams params = exitDialog.getWindow().getAttributes();
        params.width = (int)(getScreenWidth() * 0.8);
        exitDialog.getWindow().setAttributes(params);
        Button btn_exit_alert_positive = (Button) layout.findViewById(R.id.btn_exit_alert_positive);
        Button btn_exit_alert_negative = (Button) layout.findViewById(R.id.btn_exit_alert_negative);
        btn_exit_alert_positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAllData();
                finish();
            }
        });
        btn_exit_alert_negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                exitDialog.dismiss();
            }
        });

    }
*/
   /* *//**
     * 清空所有本地数据
     *//*
    private void clearAllData() {
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserLogin(), false);
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserId(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserZhongJieID(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserStoreID(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserHeadImagePath(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserNickname(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserRealName(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserSex(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserMobile(), "");
        SPUtils.put(PersonalDataActivity.this, MyApplication.getUserPassword(), "");
    }
*/

}
