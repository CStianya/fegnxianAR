package com.riskmanagement.adapter;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.riskmanagement.R;
import com.riskmanagement.http.bean.GetArData;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

import static com.riskmanagement.MyApplication.domainName;


/**
 * Created by Administrator on 2017/6/3.
 */

public class GridAdapter extends BaseAdapter {
    @Getter
    @Setter
    private ArrayList<GetArData.DataBean.ListBeana> list;
    private LayoutInflater inflater;
    private Context mContext;
    public GridAdapter(ArrayList<GetArData.DataBean.ListBeana> list, Context mContext){
        this.mContext=mContext;
        this.list=list;
        this.inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {

            convertView = inflater.inflate(R.layout.item_image, parent, false);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
          /*  Bitmap bitmap=zoomImg(photoList.get(position),100,100);
            holder.imageView.setImageBitmap(bitmap);*/

        String Url = "";
        if("10".equals(list.get(position).getId())){
            Url = list.get(position).getUrl();
        }else{
            Url = domainName+list.get(position).getUrl();
        }
        Log.d("ssh", Url + " " + position);
            Glide.with(mContext)
                    .load(Url)
                    .centerCrop()
                    .crossFade()
                    .error(R.mipmap.img)
                    .into(holder.imageView);

        return convertView;
    }
    protected class ViewHolder {
        protected ImageView imageView;
    }
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }
}
