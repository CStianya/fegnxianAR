package com.yixia.camera;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.util.Log;

import com.example.zhaoshuang.weixinrecordeddemo.MyApplication;
import com.example.zhaoshuang.weixinrecordeddemo.util.Utils;
import com.yixia.camera.model.MediaObject;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * 视频录制：边录制边底层处理视频（旋转和裁剪）
 *
 * @author yixia.com
 */
public class MediaRecorderNative extends MediaRecorderBase implements MediaRecorder.OnErrorListener {
    Camera.Size size;
    ByteArrayOutputStream stream;
    Bitmap bitmap;
    int textSize = 0;
    int waterMarkRotate = 0;

    /* 工具类utils*/private Utils utils = new Utils();

    /**
     * 视频后缀
     */
    public static final String VIDEO_SUFFIX = ".mp4";

    @Override
    public void setVideoRecordedSize(int width, int height) {
        super.setVideoRecordedSize(width, height);
        textSize = 0;
        waterMarkRotate = 0;
    }

    @Override
    public void setVideoBitRate(int bitRate) {
        super.setVideoBitRate(bitRate);
    }

    /**
     * 开始录制
     */
    @Override
    public MediaObject.MediaPart startRecord() {
        Log.e("ssh","开始录制--------------");
        //防止没有初始化的情况
        if (!UtilityAdapter.isInitialized()) {
            UtilityAdapter.initFilterParser();
        }
        stream = new ByteArrayOutputStream();

        MediaObject.MediaPart result = null;

        if (mMediaObject != null) {
            mRecording = true;
            result = mMediaObject.buildMediaPart(mCameraId, VIDEO_SUFFIX);
            String cmd = String.format("filename = \"%s\"; ", result.mediaPath);
            //如果需要定制非480x480的视频，可以启用以下代码，其他vf参数参考ffmpeg的文档：
            //cmd += String.format("addcmd = %s; "," -vf \"transpose=1,crop="+UtilityAdapter.VIDEO_WIDTH+":"+UtilityAdapter.VIDEO_HEIGHT+":0:240\" ");
            cmd += String.format("addcmd = %s; ", " -vf \"transpose=1\" ");
            UtilityAdapter.FilterParserAction(cmd, UtilityAdapter.PARSERACTION_START);
            if (mAudioRecorder == null && result != null) {
                mAudioRecorder = new AudioRecorder(this);
                mAudioRecorder.start();
            }
        }
        return result;
    }

    public int getCamraFace() {
        return mCameraId;
    }

    /**
     * 停止录制
     */
    @Override
    public void stopRecord() {
        try {
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        UtilityAdapter.FilterParserAction("", UtilityAdapter.PARSERACTION_STOP);
        super.stopRecord();
    }

    /**
     * 数据回调
     */
    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mRecording) {
            Log.e("ssh","加水印-------------");
            stream.reset();
            //解码
            size = camera.getParameters().getPreviewSize();
            if (textSize == 0){
                textSize = (int) (Math.sqrt(size.width*size.width+size.height*size.height)/"仅用于王小明本人在太平洋保险公司投保时使用".length());
                textSize*=0.8;
            }
            if (waterMarkRotate == 0){
                waterMarkRotate = (int)Math.toDegrees(Math.atan((float)size.height/(float) size.width))-180 ;
            }
            try {
                YuvImage image = new YuvImage(data, ImageFormat.NV21, size.width, size.height, null);
                if (image != null) {
                    image.compressToJpeg(new Rect(0, 0, size.width, size.height), 80, stream);
                    if(isFrontCamera()) {
                        bitmap = utils.createWatermark(utils.rotateBitmap(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()), 180), "仅用于王小明本人在太平洋保险公司投保时使用",textSize,waterMarkRotate);
                    }else{
                        bitmap = utils.createWatermark(BitmapFactory.decodeByteArray(stream.toByteArray(), 0, stream.size()), "仅用于王小明本人在太平洋保险公司投保时使用",textSize,waterMarkRotate);
                    }
                    data = utils.getNV21(bitmap.getWidth(), bitmap.getHeight(), bitmap);
                }

            } catch (Exception ex) {
                Log.e("ssh", "Error:" + ex.getMessage());
            }

            //底层实时处理视频，将视频旋转好，并剪切成480x480
            UtilityAdapter.RenderDataYuv(data);

        }
        super.onPreviewFrame(data, camera);
    }

    /**
     * 预览成功，设置视频输入输出参数
     */
    @Override
    protected void onStartPreviewSuccess() {
        if (mCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
            UtilityAdapter.RenderInputSettings(MediaRecorderBase.VIDEO_WIDTH, MediaRecorderBase.VIDEO_HEIGHT, 0, UtilityAdapter.FLIPTYPE_NORMAL);
        } else {
            UtilityAdapter.RenderInputSettings(MediaRecorderBase.VIDEO_WIDTH, MediaRecorderBase.VIDEO_HEIGHT, 180, UtilityAdapter.FLIPTYPE_HORIZONTAL);
        }
        UtilityAdapter.RenderOutputSettings(MediaRecorderBase.VIDEO_WIDTH, MediaRecorderBase.VIDEO_HEIGHT, mFrameRate, UtilityAdapter.OUTPUTFORMAT_YUV | UtilityAdapter.OUTPUTFORMAT_MASK_MP4 /*| UtilityAdapter.OUTPUTFORMAT_MASK_HARDWARE_ACC*/);
    }

    @Override
    public void onError(MediaRecorder mr, int what, int extra) {
        try {
            if (mr != null)
                mr.reset();
        } catch (IllegalStateException e) {
            Log.w("Yixia", "stopRecord", e);
        } catch (Exception e) {
            Log.w("Yixia", "stopRecord", e);
        }
        if (mOnErrorListener != null)
            mOnErrorListener.onVideoError(what, extra);
    }

    /**
     * 接收音频数据，传递到底层
     */
    @Override
    public void receiveAudioData(byte[] sampleBuffer, int len) {
        if (mRecording && len > 0) {
            UtilityAdapter.RenderDataPcm(sampleBuffer);
        }
    }


}
