package com.riskmanagement.http;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import static com.riskmanagement.MyApplication.domainName;

/**
 * Created by Administrator on 2017/4/22.
 */

public class ServiceGenerator {

    private static OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)//设置读取超时时间
            .writeTimeout(15, TimeUnit.SECONDS)//设置写的超时时间
            .readTimeout(10, TimeUnit.SECONDS)//设置连接超时时间
            .build();


//    private static final String HOST = "http://fkar.lczyfz.com/";
    private static final String HOST = domainName+"/";
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(HOST)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(client);


    public static <T> T createService(Class<T> mClass) {
        return builder.build().create(mClass);
    }
}
