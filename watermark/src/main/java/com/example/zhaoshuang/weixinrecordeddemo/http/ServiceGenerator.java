package com.example.zhaoshuang.weixinrecordeddemo.http;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.protobuf.ProtoConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by wcx on 2017/3/21.
 */

public class ServiceGenerator {
//    private static final String HOST = "http://139.196.48.196:8787/f/mobile/mobileFile/";
    private static final String HOST = "http://139.196.48.196:7979/";
    //private static final String HOST = "http://192.168.1.122:8080/";

    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(ProtoConverterFactory.create())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create());


    public static <T> T createService(Class<T> tClass){
        return builder.build().create(tClass);
    }


    /**
     * 创建带响应进度(下载进度)回调的service
     */
    public static <T> T createResponseService(Class<T> tClass, ProgressInterface.ProgressRequestListener listener){
        return builder
                .client(HttpClientHelper.addProgressRequestListener(listener))
                .build()
                .create(tClass);
    }

}
