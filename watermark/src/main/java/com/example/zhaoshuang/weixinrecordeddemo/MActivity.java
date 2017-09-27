package com.example.zhaoshuang.weixinrecordeddemo;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.os.StatFs;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.zhaoshuang.weixinrecordeddemo.util.Utils;
import com.yixia.camera.MediaRecorderBase;
import com.yixia.camera.MediaRecorderNative;
import com.yixia.camera.VCamera;
import com.yixia.camera.model.MediaObject;
import com.yixia.videoeditor.adapter.UtilityAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

//VCamera
/**
 * 仿新版微信录制视频
 * 基于ffmpeg视频编译
 * 使用的是免费第三方VCamera
 * Created by zhaoshuang on 17/2/8.
 */
public class MActivity extends BaseActivity implements MediaRecorderBase.OnEncodeListener, View.OnClickListener {

    private static final int REQUEST_START_SETTING = 101;
    private static final int REQUEST_KEY = 100;
    MediaRecorderNative mMediaRecorder;
    private MediaObject mMediaObject;
    private FocusSurfaceView sv_ffmpeg;
    private RecordedButton rb_start;
    private int maxDuration = 40 * 60 * 1000;
    private boolean recordedOver = true;
    private MyVideoView vv_play;
    private ImageView iv_finish;
    private ImageView iv_back;
    private float dp100;
    private TextView tv_hint;
    private float backX = -1;
    private TextView textView;

    /* private Chronometer chronometer;*/
    private ImageView iv_face;

    private RelativeLayout rl_showVideo;
    private String path;
    private String spaceFileName;
    private String finishFileName;
    private TextView tv_freeTime;

    private ImageView iv_setting;
    private int height;
    private int width;
    private int ma;
    private String fileName;
    private Timer timer;
    private TextView tv_time_minute;
    private TextView tv_time_second;
    private int time = 0;
    private int minute;
    private int second;
    private Boolean timerTerminal;

    private TimerTask timerTask;


    String mStartTime;
    String mEndTime;
    private String longitude = "";
    private String latitude = "";

    private Utils utils = new Utils();
    //获取地理位置管理器
    LocationManager locationManager;

    PowerManager.WakeLock wakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置全屏 系统导航栏被覆盖
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.a_activity_main);
        deleteDir(MyApplication.VIDEO_PATH);
        initView();
        initWidget();
        initData();
        tv_freeTime.setText("剩余空间:" + String.valueOf(GetSDFreeSize()) + "MB");
        initMediaRecorder();
       /* sv_ffmpeg.setTouchFocus(mMediaRecorder);*/
        rb_start.setMax(maxDuration);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, 0);

        timerTerminal = false;
        if (timer == null) {
            timer = new Timer();
            timerTask = initTimerTask();
            timer.schedule(timerTask, 0, 1000);
        }

        wakeLock = ((PowerManager) getSystemService(Context.POWER_SERVICE)).newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "TAG");
    }


    private void initWidget() {
        iv_face.setOnClickListener(this);
        iv_finish.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        rb_start.setOnClickListener(this);
        iv_setting.setOnClickListener(this);
       /* chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {

                tv_freeTime.setText("剩余空间:"+String.valueOf(GetSDFreeSize())+"MB");
                if(SystemClock.elapsedRealtime()-chronometer.getBase()>=maxDuration){

                    rb_start.ingButtom(false);
                    rb_start.closeButton();
                    videoFinish();
                    recordedOver=true;
                    chronometer.stop();
                }
            }
        });*/
    }

    private void initView() {
        sv_ffmpeg = (FocusSurfaceView) findViewById(R.id.sv_ffmpegaa);
        rb_start = (RecordedButton) findViewById(R.id.rb_start);
        vv_play = (MyVideoView) findViewById(R.id.vv_play);
        iv_finish = (ImageView) findViewById(R.id.iv_finish);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_hint = (TextView) findViewById(R.id.tv_hint);
        rl_showVideo = (RelativeLayout) findViewById(R.id.rl_showVideo);
        /*chronometer=(Chronometer)findViewById(R.id.chron_time) ;*/
        iv_face = (ImageView) findViewById(R.id.iv_face);
        tv_freeTime = (TextView) findViewById(R.id.tv_freeTime);
        iv_setting = (ImageView) findViewById(R.id.iv_setting);
        tv_time_minute = (TextView) findViewById(R.id.tv_time_minute);
        tv_time_second = (TextView) findViewById(R.id.tv_time_secnd);


        Log.e("cheight", String.valueOf(height));
        Log.e("cwidth", String.valueOf(width));
        Log.e("cma", String.valueOf(ma));
        //utils.getLocation(MActivity.this);
        //Log.e("mmmLocation",utils.getLongtitude()+utils.getLatitude());
        /*utils.getBaiduLocation(MActivity.this);*/
      /*  locationManager= (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        utils.getCurLocation(MActivity.this,locationManager);*/
        utils.getPosition(MActivity.this);
       /* Log.e("mmmLocation",utils.getLongtitude()+utils.getLatitude());*/
        tv_freeTime.setText("剩余空间:" + String.valueOf(GetSDFreeSize()) + "MB");
    }


    private void initData() {
        dp100 = getResources().getDimension(R.dimen.dp100);
        height = 480;
        width = 640;
        ma = 600;
    }

    /**
     * 初始化视频拍摄状态
     */
    private void initMediaRecorderState() {
        rl_showVideo.setVisibility(View.GONE);
        vv_play.pause();

        iv_back.setX(backX);
        iv_finish.setX(backX);

        tv_hint.setVisibility(View.VISIBLE);
        rb_start.setVisibility(View.VISIBLE);

        lastValue = 0;
    }

    /*
    * 录制结束
    * */
    private void videoFinish() {
        String video_timeStart;
        video_timeStart = Calendar.getInstance().getTime().toString();
        Log.e("edit_timeStart", video_timeStart);
        textView = showProgressDialog();
        mMediaRecorder.stopRecord();
        //开始合成视频, 异步
        mMediaRecorder.startEncoding();
        iv_back.setClickable(true);
        iv_finish.setClickable(true);
        rb_start.setClickable(true);
    }

    float lastValue;
    /*private void startAnim() {

        rb_start.setVisibility(View.GONE);
        ValueAnimator va = ValueAnimator.ofFloat(0, dp100).setDuration(300);
        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {

                float value = (float) animation.getAnimatedValue();
                float dis = value-lastValue;
                iv_back.setX(iv_back.getX()-dis);
                iv_finish.setX(iv_finish.getX()+dis);
                lastValue = value;
            }
        });
        va.start();
    }*/


    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (!recordedOver) {
                //t.setProgress(mMediaObject.getDuration());
                myHandler.sendEmptyMessageDelayed(0, 50);
                tv_hint.setVisibility(View.GONE);

            }
        }
    };


    /**
     * 初始化录制对象
     */
    private void initMediaRecorder() {
        WindowManager windowManager = getWindowManager();
        Point p = new Point();
        windowManager.getDefaultDisplay().getSize(p);
        Log.d("aaaaaaaa", "hight" + p.y + " width:" + p.x);
        mMediaRecorder = ((MyApplication) getApplication()).mMediaRecorder;
        //mMediaRecorder.setVideoRecordedSize(p.y,p.x);
       /* mMediaRecorder.setVideoRecordedSize(width,height);*/
        mMediaRecorder.setVideoRecordedSize(640, 480);
        mMediaRecorder.setOnEncodeListener(this);
       /*String key = String.valueOf(System.currentTimeMillis());*/
        String key = fileName;
        if (key == null) key = "init";
        spaceFileName = key;
        Log.e("spaceName", key);
        mMediaRecorder.setVideoBitRate(800);
      /*  mMediaRecorder.setVideoBitRate(400);*/
        //设置缓存文件夹
        mMediaObject = mMediaRecorder.setOutputDirectory(key, VCamera.getVideoCachePath());
        //设置视频预览源
        mMediaRecorder.setSurfaceHolder(sv_ffmpeg.getHolder());
        //准备
        mMediaRecorder.prepare();
        //滤波器相关
        UtilityAdapter.freeFilterParser();
        UtilityAdapter.initFilterParser();
    }

    /**
     *
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (recordedOver) {
            time = 0;
            tv_time_minute.setText("00:");
            tv_time_second.setText("00");
        } else {
            Log.d("进入222", "---------");
            timerTerminal = true;
        }
      /*  deleteDir(MyApplication.VIDEO_PATH);*/
        mMediaRecorder.startPreview();
       /*未持有屏幕锁时 上锁*/
        if (!wakeLock.isHeld())
            wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        timerTerminal = false;
        mMediaRecorder.stopPreview();
        /*持有屏幕锁时  释放*/
        if (wakeLock.isHeld())
            wakeLock.release();
    }

    @Override
    public void onBackPressed() {
        if (rb_start.getVisibility() != View.VISIBLE) {
            Log.e("ssh","ssdad");
            initMediaRecorderState();
            LinkedList<MediaObject.MediaPart> medaParts = mMediaObject.getMedaParts();
            for (MediaObject.MediaPart part : medaParts) {
                mMediaObject.removePart(part, true);
            }
            mMediaRecorder.startPreview();
        } else {
            Log.e("ssh","bbbbbbbbbbbbb");


//            Intent home = new Intent(Intent.ACTION_MAIN);
//            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            home.addCategory(Intent.CATEGORY_HOME);
//            startActivity(home);
//            moveTaskToBack(true);
//            Intent intent = new Intent(MActivity.this, StartActivity.class);
//            startActivity(intent);
            mMediaRecorder.stopPreview();
            if (!recordedOver) {

                mMediaRecorder.stopRecord();
                rb_start.ingButtom(false);
                time = 0;
            }

            recordedOver = true;
            timerTerminal = false;

            super.onBackPressed();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        if (backX == -1) {
            backX = iv_back.getX();
        }
    }

    /*
    * 得到setting的返回数据
    * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            Log.e("eeee", "ee");
            if (requestCode == REQUEST_KEY) {
                Log.e("zzzz", "zzz");
                LinkedList<MediaObject.MediaPart> medaParts = mMediaObject.getMedaParts();
//                for (MediaObject.MediaPart part : medaParts) {
//                    mMediaObject.removePart(part, true);
//                }
                Set userSet = new HashSet();
                List delList = new ArrayList();
                for (MediaObject.MediaPart part : medaParts) {
                    delList.add(part);
                }
                userSet.removeAll(delList);

                deleteDir(MyApplication.VIDEO_PATH);
            } else if (requestCode == REQUEST_START_SETTING) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    height = bundle.getInt("height");
                    width = bundle.getInt("width");
                    ma = bundle.getInt("ma");
                    fileName = bundle.getString("fileName");
                    spaceFileName = fileName;
                    mMediaRecorder.setVideoRecordedSize(width, height);
                    Log.e("width", String.valueOf(width));
                    Log.e("height", String.valueOf(height));
                    Log.e("ccma", String.valueOf(ma));
                    Log.e("fileName", bundle.getString("fileName"));
                } else {
                    Log.e("hhhh", "null");
                }
            }
        }
    }

    /**
     * 删除文件夹下所有文件
     */
    public static void deleteDir(String dirPath) {

        File dir = new File(dirPath);
        if (dir.exists() && dir.isDirectory()) {
            File[] files = dir.listFiles();
            for (File f : files) {
                deleteDir(f.getAbsolutePath());
            }
        } else if (dir.exists()) {
            dir.delete();
        }
    }

    @Override
    public void onEncodeStart() {
        mStartTime = Calendar.getInstance().getTime().toString();
        Log.e("mStartTime", mStartTime);
        Log.i("Log.i", "onEncodeStart");
    }

    @Override
    public void onEncodeProgress(int progress) {
        if (textView != null) {
            textView.setText("视频编译中 " + progress + "%");
        }
    }

    /**
     * 视频编辑完成
     */
    @Override
    public void onEncodeComplete() {
        closeProgressDialog();
        mEndTime = Calendar.getInstance().getTime().toString();
        Log.e("mEndTime", mEndTime);
        Log.e("mTime", "time:" + mStartTime + "-->" + mEndTime);
        time = 0;
        Intent intent = new Intent(MActivity.this, VideoPlayActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("spaceFileName", spaceFileName);
        bundle.putString("finishFileName", "0.ts");
        bundle.putString("path", mMediaObject.getOutputTempVideoPath());
        intent.putExtras(bundle);
        recordedOver = true;
        // TODO: 2017/3/31 解决重复录制
        startActivityForResult(intent, REQUEST_KEY);
      /*  String video_timeEnd;
        video_timeEnd= Calendar.getInstance().getTime().toString();
        Log.e("edit_timeEnd",video_timeEnd);
        path = mMediaObject.getOutputTempVideoPath();
        if(!TextUtils.isEmpty(path)){
            rl_showVideo.setVisibility(View.VISIBLE);
            vv_play.setVideoPath(path);
            vv_play.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    vv_play.setLooping(true);
                    vv_play.start();
                }
            });
            vv_play.start();

            recordedOver = true;
            startAnim();
        }*/
    }


    @Override
    public void onEncodeError() {
        Log.i("Log.i", "onEncodeError");
    }

   /* */

    /**
     * 合成图片到视频里
     *//*
    private String mergeImage(){
        mStartTime = Calendar.getInstance().getTime().toString();
        //输出开始时间
        Log.d("TestTime_Start",mStartTime);

        //得到涂鸦view的bitmap图片
        Bitmap bitmap = Bitmap.createBitmap(rl_showVideo.getWidth(), rl_showVideo.getHeight(), Bitmap.Config.ARGB_8888);
        rl_showVideo.draw(new Canvas(bitmap));
        //这步是根据视频尺寸来调整图片宽高,和视频保持一致
        Matrix matrix = new Matrix();
        matrix.postScale(MediaRecorderBase.VIDEO_HEIGHT * 1f / bitmap.getWidth(), MediaRecorderBase.VIDEO_WIDTH * 1f / bitmap.getHeight());
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        File file = new File(MyApplication.VIDEO_PATH +"tuya.png");//将要保存图片的路径
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        String key="finish_"+String.valueOf(System.currentTimeMillis());
        String finishVideo = path.substring(0, path.lastIndexOf("/"))+"/"+key+".mp4";
        Log.e("finishFile",key);
        finishFileName=key;
        //ffmpeg -i videoPath -i imagePath -filter_complex overlay=0:0 -vcodec libx264 -profile:v baseline -preset ultrafast -b:v 3000k -g 30 -f mp4 outPath

        StringBuilder sb = new StringBuilder();
        sb.append("ffmpeg");
        sb.append(" -i");
        sb.append(" "+path);
        sb.append(" -i");
        sb.append(" "+MyApplication.VIDEO_PATH +"tuya.png");
        sb.append(" -filter_complex");
        sb.append(" overlay=0:0");
        sb.append(" -vcodec libx264 -profile:v baseline -preset ultrafast -b:v "+String.valueOf(ma)+"k -g 15");
        sb.append(" -f mp4");
        sb.append(" "+finishVideo);

        int i = UtilityAdapter.FFmpegRun("", sb.toString());

        if(i == 0){
            return finishVideo;
        }else{
            return "";
        }
    }*/


    /*
    * 监测当前存储容量
    * */
    public long GetSDFreeSize() {
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory();
        StatFs sf = new StatFs(path.getPath());
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize();
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize) / 1024 / 1024; //单位MB
    }


    @Override
    public void onClick(View v) {
            if(v.getId() == R.id.iv_face) {

                if (mMediaRecorder.getCamraFace() == android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK) {
                    mMediaRecorder.switchCamera(android.hardware.Camera.CameraInfo.CAMERA_FACING_FRONT);
                } else
                    mMediaRecorder.switchCamera(android.hardware.Camera.CameraInfo.CAMERA_FACING_BACK);

            }else if(v.getId() == R.id.rb_start) {
                if (!recordedOver) {
                    rb_start.setClickable(false);
                    iv_finish.setClickable(false);
                    iv_back.setClickable(false);
                    rb_start.ingButtom(false);
                  /*  chronometer.stop();*/
                    rb_start.closeButton();
                    videoFinish();
                    recordedOver = true;
                    timerTerminal = false;
                } else {
                    mMediaRecorder.startRecord();
                    rb_start.ingButtom(true);
                 /*   chronometer.setBase(SystemClock.elapsedRealtime());
                    chronometer.start();*/

                    myHandler.sendEmptyMessageDelayed(0, 50);
                    recordedOver = false;
                    timerTerminal = true;
                }
            }
           /* case R.id.iv_back:
                iv_face.setVisibility(View.VISIBLE);
                iv_setting.setVisibility(View.VISIBLE);
                chronometer.setBase(SystemClock.elapsedRealtime());
                onBackPressed();
                break;*/
           /* case R.id.iv_finish:
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected void onPreExecute() {
                        showProgressDialog();
                    }
                    @Override
                    protected String doInBackground(Void... params) {
                        return mergeImage();
                    }
                    @Override
                    protected void onPostExecute(String result) {
                        closeProgressDialog();
                        if(!TextUtils.isEmpty(result)) {
                            mEndTime = Calendar.getInstance().getTime().toString();
                            Log.d("TestEndTime",mEndTime);
                            Log.d("TestTime-->","start: "+mStartTime+" end: "+mEndTime);
                            Intent intent = new Intent(MActivity.this, VideoPlayActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("spaceFileName",spaceFileName);
                            bundle.putString("finishFileName",finishFileName);
                            bundle.putString("path",result);
                            intent.putExtras(bundle);
                           *//* iv_setting.setVisibility(View.VISIBLE);
                            iv_face.setVisibility(View.);*//*
                            startActivity(intent);
                        }
                        chronometer.setBase(SystemClock.elapsedRealtime());
                    }
                }.execute();

                break;*/
            else if(v.getId() ==  R.id.iv_setting){
                Intent intent = new Intent(MActivity.this, SettingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("swidth", width);
                bundle.putInt("sma", ma);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_START_SETTING);
        }
    }

    private TimerTask initTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {      // UI thread
                    @Override
                    public void run() {
                        if (timerTerminal) {
                            tv_freeTime.setText("剩余空间:" + String.valueOf(GetSDFreeSize()) + "MB");
                            time++;
                            Log.e("mthread", String.valueOf(time));
                            second = time % 60;
                            minute = time / 60;
                            if (minute < 10) {
                                tv_time_minute.setText("0" + String.valueOf(minute) + ":");
                            } else {
                                tv_time_minute.setText(String.valueOf(minute) + ":");
                            }
                            if (second < 10) {
                                tv_time_second.setText("0" + String.valueOf(second));
                            } else {
                                tv_time_second.setText(String.valueOf(second));
                            }
                            if (time > 40 * 60) {
                                rb_start.setClickable(false);
                                iv_finish.setClickable(false);
                                iv_back.setClickable(false);
                                rb_start.ingButtom(false);
                  /*  chronometer.stop();*/
                                rb_start.closeButton();
                                timer.cancel();
                                videoFinish();
                                recordedOver = true;
                            }
                        }
                    }
                });
            }
        };
    }
}




