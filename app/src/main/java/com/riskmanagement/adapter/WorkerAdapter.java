package com.riskmanagement.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.riskmanagement.R;
import com.riskmanagement.http.bean.ArHomeData;
import com.riskmanagement.http.bean.GetArData;
import com.riskmanagement.http.bean.UserList;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by Administrator on 2017/6/5.
 */

public class WorkerAdapter extends BaseAdapter {
    @Getter
    @Setter
    private List<UserList.DataBean.ListBean> list;
    private Context mContext;
    private LayoutInflater layoutInflater;
    public WorkerAdapter(List<UserList.DataBean.ListBean> list, Context mContext){
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
            view=layoutInflater.inflate(R.layout.item_worker,viewGroup,false);
            viewHolder=new ViewHolder();
            viewHolder.tv_worker_name=(TextView)view.findViewById(R.id.tv_worker_name);
            viewHolder.tv_worker_id=(TextView)view.findViewById(R.id.tv_worker_id);
            viewHolder.tv_worker_department=(TextView)view.findViewById(R.id.tv_worker_department);
            viewHolder.tv_worker_post=(TextView)view.findViewById(R.id.tv_worker_post);
            viewHolder.cb_worker_selected=(CheckBox)view.findViewById(R.id.cb_worker_selected);
            view.setTag(viewHolder);
        }else{
            viewHolder=(ViewHolder) view.getTag();
        }
        viewHolder.tv_worker_name.setText("名字"+list.get(i).getRealName());
        viewHolder.tv_worker_id.setText("ID"+list.get(i).getId());
        viewHolder.tv_worker_department.setText("部门"+list.get(i).getOfficeDesc());
        viewHolder.tv_worker_post.setText("职务"+list.get(i).getRoleDesc());
        return view;
    }
    protected static class ViewHolder {
        private TextView tv_worker_name;
        private TextView tv_worker_id;
        private TextView tv_worker_department;
        private TextView tv_worker_post;
        private CheckBox cb_worker_selected;
    }
}
