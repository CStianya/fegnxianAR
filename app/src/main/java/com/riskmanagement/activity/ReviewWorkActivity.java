package com.riskmanagement.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.baidu.mapapi.SDKInitializer;
import com.riskmanagement.R;
import com.riskmanagement.fragment.GPSFragment;
import com.riskmanagement.fragment.LiveSceneFragment;
import com.riskmanagement.fragment.ReviewGPSFragment;
import com.riskmanagement.fragment.RiskIdentificationFragment;
import com.riskmanagement.fragment.WorkerFragment;
import com.riskmanagement.utils.ViewPagerIndicator;
import com.riskmanagement.utils.myPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/3.
 */

public class ReviewWorkActivity extends BaseActivity {
    private ViewPager mViewPager;
    private ViewPagerIndicator mIndicator;
    private List<Fragment> list = new ArrayList<Fragment>();
    private FragmentPagerAdapter mAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_review);
        initTop("作业前复测");
        initViews();
        initDatas();
        myPager myPager =new myPager(mViewPager,mIndicator,list,mAdapter);
    }
    //初始化Views
    private void initViews(){
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
        mIndicator = (ViewPagerIndicator) findViewById(R.id.id_indicator);
        mIndicator.setNum(4);
    }
    //初始化数据
    private void initDatas() {
        LiveSceneFragment liveSceneFragment = new LiveSceneFragment();
        ReviewGPSFragment reviewGPSFragment=new ReviewGPSFragment();
        RiskIdentificationFragment riskIdentificationFragment = new RiskIdentificationFragment();
        WorkerFragment workerFragment=new WorkerFragment();
        Bundle arguments=new Bundle();
        arguments.putInt("statueID",1);
        riskIdentificationFragment.setArguments(arguments);
        list.add(liveSceneFragment);
        list.add(reviewGPSFragment);
        list.add(riskIdentificationFragment);
        list.add(workerFragment);
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
    }
}
