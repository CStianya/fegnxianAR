package com.example.zhaoshuang.weixinrecordeddemo.http;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by wcx on 2017/3/21.
 */

public class HttpClientHelper {
    /**
     * 包装OkHttpClient，用于上传文件的回调
     * @param progressListener 进度回调接口
     * @return 包装后的OkHttpClient
     */
    public static OkHttpClient addProgressRequestListener(final ProgressInterface.ProgressRequestListener progressListener){
        OkHttpClient.Builder client = new OkHttpClient.Builder();
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
//包含header、body数据
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        //增加拦截器
        client//拦截log
                .addInterceptor(loggingInterceptor)
                .addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                Request request = original.newBuilder()
                        .method(original.method(), new ProgressRequestBody(original.body(),progressListener))
                        .build();
                return chain.proceed(request);
            }
        });
        client.readTimeout(600, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(600,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(600,TimeUnit.SECONDS);//设置连接超时时间
        return client.build();
    }
}
