package com.riskmanagement.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.List;

/**
 * Created by Administrator on 2016/10/18.
 */

//滑动变换
public class myPager {
    public myPager(ViewPager viewPager, ViewPagerIndicator viewPagerIndicator, List<Fragment> listint, FragmentPagerAdapter adapter) {
        viewPager.setAdapter(adapter);
        viewPagerIndicator.setNum(listint.size());
        viewPagerIndicator.setViewPager(viewPager,0);
    }

}
