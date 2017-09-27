package com.example.zhaoshuang.weixinrecordeddemo;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoshuang.weixinrecordeddemo.http.UploadActivity;
import com.example.zhaoshuang.weixinrecordeddemo.http.bean.ResponeBean;
import com.yixia.camera.MediaRecorderNative;

import java.util.Random;

import retrofit2.Call;

/**
 * Created by zhaoshuang on 17/2/24.
 */

public class VideoPlayActivity extends BaseActivity implements View.OnClickListener {

    //显示播放画面
    private MyVideoView vv_play;
    //加密按钮
    private Button btn_encryption;
    //上传按钮
    private Button btn_upload;
    //文件路径和文件名
    private String path;
    private String finishFileName;
    private String spaceFileName;
    //加密工具
    private Code code;
    //进度文字
    private String dialogProgress = "视频上传中";
    //进度
    private int progress = 0;
    //加密信息
    String passwd;

    //Http请求
    Call<ResponeBean> mCall;
    //上传进度
    long uploadProgress;
    //文件总长度
    int fileContentLength;
    //当前分片已上传的长度
    int nowDivisionSendedL = 0;
    //更新进度的msg
    Message progressMsg = new Message();
    //文件路径
    private String uploadFilePath = "/sdcard/WeiXinRecordedDemo/";

    private AlertDialog progressDialog;
    private int mState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.a_activity_video_play);

        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        btn_encryption = (Button) findViewById(R.id.btn_encryption);
        btn_upload = (Button) findViewById(R.id.bt_upload);

        Intent intent = getIntent();

        path = intent.getStringExtra("path");
        spaceFileName = intent.getStringExtra("spaceFileName");
        finishFileName = intent.getStringExtra("finishFileName");
        //Log.e("mfinishName", finishFileName);
        //Log.e("mspaceName", spaceFileName);

        //String password=getRandomString(5);
        //Log.e("randomString",password);
        code = new Code("abcde");
        //vv_play.setVideoPath(MyApplication.VIDEO_PATH + "0.mp4");
        vv_play.setVideoPath(MyApplication.VIDEO_PATH + "0.mp4");
        vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                vv_play.setLooping(true);
                vv_play.start();
            }
        });
        btn_upload.setClickable(false);
        btn_upload.setVisibility(View.GONE);
        mState = 0;
        btn_encryption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                try {
//                    passwd = code.encode("video.txt", finishFileName, spaceFileName);
//                    Log.d("aaaaa", path + spaceFileName + "gggg");
//                    setUploadButtonOrEncryptionButton(false, 0);
//                    setUploadButtonOrEncryptionButton(true, 1);
//                    Toast.makeText(VideoPlayActivity.this, "加密完成", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    e.printStackTrace();

                switch (mState) {
                    case 0:
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected void onPreExecute() {

                                showProgressDialog("正在加密...");
                            }

                            @Override
                            protected Boolean doInBackground(Void... params) {
                                try {
                                    passwd = code.encode("video.txt", "0", spaceFileName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return true;
                            }
                            @Override
                            protected void onPostExecute(Boolean result) {
                                closeProgressDialog();
                                Toast.makeText(VideoPlayActivity.this, "加密完成", Toast.LENGTH_SHORT).show();
                            }
                        }.execute();
                        // TODO: 2017/4/6 压缩上传
                        /*btn_encryption.setText("压缩上传");*/
                        btn_encryption.setVisibility(View.GONE);
                        mState = 1;
                        btn_upload.setClickable(true);
                        btn_upload.setVisibility(View.VISIBLE);
                        btn_upload.setText("压缩并上传");
                        break;
                    // TODO: 2017/4/6 压缩并上传
                    /*case 1:
                        new AsyncTask<Void, Void, Boolean>() {
                            @Override
                            protected void onPreExecute() {

                                showEnProgressDialog();
                            }

                            @Override
                            protected Boolean doInBackground(Void... params) {
                                Log.e("yasuoStartTime--->", Calendar.getInstance().getTime().toString());
                                String cmd = String.format("-y -i /sdcard/WeiXinRecordedDemo/0.mp4 -strict -2 -vcodec libx264 -preset ultrafast -crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 9:16 /sdcard/WeiXinRecordedDemo/1.mp4");
                               *//* String cmd = String.format("-y -i /sdcard/gg/0.mp4 -strict -2 -vcodec libx264 -preset ultrafast -crf 24 -acodec aac -ar 44100 -ac 2 -b:a 96k -s 640x480 -aspect 9:16 /sdcard/gg/1.mp4");*//*
                                int i = UtilityAdapter.FFmpegRun("", cmd);
                                try {
                                    passwd = code.encode("video.txt", "1", spaceFileName);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                return i == 0;
                            }
                            @Override
                            protected void onPostExecute(Boolean result) {

                                Log.e("yasuoEndTime--->", Calendar.getInstance().getTime().toString());
                                closeEnProgressDialog();
                                Log.e("ggggg","gggg");
                                Intent intent = new Intent(VideoPlayActivity.this, UploadActivity.class);
                                intent.putExtra("path", uploadFilePath + spaceFileName + ".mp4");
                                intent.putExtra("fileName", spaceFileName + MediaRecorderNative.VIDEO_SUFFIX);
                                startActivity(intent);
                                finish();
                            }
                        }.execute();
                       *//* Intent intent = new Intent(VideoPlayActivity.this,UploadActivity.class);
                        intent.putExtra("path",uploadFilePath + spaceFileName + ".mp4");
                        intent.putExtra("fileName",spaceFileName + MediaRecorderNative.VIDEO_SUFFIX);
                        startActivity(intent);*//*
                        break;*/
                }
               /* try {
                    code.decode("video.txt",spaceFileName,finishFileName);
                } catch (Exception e) {
                    e.printStackTrace();
                }*/
                /*new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPreExecute() {
                        showProgressDialog();
                    }
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            code.enCode("video.txt",finishFileName,spaceFileName);
                            Log.e("finishName",finishFileName);
                            Log.e("spaceName",spaceFileName);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        closeProgressDialog();

                    }
                }.execute();*/
            }
        });

        btn_upload.setOnClickListener(this);
       /* setUploadButtonOrEncryptionButton(true,0);*/
    }

    @Override
    public void onClick(View view) {
        if(view.getId() ==R.id.bt_upload){
            Intent intent = new Intent(VideoPlayActivity.this, UploadActivity.class);
            intent.putExtra("path", uploadFilePath + spaceFileName + ".mp4");
            intent.putExtra("fileName",spaceFileName+MediaRecorderNative.VIDEO_SUFFIX);
            startActivity(intent);
            finish();
        }
    }

    // TODO: 2017/3/31 解决重复录制
    @Override
    public void onBackPressed() {
        setResult(RESULT_OK);
        finish();
        //super.onBackPressed();
    }

    // TODO: 2017/3/31 解决重复录制
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK){
            if (requestCode==200){
                onBackPressed();
            }
        }
    }

    //生成随机字符串
    public String getRandomString(int length) { //length表示生成字符串的长度
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        return sb.toString();

    }

    //String finishPath = "/sdcard/WeiXinRecordedDemo/"+"/"+spaceFileName+".mp4";


    public void showProgressDialog(String string) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        View view = View.inflate(this, R.layout.a_dialog_loading, null);
        builder.setView(view);
        ProgressBar pb_loading = (ProgressBar) view.findViewById(R.id.pb_loading);
        TextView tv_hint = (TextView) view.findViewById(R.id.tv_hint);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            pb_loading.setIndeterminateTintList(ContextCompat.getColorStateList(this, R.color.dialog_pro_color));
        }
        tv_hint.setText(string);
        progressDialog = builder.create();
        progressDialog.show();

    }

    public void closeProgressDialog() {
        try {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    /**
//     * 设置上传按钮可否点击
//     *
//     * @param isUpload 是否是上传按钮
//     * @param state    1为可以点击 其他为不可点击
//     */
//    private void setUploadButtonOrEncryptionButton(boolean isUpload, int state) {
//        if (isUpload) {

    /**
     *  设置上传按钮可否点击
     * @param isUpload 是否是上传按钮
     * @param state 1为可以点击 其他为不可点击
     *//*
    private void setUploadButtonOrEncryptionButton(boolean isUpload , int state) {
        mState=state;
        if (isUpload){
>>>>>>> 81010af3b0581e5626d4f7886d87c09e8a24cb5e
            if (state == 1) {
                btn_upload.setClickable(true);
                btn_upload.setVisibility(View.VISIBLE);
            } else {
                btn_upload.setClickable(false);
                btn_upload.setVisibility(View.GONE);
            }
        } else {
            if (state == 1) {
                btn_encryption.setClickable(true);
                btn_encryption.setVisibility(View.VISIBLE);
            } else {
                btn_encryption.setClickable(false);
                btn_encryption.setVisibility(View.GONE);
            }
        }

<<<<<<< HEAD
    }

*/
}
