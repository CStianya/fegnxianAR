package com.riskmanagement.http;


import com.riskmanagement.http.bean.ArHomeData;
import com.riskmanagement.http.bean.ForgetPassword;
import com.riskmanagement.http.bean.GetArData;
import com.riskmanagement.http.bean.Login;
import com.riskmanagement.http.bean.Register;
import com.riskmanagement.http.bean.UpProject;
import com.riskmanagement.http.bean.UserList;
import com.riskmanagement.http.bean.UserMessage;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;


/**
 * Created by Administrator on 2017/4/22.
 */

public interface APIService {
    @POST("api/user/userList")
    Call<UserList> getUserList() ;
    @Multipart
    @POST("api/project/arHomeData")
    Call<ArHomeData> getArHomeData(@PartMap Map<String,String > params) ;

    @Multipart
    @POST("api/project/getArData")
    Call<GetArData> getArData(@PartMap Map<String,String > params) ;
    @Multipart
    @POST("api/user/login")
    Call<Login> login(@PartMap Map<String,String > params) ;
    @Multipart
    @POST("api/user/forgotPassword")
    Call<ForgetPassword> forgetPassword(@PartMap Map<String,String > params) ;
    @Multipart
    @POST("api/user/register")
    Call<Register> register(@PartMap Map<String,String > params) ;
    /**保存信息交底*/
    @Multipart
    @POST("api/project/upProject")
    Call<UpProject> upProject(@PartMap Map<String,String > params) ;


    /**
     * 保存复测信息
     * @param params
     * @param parts
     * @return
     */
    @Multipart
    @POST("api/project/saveArBgProject")
    Call<UpProject> UploadFiles(@PartMap Map<String,String>params,@Part() List<MultipartBody.Part> parts);

    /**
     * 查询个人资料*/
    @Multipart
    @POST("api/user/userData")
    Call<UserMessage> getUserMessage(@PartMap Map<String,String > params) ;

    /**
     * 编辑头像
     * @param params
     * @return
     */
    @Multipart
    @POST("api/user/editHeadImg")
    Call<UpProject> editHeadImg(@PartMap Map<String,String>params,@Part() List<MultipartBody.Part> parts) ;

    /**
     * 修改昵称
     * @param params
     * @return
     */
    @Multipart
    @POST("api/user/editNickname")
    Call<Login> editNickname(@PartMap Map<String,String > params) ;

    /**
     * 编辑性别
     * @param params
     * @return
     */
    @Multipart
    @POST("api/user/editSex")
    Call<Login> editSex(@PartMap Map<String,String > params) ;

    /**
     *修改密码
     * @param params
     * @return
     */
    @Multipart
    @POST("api/user/editPassword")
    Call<Login> editPassword(@PartMap Map<String,String > params) ;


    @Multipart
    @POST("api/user/login")
    Observable<Login> logina(@PartMap Map<String,String > params) ;
}