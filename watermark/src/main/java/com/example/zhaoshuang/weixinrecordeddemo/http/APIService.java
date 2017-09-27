package com.example.zhaoshuang.weixinrecordeddemo.http;

import com.example.zhaoshuang.weixinrecordeddemo.http.bean.GetFileListBean;
import com.example.zhaoshuang.weixinrecordeddemo.http.bean.TempBean;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by wcx on 2017/3/21.
 */

public interface APIService {

//    @Multipart
//    @POST("mobileFileUpload")
//    Call<ResponeBean> uploadFile(@Part("code") String code,@Part MultipartBody.Part file);

    @Multipart
    @POST("api/mobile/mobileFile/batchUploadFile")
    Call<TempBean> uploadFile(@PartMap Map<String,String> params, @Part MultipartBody.Part file);

    @GET("getChunk")
    Call<GetFileListBean> getFileList(@Query("filename") String fileName);
}
