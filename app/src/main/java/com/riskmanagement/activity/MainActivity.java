package com.riskmanagement.activity;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;

import com.riskmanagement.R;
import com.riskmanagement.fragment.MyAccountFragment;
import com.riskmanagement.fragment.ProjectListFragment;

public class MainActivity extends FragmentActivity {
    private FragmentTabHost mTabHost;
    private String[] tabTags={"projectList","myAccount"};
    private Class[] tabFragments={ProjectListFragment.class,MyAccountFragment.class};
    private String[] tabTitles={"项目列表","我的"};
    private int[] tabIcons={R.mipmap.ic_launcher,R.mipmap.ic_launcher};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for(int i=0;i<tabTags.length;i++){
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(tabTags[i]);
            tabSpec.setIndicator(getTabView(i));
            mTabHost.addTab(tabSpec, tabFragments[i], null);
        }
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
    }

    private View getTabView(int i) {
        View view = LayoutInflater.from(this).inflate(R.layout.view_tab_main, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_mainTabIcon);
        TextView tvTitle = (TextView) view.findViewById(R.id.tv_mainTabTitle);
//        tvTitle.setBackgroundResource(R.drawable.selector_tab_background);
        ivIcon.setImageResource(tabIcons[i]);
        tvTitle.setText(tabTitles[i]);
        return view;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if(mTabHost.getCurrentTab()==1){
                mTabHost.setCurrentTab(0);
                return true;
            }else if(mTabHost.getCurrentTab()==0){
                finish();
                return true;
            }
        }
        return false;
    }
}
