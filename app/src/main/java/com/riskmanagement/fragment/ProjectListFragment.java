package com.riskmanagement.fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.riskmanagement.R;
import com.riskmanagement.activity.ProjectStatueActivity;
import com.riskmanagement.adapter.ProjectAdapter;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.ArHomeData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observable;
import rx.schedulers.Schedulers;


/**
 * Created by Administrator on 2017/6/1.
 */

public class ProjectListFragment extends Fragment implements AdapterView.OnItemClickListener,AdapterView.OnItemSelectedListener{
    private Spinner sp_projectlist_select;
    private ArrayAdapter<String> mAdapter;
    private String[] mItems=new String[]{"全部","作业初勘", "作业复查", "作业交底"};
    private ProjectAdapter projectAdapter;
    private ListView lv_projectlist_list;
    private ArrayList<ArHomeData.DataBean.ListBean> projectList;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_projectlist, container, false);
        initView(view);
        initWidget();
        return view;
    }

    private void initWidget() {
        lv_projectlist_list.setOnItemClickListener(this);
        sp_projectlist_select.setOnItemSelectedListener(this);
        sp_projectlist_select.setSelection(1,true);
    }

    private void initView(View view) {
        sharedPreferences=getContext().getSharedPreferences("myData", Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
        projectList = new ArrayList<>();
        ArHomeData.DataBean.ListBean bean=new ArHomeData.DataBean.ListBean();

        projectAdapter = new ProjectAdapter(projectList, getContext());
        lv_projectlist_list = (ListView) view.findViewById(R.id.lv_projectlist_list);
        sp_projectlist_select = (Spinner) view.findViewById(R.id.sp_projectlist_select);
        lv_projectlist_list.setAdapter(projectAdapter);

        mAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, mItems);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_projectlist_select.setAdapter(mAdapter);

    }

    private void initData(String statu) {
        HashMap<String, String> params = new HashMap<>();
        params.put("stat", statu);
        Call<ArHomeData> arHomeDataCall = HttpHelper.getArHomeData(params, new Callback<ArHomeData>() {
            @Override
            public void onResponse(Call<ArHomeData> call, Response<ArHomeData> response) {
                projectList.clear();
                projectAdapter.getList().clear();
                projectList=(ArrayList<ArHomeData.DataBean.ListBean>) response.body().getData().getList();
                Log.e("ssh","com.riskmanagement.aa====="+response.body().getData().getList().toString());
                projectAdapter.getList().addAll(projectList);
                Log.d("++++",String.valueOf(projectAdapter.getList().size()));
                projectAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(Call<ArHomeData> call, Throwable t) {
                Toast.makeText(getContext(),"网络访问异常",Toast.LENGTH_LONG);
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Bundle bundle=new Bundle();
        bundle.putString("statu",projectAdapter.getList().get(i).getStatus());
        Intent intent=new Intent(getActivity(), ProjectStatueActivity.class);
        intent.putExtras(bundle);
        editor.putString("id",projectAdapter.getList().get(i).getId());
        editor.commit();
        Log.d("myID",sharedPreferences.getString("id",""));
        startActivity(intent);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i){
            case 0:
                initData("3");
                break;
            case 1:
                initData("0");
                break;
            case 2:
                initData("1");
                break;
            case 3:
                initData("2");
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }


}
