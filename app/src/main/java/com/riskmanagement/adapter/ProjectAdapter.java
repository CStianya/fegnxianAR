package com.riskmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.riskmanagement.R;
import com.riskmanagement.http.bean.ArHomeData;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ProjectAdapter extends BaseAdapter {
    @Getter@Setter protected List<ArHomeData.DataBean.ListBean> list;
    protected Context mContext;
    protected LayoutInflater layoutInflater;

    public ProjectAdapter(List<ArHomeData.DataBean.ListBean> list,Context mContext){
        this.mContext=mContext;
        this.list=list;
        layoutInflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder=null;
        if(view==null){
            view=layoutInflater.inflate(R.layout.item_projectlist,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.tv_project_name=(TextView)view.findViewById(R.id.tv_project_name);
            viewHolder.tv_project_statue=(TextView)view.findViewById(R.id.tv_project_statue);
            viewHolder.tv_project_date=(TextView)view.findViewById(R.id.tv_project_date);
            viewHolder.tv_project_introduce=(TextView)view.findViewById(R.id.tv_project_introduce);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.tv_project_name.setText(list.get(i).getProjectName());
        switch (list.get(i).getStatus()){
            case "0":
                viewHolder.tv_project_statue.setText("作业初勘");
                break;
            case "1":
                viewHolder.tv_project_statue.setText("作业前复查");
                break;
            case "2":
                viewHolder.tv_project_statue.setText("作业交底");
                break;
            case "3":
                viewHolder.tv_project_statue.setText("全部");
                break;
        }

        viewHolder.tv_project_date.setText(list.get(i).getDateTime());
        viewHolder.tv_project_introduce.setText(list.get(i).getArDesc());
        return view;
    }
    protected static  class ViewHolder {
        private TextView tv_project_name;
        private TextView tv_project_statue;
        private TextView tv_project_date;
        private TextView tv_project_introduce;
    }
}
