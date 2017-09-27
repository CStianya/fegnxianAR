package com.riskmanagement.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.riskmanagement.R;
import com.riskmanagement.activity.PersonalDataActivity;
import com.riskmanagement.activity.SuggestionActivity;

/**
 * Created by codingWw on 2016/3/7.
 */

public class MyAccountFragment extends Fragment implements View.OnClickListener {

    public final static int INDEXT = 2;
    private final static int TO_LOGIN = 0;
    private final static int TO_OTHERS = 1;


    private LinearLayout i_myaccount_personal_ll;
    private LinearLayout ll_moreServices;



    //用户头像、姓名
    private ImageView iv_myAccount_userImage;
    private TextView tv_myAccount_userName;

    //设置
    private ImageView iv_myAccount_setting;

    private Class targetClass = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.i_fragment_myaccount, container, false);
        initView(view);
        initEvent();
        return view;
    }

  /*  @Override
    public void onStart() {
        super.onStart();
        Log.e("@@@@","onstart");
        String imagePath = (String) SPUtils.get(getActivity(), MyApplication.getUserHeadImagePath(), "");
        String userName = (String) SPUtils.get(getActivity(), MyApplication.getUserRealName(), "");
       // String userId = (String) SPUtils.get(getActivity(), MyApplication.getUserId(), "");

        if (isLogin()) {
            if (isAgent()) {
                ll_myAccount_agent_view.setVisibility(View.VISIBLE);
            }
            Glide.with(getActivity())
                    .load(imagePath)
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.icon_user_defalut_avatar)
                    .into(iv_myAccount_userImage);
            tv_myAccount_userName.setText(userName);
        }
    }*/


    private void initView(View view) {

        iv_myAccount_userImage = (ImageView) view.findViewById(R.id.iv_myAccount_userImage);
        tv_myAccount_userName = (TextView) view.findViewById(R.id.tv_myAccount_userName);
        i_myaccount_personal_ll = (LinearLayout) view.findViewById(R.id.i_myaccount_personal_ll);
        ll_moreServices = (LinearLayout) view.findViewById(R.id.ll_moreServices);
    }

    private void initEvent() {
        iv_myAccount_userImage.setOnClickListener(this);
        tv_myAccount_userName.setOnClickListener(this);
        i_myaccount_personal_ll.setOnClickListener(this);
        ll_moreServices.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.tv_myAccount_userName:
               /* needLoginIntent(MyAccountActivity.this, PersonalDataActivity.class);*/
                break;
            case R.id.iv_personalData_head:
                /*needLoginIntent(MyAccountActivity.this, PersonalDataActivity.class);*/
                break;
            case R.id.i_myaccount_personal_ll:
                startActivity(new Intent(getActivity(),PersonalDataActivity.class));
               /* needLoginIntent(MyAccountActivity.this, PersonalDataActivity.class);*/
                break;
            case R.id.ll_moreServices:
                startActivity(new Intent(getActivity(),SuggestionActivity.class));
               /* needLoginIntent(MyAccountActivity.this, SuggestionActivity.class);*/
                break;
            case R.id.iv_myAccount_userImage:
                /*needLoginIntent(MyAccountActivity.this, PersonalDataActivity.class);*/
                break;
            default:
                break;
        }
    }


   /* *//**
     * 判断用户是否已经登录
     *//*
    private boolean isLogin() {
        return ((boolean) SPUtils.get(getActivity(), MyApplication.getUserLogin(), false));
    }*/



    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String imagePath = (String) SPUtils.get(getActivity(), MyApplication.getUserHeadImagePath(), "");
        String userName = (String) SPUtils.get(getActivity(), MyApplication.getUserNickname(), "");
        if (isLogin()) {
            if (isAgent()) {
                ll_myAccount_agent_view.setVisibility(View.VISIBLE);
            }
            Glide.with(getActivity())
                    .load(imagePath)
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.icon_user_defalut_avatar)
                    .into(iv_myAccount_userImage);
            tv_myAccount_userName.setText(userName);
        } else {
            iv_myAccount_userImage.setImageResource(R.mipmap.icon_user_defalut_avatar);
            tv_myAccount_userName.setText("立即登录");
            ll_myAccount_agent_view.setVisibility(View.GONE);
        }*/

//        if (requestCode == TO_LOGIN) {
//            if (resultCode == MyApplication.getLoginReturn()) {
//                toTargetClass(getActivity(), targetClass);
//            }
//        }

    }

   /* public void needLoginIntent(Activity activity, Class clas) {
        targetClass = clas;
        if (isLogin()) {
            toTargetClass(activity, clas);
        } else {
            startActivityForResult(new Intent(activity, LoginActivity.class), TO_LOGIN);
        }
    }*/

   /* public void toTargetClass(Activity activity, Class clas) {
        String userID = (String) SPUtils.get(getActivity(), MyApplication.getUserId(), "");
        Intent intent = new Intent();
        intent.setClass(activity, clas);
        Bundle bundle = new Bundle();
        bundle.putString("UserID", userID);
        intent.putExtras(bundle);
        startActivityForResult(intent, TO_OTHERS);
    }*/


