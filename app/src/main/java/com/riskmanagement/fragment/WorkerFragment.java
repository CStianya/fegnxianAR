package com.riskmanagement.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.riskmanagement.R;
import com.riskmanagement.activity.AddWorkerActivity;
import com.riskmanagement.activity.ClarificationActivity;
import com.riskmanagement.adapter.WorkerAdapter;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.GetArData;
import com.riskmanagement.http.bean.UpProject;
import com.riskmanagement.http.bean.UserList;

import java.util.ArrayList;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.riskmanagement.MyApplication.mUserList;

/**
 * Created by Administrator on 2017/6/3.
 */

public class WorkerFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener{
    private ListView lv_worker;
    private Button btn_worker_add;
    private Button btn_worker_edit;
    private Button btn_worker_next;
    private ArrayList<UserList.DataBean.ListBean> userList=new ArrayList<>();
    private WorkerAdapter workerAdapter;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String id;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_worker,container,false);
        initView(view);
        initWidget();
        initData();
        return view;
    }

    private void initWidget() {
        btn_worker_add.setOnClickListener(this);
        btn_worker_next.setOnClickListener(this);
        btn_worker_edit.setOnClickListener(this);
    }

    private void initView(View view) {
        sharedPreferences=getContext().getSharedPreferences("myData",MODE_PRIVATE);
        editor=sharedPreferences.edit();
        id=sharedPreferences.getString("id","");
        lv_worker=(ListView)view.findViewById(R.id.lv_worker_worker);
        btn_worker_add=(Button)view.findViewById(R.id.btn_worker_add);
        btn_worker_edit=(Button)view.findViewById(R.id.btn_worker_edit);
        btn_worker_next=(Button)view.findViewById(R.id.btn_worker_next);
        workerAdapter=new WorkerAdapter(mUserList,getActivity());
        lv_worker.setAdapter(workerAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_worker_add:
               /* startActivityForResult(new Intent(getContext(), AddWorkerActivity.class),requestCode);*/
                startActivity(new Intent(getContext(), AddWorkerActivity.class));
                break;
            case R.id.btn_worker_next:
                submit();
                startActivity(new Intent(getActivity(), ClarificationActivity.class));
                getActivity().finish();
                break;
            case R.id.btn_worker_edit:
                int j=0;
                for (int i = 0; i < lv_worker.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout) lv_worker.getChildAt(i);// 获得子item的layout
                    CheckBox checkBox = (CheckBox) layout.findViewById(R.id.cb_worker_selected);// 从layout中获得控件,根据其id
                    if(checkBox.isChecked()){
                        mUserList.remove(i-j);
                        j++;
                    }
                    Log.d("mySize",String.valueOf(mUserList.size()));
                }
                workerAdapter.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }

    private void initData() {
        HashMap<String,String> params=new HashMap<>();
        params.put("id",id);
        Log.e("ssh",id);
        final Call<GetArData> getArDataCall= HttpHelper.getArData(params, new Callback<GetArData>() {
            @Override
            public void onResponse(Call<GetArData> call, Response<GetArData> response) {
                mUserList.addAll(response.body().getData().getUserList());
                mHandler.sendEmptyMessage(10);
            }
            @Override
            public void onFailure(Call<GetArData> call, Throwable t) {
                Toast.makeText(getContext(),"网络访问异常",Toast.LENGTH_LONG);
            }
        });

    }
    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    Log.e("ssh","aaaaa");
                    workerAdapter.notifyDataSetChanged();
                    Log.e("ssh","bbbb");
                    break;
            }
        }
    };

    private void submit(){

        HashMap<String,String> params=new HashMap<>();
        params.put("id",id);
        params.put("status","1");
        params.put("arDesc","");
        Call<UpProject> upProjectCall=HttpHelper.upProject(params, new Callback<UpProject>() {
            @Override
            public void onResponse(Call<UpProject> call, Response<UpProject> response) {
                Log.d("isSuccess",response.body().getMessage());
            }

            @Override
            public void onFailure(Call<UpProject> call, Throwable t) {
                Toast.makeText(getContext(),"网络访问异常",Toast.LENGTH_LONG);
                Log.d("error",t.toString());
            }
        });
    }
    @Override
    public void onResume() {
        super.onResume();
        workerAdapter.notifyDataSetChanged();
        Log.d("+++","Aaaa");
    }

    /*  @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }*/

    @Override
    public void onItemClick (AdapterView < ? > adapterView, View view,int i, long l){

    }

}
