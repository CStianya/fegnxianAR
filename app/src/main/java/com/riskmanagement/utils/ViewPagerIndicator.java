package com.riskmanagement.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

//指示器
public class ViewPagerIndicator extends LinearLayout {
    private  int num=3;//tab数量
    private Paint mPaint;//画笔
    private Path mPath;//构造图形
    private int mRectangleWidth;//每个tab的宽
    private int mRectangleHeight;//每个tab的高
    private static final float RADIO_LINE_WIDTH = 1/2F; //宽与tab比例
    private int mInitTranslationX;//初始偏移位置
    private int mTranslationX;//图形跟着手指移动位移
    private static final  int COLOR_TEXT_NORMAL =0x77FFFFFF;
    private static final int COLOR_TEXT_HIGHLIGHT=0xFFFFFFFF;//高亮颜色
    private ViewPager mViewPager;



    public void setNum(int num){
        this.num=num;
    }

    //设置图形大小
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mRectangleWidth = (int)(w / num * RADIO_LINE_WIDTH);//每个tab宽度
        mInitTranslationX =(int)(w / num / 2 - mRectangleWidth / 2);//初始偏移量，位于tab中间往左1/2
        initLine();//初始化图形
    }


    //图形的绘制
    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitTranslationX + mTranslationX, getHeight());//平移
        canvas.drawPath(mPath, mPaint);//平移到指定位置，画出图形
        canvas.restore();
        super.dispatchDraw(canvas);

    }


    //初始化矩形
    private void initLine() {
        mRectangleHeight = mRectangleWidth / 10;
        mPath = new Path();
        mPath.moveTo(0, 0);//点位于tab中间往左1/2
        mPath.lineTo(mRectangleWidth, 0);//往右画图形宽度的长度
        //往下为y正半轴
        mPath.lineTo(mRectangleWidth, -mRectangleHeight);//往上画图形高度的长度
        mPath.lineTo(0, -mRectangleHeight);//往左画图形宽度的长度
        mPath.close();
    }




    //指示器跟着手指滑动
    public void scroll(int position,float offset) {
        int tabWidth = getWidth() / num;
        mTranslationX = (int) (tabWidth * offset + position * tabWidth);//总偏移量
        invalidate();//重绘图形
    }


    public ViewPagerIndicator(Context context) {
        this(context, null);
    }

    public ViewPagerIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        mPaint  = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#3090e6"));//画笔颜色
        mPaint.setStyle(Paint.Style.FILL);//画笔style
        mPaint.setPathEffect(new CornerPathEffect(3));//连接的点为圆角效果

    }

    //获得屏幕的宽度
    private int getScreenWidth()
    {
        WindowManager wm=(WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics=new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    //已将PageOnchangeListener接口占用，故提供PageOnchangeListener接口给外部
    public interface PageOnchangeListener
    {
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels);
        public void onPageSelected(int position);
        public void onPageScrollStateChanged(int state) ;
    }
    public PageOnchangeListener mListener;
    public void setOnPageChangeListener(PageOnchangeListener listener){
        this.mListener=listener;
    }

    /*
    * 设置关联的ViewPager
    * */
    public void setViewPager(ViewPager viewPager, final int pos){
        mViewPager=viewPager;
        setItemClickEvent();
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                scroll(position, positionOffset);
                if(mListener!=null)
                {
                    mListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
                }
            }

            @Override
            public void onPageSelected(int position) {
                if(mListener!=null)
                {
                    mListener.onPageSelected(position);
                }
                highLightTextView(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if(mListener!=null)
                {
                    mListener.onPageScrollStateChanged(state);
                }
            }
        });
        mViewPager.setCurrentItem(pos);
        highLightTextView(pos);
    }
    /*
    * 重置tab文本颜色
    * */
    private void resetTextViewColor()
    {
        for(int i=0;i<getChildCount();i++){
            View view=getChildAt(i);
            if(view instanceof TextView)
            {
                ((TextView)view).setTextColor(COLOR_TEXT_NORMAL);
            }
        }
    }
    /*
    * 高亮某个tab的文本
    *
    * */
    private void highLightTextView(int pos){
        resetTextViewColor();
        View view=getChildAt(pos);
        if(view instanceof TextView)
        {
            ((TextView)view).setTextColor(COLOR_TEXT_HIGHLIGHT);
        }
    }

    /*
    * 设置Tab的点击事件
    *
    * */
    private void setItemClickEvent()
    {
        int cCount=getChildCount();
        for(int i=0;i<cCount;i++)
        {
            final int j=i;
            View view=getChildAt(i);
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });

        }
    }
}
