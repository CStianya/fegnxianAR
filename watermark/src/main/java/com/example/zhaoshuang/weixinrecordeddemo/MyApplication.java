package com.example.zhaoshuang.weixinrecordeddemo;

import android.app.Application;

import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;

import java.io.File;

/**
 * Created by zhaoshuang on 17/2/8.
 */

public class MyApplication extends Application {
    public MediaRecorderNative mMediaRecorder;
    public static String VIDEO_PATH =  "/sdcard/WeiXinRecordedDemo/";


    @Override
    public void onCreate() {

        initVcamera();
        mMediaRecorder = new MediaRecorderNative();
        EncodeListener encodeListener = new EncodeListener();
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
        VCamera.initialize(this);
    }

    class EncodeListener implements  MediaRecorderBase.OnEncodeListener
    {

        @Override
        public void onEncodeStart() {

        }

        @Override
        public void onEncodeProgress(int progress) {

        }

        @Override
        public void onEncodeComplete() {
//            closeProgressDialog();
//            time=0;
//            Intent intent = new Intent(MActivity.this, VideoPlayActivity.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("spaceFileName",spaceFileName);
//            bundle.putString("finishFileName","0.ts");
//            bundle.putString("path",mMediaObject.getOutputTempVideoPath());
//            intent.putExtras(bundle);
//            recordedOver = true;
//            // TODO: 2017/3/31 解决重复录制
//            startActivityForResult(intent,REQUEST_KEY);
        }

        @Override
        public void onEncodeError() {

        }
    }
}
