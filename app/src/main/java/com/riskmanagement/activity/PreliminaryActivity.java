package com.riskmanagement.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.riskmanagement.R;
import com.riskmanagement.fragment.LiveSceneFragment;
import com.riskmanagement.fragment.GPSFragment;
import com.riskmanagement.fragment.RiskIdentificationFragment;
import com.riskmanagement.utils.ViewPagerIndicator;
import com.riskmanagement.utils.myPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pc on 2017/6/1.
 */

public class PreliminaryActivity extends BaseActivity {
    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private List<Fragment> list = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    String statu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_preliminary);
        initTop("作业初勘");
        statu = getIntent().getStringExtra("statu");
        initViews();
        initDatas();
        myPager myPager =new myPager(mViewPager,mIndicator,list,mAdapter);
    }
    //初始化Views
    private void initViews(){
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
    }
    //初始化数据
    private void initDatas() {
        LiveSceneFragment liveSceneFragment = new LiveSceneFragment();
        GPSFragment gpsFragment = new GPSFragment();
        Bundle statuNum=new Bundle();
        statuNum.putString("statu",statu);
        gpsFragment.setArguments(statuNum);
        RiskIdentificationFragment riskIdentificationFragment = new RiskIdentificationFragment();
        Bundle arguments=new Bundle();
        arguments.putInt("statueID",0);
        riskIdentificationFragment.setArguments(arguments);
        list.add(liveSceneFragment);
        list.add(gpsFragment);
        list.add(riskIdentificationFragment);
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return list.get(position);
            }
            @Override
            public int getCount() {
                return list.size();
            }
        };
//        mViewPager.setOffscreenPageLimit(3);
    }
}
