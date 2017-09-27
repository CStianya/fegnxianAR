package com.example.zhaoshuang.weixinrecordeddemo.http;

/**
 * Created by wcx on 2017/3/21.
 */

public interface ProgressInterface {
    /**
     * 请求体进度回调接口，用于文件上传进度回调
     */
    public interface ProgressRequestListener {
        void onRequestProgress(long bytesWritten, long contentLength, boolean done);
    }

}
