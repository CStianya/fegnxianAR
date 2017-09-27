package com.example.zhaoshuang.weixinrecordeddemo;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.Display;
import android.widget.TextView;

/**
 * Created by wcx on 2017/3/19.
 */

public class WatermarkTextView extends TextView{
    public WatermarkTextView(Context context) {
        super(context);
    }

    public WatermarkTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WatermarkTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //倾斜度45,上下左右居中
        canvas.rotate(-45, getMeasuredWidth()/2, getMeasuredHeight()/2);
        super.onDraw(canvas);
    }
}
