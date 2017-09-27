package com.riskmanagement.http;


import com.riskmanagement.http.bean.ArHomeData;
import com.riskmanagement.http.bean.ForgetPassword;
import com.riskmanagement.http.bean.GetArData;
import com.riskmanagement.http.bean.Login;
import com.riskmanagement.http.bean.Register;
import com.riskmanagement.http.bean.UpProject;
import com.riskmanagement.http.bean.UserList;
import com.riskmanagement.http.bean.UserMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by Administrator on 2017/4/22.
 */

public class HttpHelper {
    public static Call<UserList> getUserList(Callback<UserList> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<UserList> call=apiService.getUserList();
        call.enqueue(callback);
        return call;
    }
    public static Call<ArHomeData> getArHomeData(HashMap<String, String> params, Callback<ArHomeData> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<ArHomeData> call=apiService.getArHomeData(params);
        call.enqueue(callback);
        return call;
    }
    public static Call<GetArData> getArData(HashMap<String, String> params,Callback<GetArData> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<GetArData> call=apiService.getArData(params);
        call.enqueue(callback);
        return call;
    }
    public static Call<ForgetPassword> forgetPassword(HashMap<String, String> params, Callback<ForgetPassword> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<ForgetPassword> call=apiService.forgetPassword(params);
        call.enqueue(callback);
        return call;
    }
    public static Call<Login> login(HashMap<String, String> params, Callback<Login> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<Login> call=apiService.login(params);
        call.enqueue(callback);
        return call;
    }
    public static Call<Register> register(HashMap<String, String> params, Callback<Register> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<Register> call=apiService.register(params);
        call.enqueue(callback);
        return call;
    }
    public static Call<UpProject> upProject(HashMap<String, String> params, Callback<UpProject> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<UpProject> call=apiService.upProject(params);
        call.enqueue(callback);
        return call;
    }


    public static Call<UpProject> UploadFiles(List<File> files, HashMap<String,String> params, Callback<UpProject> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        Call<UpProject> call = apiService.UploadFiles(params, parts);
        call.enqueue(callback);
        return call;
    }

    /**
     * 查询个人资料
     * @param callback
     * @return
     */
    public static Call<UserMessage> getUserMessage(HashMap<String, String> params,Callback<UserMessage> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<UserMessage> call=apiService.getUserMessage(params);
        call.enqueue(callback);
        return call;
    }
    /**
     * 编辑头像
     * @param callback
     * @return
     */
    public static Call<UpProject> editHeadImg(List<File> files, HashMap<String, String> params, Callback<UpProject> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        List<MultipartBody.Part> parts = new ArrayList<>(files.size());
        for (File file : files) {
            // TODO: 16-4-2  这里为了简单起见，没有判断file的类型
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
            MultipartBody.Part part = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
            parts.add(part);
        }
        Call<UpProject> call=apiService.editHeadImg(params,parts);
        call.enqueue(callback);
        return call;
    }
    /**
     * 修改昵称
     * @param callback
     * @return
     */
    public static Call<Login> editNickname(HashMap<String, String> params, Callback<Login> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<Login> call=apiService.editNickname(params);
        call.enqueue(callback);
        return call;
    }
    /**
     * 编辑性别
     * @param callback
     * @return
     */
    public static Call<Login> editSex(HashMap<String, String> params, Callback<Login> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<Login> call=apiService.editSex(params);
        call.enqueue(callback);
        return call;
    }
    /**
     * 修改密码
     * @param callback
     * @return
     */
    public static Call<Login> editPassword(HashMap<String, String> params, Callback<Login> callback) {
        APIService apiService = ServiceGenerator.createService(APIService.class);
        Call<Login> call=apiService.editPassword(params);
        call.enqueue(callback);
        return call;
    }
}


