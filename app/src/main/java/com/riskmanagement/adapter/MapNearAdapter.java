package com.riskmanagement.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.baidu.mapapi.search.core.PoiInfo;
import com.riskmanagement.R;

import java.util.ArrayList;



/**
 * Created by ssh on 2017/6/20.
 */

public class MapNearAdapter extends BaseAdapter {
    ArrayList<PoiInfo> nearList;
    Activity activity;
    private LayoutInflater inflater;
    public MapNearAdapter(ArrayList<PoiInfo> nearList,Activity activity){

        this.nearList = nearList;
        this.activity = activity;
        this.inflater= LayoutInflater.from(activity);
    }

    @Override
    public int getCount() {
        return nearList==null?0:nearList.size();
    }

    @Override
    public Object getItem(int i) {
        return nearList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //加载布局为一个视图
        View view=inflater.inflate(R.layout.adapter_mapnear_item,null);
        PoiInfo poiInfo = nearList.get(position);
        TextView TV_name = (TextView) view.findViewById(R.id.TV_name);
        TextView TV_address = (TextView) view.findViewById(R.id.TV_address);
        TV_name.setText(poiInfo.name);
        TV_address.setText(poiInfo.address);
        return view;
    }
}
