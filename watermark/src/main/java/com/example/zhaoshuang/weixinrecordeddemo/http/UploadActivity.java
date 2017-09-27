package com.example.zhaoshuang.weixinrecordeddemo.http;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoshuang.weixinrecordeddemo.BaseActivity;
import com.example.zhaoshuang.weixinrecordeddemo.R;
import com.example.zhaoshuang.weixinrecordeddemo.http.bean.GetFileListBean;
import com.example.zhaoshuang.weixinrecordeddemo.http.bean.TempBean;
import com.example.zhaoshuang.weixinrecordeddemo.session.SessionKeeper;
import com.example.zhaoshuang.weixinrecordeddemo.util.FileDivisionHelper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UploadActivity extends BaseActivity implements View.OnClickListener {
    //开始上传
    private static final int MESSAGE_START_UPLOAD = 1001;
    //上传结束
    private static final int MESSAGE_FINISH_UPLOAD = 1002;
    //上传失败
    private static final int MESSAGE_UPLOAD_FAILED = 1003;
    //上传进度更新
    private static final int MESSAGE_UPLOAD_PROGRESS = 1004;
    //取消上传
    private static final int MESSAGE_UPLOAD_CANCEL = 1005;
    //获取已上传文件列表成功
    private static final int MESSAGE_GET_SUCCESS = 1006;
    //没有文件需要上传
    private static final int MESSAGE_NO_FILE_UPLOAD = 1007;

    //已上传的长度
    final SendedLength sendedLength = new SendedLength(0);
    //文件路径
    String filePath;
    //UI
    ProgressBar progressBar;
    Button stop;
    Button cancel;
    Button complete;
    TextView upload_tag;
    //文件总长度
    int fileContentLength;
    //文件分片帮助类
    FileDivisionHelper fileHelper;
    //标记按钮状态
    boolean tag_isStop = true;
    //机构号等信息
    String institution;
    String warranty;
    String userName;
    //文件名
    String fileName;
    //文件块数
    int fileCount;
    //剩余未上传的文件index
    List<Integer> notUploadFileList = new ArrayList<>();
    //标记上传是否完成
    boolean isUploadComplete = false;
    //callList
    List<Call<TempBean>> callList = new ArrayList<>();
    //是否在请求
    boolean isRequesting = false;
    //是否强制结束循环

    List<Thread> threadPool = new ArrayList<>();
    int startIndex = 0;

    InterrupterTag interrupterTag = new InterrupterTag();

    AlertDialog alertDialog;

    NetWorkBroadcastReceiver myNetReceiver = new NetWorkBroadcastReceiver();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_upload);
        filePath = getIntent().getStringExtra("path");
        Log.e("ssh","filePath===="+filePath);
        fileName = getIntent().getStringExtra("fileName");
        Log.e("ssh","fileName===="+fileName);
        initView();
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (myNetReceiver != null) {
                unregisterReceiver(myNetReceiver);
            }
        }catch(Exception e){
            e.printStackTrace();
        }

    }


    private void initView() {
        progressBar = (ProgressBar) findViewById(R.id.pb_upload);
        stop = (Button) findViewById(R.id.btn_stopOrStart);
        cancel = (Button) findViewById(R.id.btn_cancel);
        complete = (Button) findViewById(R.id.btn_upload_complete);
        upload_tag = (TextView) findViewById(R.id.tv_tag_upload);

        stop.setOnClickListener(this);
        cancel.setOnClickListener(this);
        complete.setOnClickListener(this);

        institution = SessionKeeper.getInstitution(this);
        warranty = SessionKeeper.getWarranty(this);
        userName = SessionKeeper.getUseName(this);
        //初始化完成后就开始上传任务
        upload4Parts_Start();
        //上传开始
        mHandler.sendEmptyMessage(MESSAGE_START_UPLOAD);
    }

    /**
     * 分片上传文件
     */
    private void upload4Parts_Start() {
//        uploadThread = new Thread());
//        uploadThread.start();
        Thread aa = new Thread(getUploadAsyncTask());
        aa.start();
        threadPool.add(aa);
    }

    /**
     *
     */
    private AlertDialog getAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false)
                .setMessage("是否取消上传?")
                .setTitle("提醒!")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mHandler.sendEmptyMessage(MESSAGE_UPLOAD_CANCEL);
                        cancelUpload();
                        // TODO: 回start
                        setResult(RESULT_OK);
                        finish();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        return builder.create();
    }

    /**
     * 上传文件
     */
//    private void upload() {
//        new AsyncTask<Object, Object, Object>() {
//            @Override
//            protected Object doInBackground(Object[] objects) {
//                android.util.Log.d("Path--------->", Environment.getExternalStorageDirectory() + "/testupload/007.mp4");
//                android.util.Log.d("测试时间_开始", "_____________________________");
//                //
//                //Environment.getExternalStorageDirectory()+"/testupload/007.mp4"
//                Map<String, String> params = new HashMap<>();
//                params.put("fileContentLength", String.valueOf(fileContentLength));
//                params.put("index", String.valueOf(i));
//                params.put("fileCount", String.valueOf(fileCount));
//                params.put("name", userName);
//                params.put("org", institution);
//                params.put("insurance", warranty);
//                params.put("psd", passwd);
//                mCall = HttpHelper.uploadFile(null, uploadFilePath + spaceFileName + ".mp4", new ProgressInterface.ProgressRequestListener() {
//                    @Override
//                    public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
//                        uploadProgress += bytesWritten;
//                        if (!done) {
//                            progress = (int) (((float) uploadProgress / (float) contentLength) * 100);
//                            mHandler.sendEmptyMessage(MESSAGE_UPLOAD_PROGRESS);
//                        } else {
//                            android.util.Log.d("测试时间_结束", "_____________________________");
//                            progress = 100;
//                        }
//                    }
//                }, new Callback<ResponeBean>() {
//                    @Override
//                    public void onResponse(Call<ResponeBean> call, Response<ResponeBean> response) {
//                        try {
//                            if (response.body().isSuccess()) {
//                                android.util.Log.d("VideoActivity Success", "________________success_________");
//                            }
//                            android.util.Log.d("VideoActivity Success", "--->" + String.valueOf(response.body().getMsg()));
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                        mHandler.sendEmptyMessage(MESSAGE_FINISH_UPLOAD);
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponeBean> call, Throwable t) {
//                        t.printStackTrace();
//                        android.util.Log.d("VideoActivity failure", "上传失败");
//                        mHandler.sendEmptyMessage(MESSAGE_UPLOAD_FAILED);
//                    }
//                });
//                return null;
//            }
//        }.execute();
//    }

    boolean requestTag;

    /**
     * 获取异步执行类
     *
     * @return 异步执行类
     */
    private Runnable getUploadAsyncTask() {
        return new Runnable() {
            @Override
            public void run() {
                callList.clear();
                interrupterTag.itpt = false;
                isRequesting = true;
                fileHelper = new FileDivisionHelper(filePath, UploadActivity.this);
                fileContentLength = fileHelper.getFileSize();
                fileCount = fileHelper.getDivisionFileCount();
                String str;
                List<Integer> fileIndexList = new ArrayList<>();
                if (startIndex == 0) {
                    for (int i = 0; i < fileCount; i++) fileIndexList.add(i);
                } else {
                    fileIndexList.clear();
                    fileIndexList.addAll(notUploadFileList);
                }
                Log.e("ssh", "需要上传的文件-->"+fileIndexList.size());
                Log.e("ssh", "需要上传的文件-->"+fileIndexList.toString());
                requestTag = true;
                /*
                 切割文件
                 */
                Map<Integer, String> needUploadFileList = new HashMap<>();
                for (int temp = 0; temp < fileIndexList.size(); temp++) {
                    int index = fileIndexList.get(temp);
                    try {
                        needUploadFileList.put(index, fileHelper.divisiveFile(index));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                /*
                上传请求
                 */
                for (int tag = 0; true; ) {
                    if (interrupterTag.itpt) break;
                    if (tag == fileIndexList.size()) break;
                    if (requestTag) {
                        requestTag = false;
                        int i = fileIndexList.get(tag);
                        Log.e("ssh","tag =="+tag);
                        tag++;
                        str = needUploadFileList.get(i);
                        if (new File(str).exists()) {
                            Log.d(str, "-----------存在");
                        }
                        Map<String, String> params = new HashMap<>();
                        //params.put("fileContentLength", String.valueOf(fileContentLength));
                        params.put("index", String.valueOf(i));
                        params.put("fileCount", String.valueOf(fileCount));
                        params.put("userName", userName);
                        params.put("org", institution);
                        params.put("insurance", warranty);
                        params.put("projectName", "aa");
                        params.put("longitude", SessionKeeper.getLongitude(UploadActivity.this));
                        params.put("latitude", SessionKeeper.getLatitude(UploadActivity.this));
                        Log.d("paramsMap", params.toString());
                        params.put("code", "abcde");
                        callList.add(HttpHelper.uploadFile(params, str, new ProgressInterface.ProgressRequestListener() {
                            @Override
                            public void onRequestProgress(long bytesWritten, long contentLength, boolean done) {
//                            nowDivisionSendedL = (int) bytesWritten;
//                            progress = sendedLength+(int) ((nowDivisionSendedL / (float) contentLength)*100);
                                synchronized (sendedLength) {
                                    sendedLength.sendedLength += bytesWritten;
                                    mHandler.sendEmptyMessage(MESSAGE_UPLOAD_PROGRESS);
                                }
//                                mHandler.sendEmptyMessage(MESSAGE_UPLOAD_PROGRESS);
                                //progressMsg.arg1 = (int) bytesWritten;
                                //mHandler.sendMessage(progressMsg);
                                if (done) {
                                    if (sendedLength.sendedLength >= fileContentLength) {
                                        mHandler.sendEmptyMessage(MESSAGE_FINISH_UPLOAD);
                                    }
                                }
                            }
                        }, new Callback<TempBean>() {
                            @Override
                            public void onResponse(Call<TempBean> call, Response<TempBean> response) {
                                try {
                                    if (response.body().isSuccess()) {
                                        requestTag = true;
                                        android.util.Log.d("上传回传信息", response.body().getMsg());
                                        //mHandler.sendEmptyMessage(MESSAGE_UPLOAD_PROGRESS);
                                    }
                                } catch (NullPointerException e) {
                                    e.printStackTrace();
                                    mHandler.sendEmptyMessage(MESSAGE_FINISH_UPLOAD);
                                }
                            }

                            @Override
                            public void onFailure(Call<TempBean> call, Throwable t) {
                                t.printStackTrace();
                                if (!call.isCanceled()) {
                                    mHandler.sendEmptyMessage(MESSAGE_UPLOAD_FAILED);
                                }
                            }
                        }));
                    }
                }
            }
        };
    }

    /**
     * 删除分片的文件
     */
    private void deleteDivisionFile() {
        List<String> divisionFileUrl = fileHelper.getUrlList();
        for (int i=0;i<divisionFileUrl.size();i++) {
            String tempUrl = divisionFileUrl.get(i);
            deleteDivisionFile(tempUrl);
        }
    }

    /**
     * 删除文件
     *
     * @param url
     */
    private void deleteDivisionFile(String url) {
        File file = new File(url);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 获取剩余未上传文件列表
     */
    private void getFileList() {
        Log.d("jinru", "---------");
        interrupterTag.itpt = false;
        HttpHelper.getFileList(SessionKeeper.getWarranty(this), new Callback<GetFileListBean>() {
            @Override
            public void onResponse(Call<GetFileListBean> call, Response<GetFileListBean> response) {
                if (response.body().getStatus() == 200) {
                    //List<Integer> list = new ArrayList<Integer>();
                    notUploadFileList.clear();
                    for (int i = 0; i < fileCount; i++) notUploadFileList.add(i);
                    for (Integer i : response.body().getData()) {
                        notUploadFileList.remove(i);
                    }
                    if (notUploadFileList.size() == 0) {
                        mHandler.sendEmptyMessage(MESSAGE_NO_FILE_UPLOAD);
                    } else if (!interrupterTag.itpt) {
                        mHandler.sendEmptyMessage(MESSAGE_GET_SUCCESS);
                    }
                } else if (response.body().getStatus() == 404) {
                    notUploadFileList.clear();
                    for (int i = 0; i < fileCount; i++) notUploadFileList.add(i);
                    mHandler.sendEmptyMessage(MESSAGE_GET_SUCCESS);
                }
            }

            @Override
            public void onFailure(Call<GetFileListBean> call, Throwable t) {

            }
        });
    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_START_UPLOAD:
                    Log.d("上传计时------开始>", "-----------------------------");
                    break;
                case MESSAGE_FINISH_UPLOAD:
                    Log.d("上传计时------结束>", "-----------------------------");
                    isRequesting = false;
                    Toast.makeText(UploadActivity.this, "上传完成", Toast.LENGTH_SHORT).show();
                    stop.setVisibility(View.GONE);
                    cancel.setVisibility(View.GONE);
                    complete.setVisibility(View.VISIBLE);
                    isUploadComplete = true;
                    upload_tag.setText("上传完成！");
                    //deleteDivisionFile();
                    break;
                case MESSAGE_UPLOAD_FAILED:
                    //interupt = true;
                    tag_isStop = false;
                    stop.setText("继续");
                    cancelUpload();
                    isRequesting = false;
                    Toast.makeText(UploadActivity.this, "上传失败 请重试", Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_UPLOAD_PROGRESS:
                    progressBar.setProgress((int) (((float) sendedLength.sendedLength / (float) fileContentLength) * 100));
                    break;
                case MESSAGE_UPLOAD_CANCEL:
                    cancelUpload();
                    //clearThread();
                    tag_isStop = false;
                    stop.setText("开始");
                    //interupt = true;
                    isRequesting = false;
                    Toast.makeText(UploadActivity.this, "已取消上传", Toast.LENGTH_SHORT).show();
                    deleteDivisionFile();
               /*     Intent intent = new Intent(UploadActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);*/
                    break;
                case MESSAGE_GET_SUCCESS:
                    continueUpload();
                    break;
                case MESSAGE_NO_FILE_UPLOAD:
                    Toast.makeText(UploadActivity.this, "没有文件需要继续上传", Toast.LENGTH_SHORT).show();
                    tag_isStop = false;
                    stop.setText("继续");
                    break;
            }
        }
    };

    /**
     * 继续上传
     */
    private void continueUpload() {
//        try {
//            fileHelper.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        deleteDivisionFile();
        startIndex = -1;
        Thread t = new Thread(getUploadAsyncTask());
        t.start();
        threadPool.add(t);
//        if (uploadThread != null) {
//            Log.d("进入", "-------");
//            if (uploadThread.isAlive()) {
//                uploadThread.interrupt();
//            }
//            uploadThread.run();
//        } else {
//            Log.d("进入1", "-------");
//            uploadThread = new Thread(getUploadAsyncTask());
//            uploadThread.start();
//        }
    }

    @Override
    public void onClick(View view) {
        //取消上传
        if(view.getId() == R.id.btn_cancel) {
            if (alertDialog == null) {
                alertDialog = getAlertDialog();
            }
            alertDialog.show();
        }
        //暂停/开始上传
        else if(view.getId() == R.id.btn_stopOrStart) {
            if (!tag_isStop) {
                //继续上传
                getFileList();
                stop.setText("暂停");
                tag_isStop = true;
            } else {
                // 暂停上传
                cancelUpload();
                stop.setText("继续");
                tag_isStop = false;
            }
        }
        else if(view.getId() == R.id.btn_upload_complete){
//                Intent intent = new Intent(UploadActivity.this, StartActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(intent);
//                final Intent intent = getPackageManager().getLaunchIntentForPackage(getPackageName());
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            // TODO: 2017/3/31 解决重复录制
            // TODO: 回start
                /*finish();*/
            finish();
        }
    }

    private void cancelUpload() {
        if (callList != null)
            for (Call call : callList) {
                if (call.isExecuted()) {
                    call.cancel();
                }
            }
        synchronized (interrupterTag) {
            interrupterTag.itpt = true;
        }
        clearThread();
    }

    @Override
    public void onBackPressed() {
        if (!isUploadComplete) {
            Toast.makeText(UploadActivity.this, "上传还未完成", Toast.LENGTH_SHORT).show();
        } else {
            for (Thread thread : threadPool) {
                if (thread.isAlive())
                    thread.interrupt();
            }

            finish();
            super.onBackPressed();
        }
    }

    /**
     * 记录已上传长度内部类
     */
    class SendedLength {
        public int sendedLength;

        public SendedLength(int sendedLength) {
            this.sendedLength = sendedLength;
        }
    }

    @Override
    protected void onStop() {
        cancelUpload();
        deleteDivisionFile();
//        if (myNetReceiver != null) {
//            unregisterReceiver(myNetReceiver);
//        }
        stop.setText("继续");
        tag_isStop = false;
        super.onStop();
    }

    @Override
    protected void onPause() {
        cancelUpload();
        stop.setText("继续");
        tag_isStop = false;
        super.onPause();
    }

    private void clearThread() {
        if (threadPool != null)
            for (Thread thread : threadPool) {
                if (thread.isAlive())
                    thread.interrupt();
            }
    }

    class InterrupterTag {
        public boolean itpt;

        public InterrupterTag() {
            this.itpt = false;
        }
    }

    private NetworkInfo netInfo;
    private ConnectivityManager mConnectivityManager;

    /**
     * 监听网络 网络断开 取消请求
     */
    class NetWorkBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                mConnectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                netInfo = mConnectivityManager.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        /////WiFi网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        /////有线网络

                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        /////////3g网络

                    }
                } else {
                    ////////网络断开
                    Log.d("网络断开", "-----------");
                    Toast.makeText(UploadActivity.this, "网络断开 请检查网络", Toast.LENGTH_SHORT).show();
                    cancelUpload();
                    stop.setText("继续");
                    tag_isStop = false;
                }
            }

        }
    }
}
