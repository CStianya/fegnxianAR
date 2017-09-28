package com.riskmanagement.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.riskmanagement.R;
import com.riskmanagement.adapter.WorkerAdapter;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.UserList;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.riskmanagement.MyApplication.mUserList;

/**
 * Created by Administrator on 2017/6/3.
 */

public class AddWorkerActivity extends BaseActivity implements AdapterView.OnItemSelectedListener{
    private ListView lv_addworker;
    private Button btn_addworker;
    private ArrayList<UserList.DataBean.ListBean> userList=new ArrayList<>();
    private ArrayList<UserList.DataBean.ListBean> myList=new ArrayList<>();
    private WorkerAdapter workerAdapter;

    @Override
    public void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addworker);
        initTop("添加");
        initView();
        initData();
    }

    private void initData() {
        Log.d("++++","in");
       Call<UserList> userListCall=HttpHelper.getUserList(new Callback<UserList>() {
           @Override
           public void onResponse(Call<UserList> call, Response<UserList> response) {
               userList.clear();
               workerAdapter.getList().clear();
               userList=(ArrayList<UserList.DataBean.ListBean>) response.body().getData().getList();
               workerAdapter.getList().addAll(userList);
               Log.d("++++",String.valueOf(workerAdapter.getList().size()));
               workerAdapter.notifyDataSetChanged();
           }

           @Override
           public void onFailure(Call<UserList> call, Throwable t) {
               Toast.makeText(AddWorkerActivity.this,"网络访问异常",Toast.LENGTH_LONG);
           }
       });
    }

    private void initView() {
        lv_addworker=(ListView)findViewById(R.id.lv_addworker);
        btn_addworker=(Button)findViewById(R.id.btn_addworker_submit);
        workerAdapter=new WorkerAdapter(userList,AddWorkerActivity.this);
        lv_addworker.setAdapter(workerAdapter);
        btn_addworker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < lv_addworker.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout) lv_addworker.getChildAt(i);// 获得子item的layout
                    CheckBox checkBox = (CheckBox) layout.findViewById(R.id.cb_worker_selected);// 从layout中获得控件,根据其id
                    if(checkBox.isChecked()){
                        boolean isExit = false;
                        for(int j = 0; j<mUserList.size();j++){
                            if(userList.get(i).getId().equals(mUserList.get(j).getId())){
                                isExit = true;
                                Toast.makeText(AddWorkerActivity.this,"已有该作业人员",Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                        if(!isExit || mUserList.size() == 0){
                            Toast.makeText(AddWorkerActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                            mUserList.add(userList.get(i));
                        }
                    }
                }
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
