package com.riskmanagement;

import android.app.Application;
import android.graphics.Bitmap;

import com.riskmanagement.http.bean.UserList;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/6/3.
 */

public class MyApplication extends Application {
    public static ArrayList<Bitmap> photoList=new ArrayList<>();
    public static String mLocation=null;
    public static ArrayList<UserList.DataBean.ListBean> mUserList=new ArrayList<>();

//    public static String domainName = "http://fkar.lczyfz.com";
    public static String domainName = "http://139.196.48.196:7979";
    //删除的图片idString 删除图片id用逗号隔开
    public static String MPdeleteId = "";
    //坐标
    public static String MPcoordinates = "";
    //userid
    public static String data = "";

    public MediaRecorderNative mMediaRecorder;
    public static String VIDEO_PATH = "/sdcard/WeiXinRecordedDemo/";

    @Override
    public void onCreate() {
        initVcamera();
        mMediaRecorder = new MediaRecorderNative();
        super.onCreate();
    }

    public void initVcamera(){
        /*VIDEO_PATH += String.valueOf(System.currentTimeMillis());*/
        File file = new File(VIDEO_PATH);
        if(!file.exists()) file.mkdirs();

        //设置视频缓存路径
        VCamera.setVideoCachePath(VIDEO_PATH);

        // 开启log输出,ffmpeg输出到logcat
        VCamera.setDebugMode(true);

        // 初始化拍摄SDK，必须
        //VCamera.initialize(this);
    }

}
