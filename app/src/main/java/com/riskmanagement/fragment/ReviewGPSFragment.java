package com.riskmanagement.fragment;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.riskmanagement.R;
import com.riskmanagement.activity.GpsActivity;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.GetArData;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.riskmanagement.MyApplication.mLocation;

/**
 * Created by Administrator on 2017/6/3.
 */

public class ReviewGPSFragment extends Fragment {
    private Button btn_reviewGps_location;
    private TextView tv_reviewGps_location;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String id;
    int i = 0;
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_review_gps,container,false);
        initView(view);
        initData();
        return view;
    }



    private void initView(View view) {
        i++;
        sharedPreferences=getContext().getSharedPreferences("myData",MODE_PRIVATE);
        id=sharedPreferences.getString("id","");
        btn_reviewGps_location=(Button)view.findViewById(R.id.btn_reviewGps_reLocation);
        tv_reviewGps_location=(TextView)view.findViewById(R.id.tv_reviewGps_location);
        tv_reviewGps_location.setText(mLocation);
        btn_reviewGps_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), GpsActivity.class));
            }
        });
    }

    private void initData() {
        HashMap<String,String> params=new HashMap<>();
        params.put("id",id);
        final Call<GetArData> getArDataCall= HttpHelper.getArData(params, new Callback<GetArData>() {
            @Override
            public void onResponse(Call<GetArData> call, Response<GetArData> response) {

                mLocation=response.body().getData().getCoorDesc();
                tv_reviewGps_location.setText(response.body().getData().getCoorDesc());
            }
            @Override
            public void onFailure(Call<GetArData> call, Throwable t) {
                Toast.makeText(getContext(),"网络访问异常",Toast.LENGTH_LONG);
            }
        });

    }

    @Override
    public void onResume() {
        if(i != 0){
            tv_reviewGps_location.setText(mLocation);
        }
        super.onResume();
    }
}
