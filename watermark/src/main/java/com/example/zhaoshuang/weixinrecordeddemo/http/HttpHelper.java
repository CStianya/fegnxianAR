package com.example.zhaoshuang.weixinrecordeddemo.http;

import android.util.Log;

import com.example.zhaoshuang.weixinrecordeddemo.http.bean.GetFileListBean;
import com.example.zhaoshuang.weixinrecordeddemo.http.bean.TempBean;

import java.io.File;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * Created by wcx on 2017/3/21.
 */

public class HttpHelper {

    public static void getFileList(String fileName, Callback<GetFileListBean> callback) {
        APIService service = ServiceGenerator.createService(APIService.class);
        Call<GetFileListBean> call = service.getFileList(fileName);
        call.enqueue(callback);
    }

//    public static Call<ResponeBean> uploadFile(Map<String,String> params, String fileUri, ProgressInterface.ProgressRequestListener listener, Callback<ResponeBean> callback) {
//        APIService service = ServiceGenerator.createService(APIService.class);
//        final File file = new File(fileUri);
//        Log.d("文件是否存在", String.valueOf(file.exists()));
//        RequestBody requestFile =
//                RequestBody.create(MediaType.parse("multipart/form-data"), file);
////将requestFile封装成ProgressRequestBody传入
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("file", file.getName(), new ProgressRequestBody(requestFile, listener));//this是在当前类实现了ProgressRequestListener接口
//        Call<ResponeBean> call = service.uploadFile(params, body);
//        call.enqueue(callback);
//        return call;
//    }

    public static Call<TempBean> uploadFile(Map<String,String> params, String fileUri, ProgressInterface.ProgressRequestListener listener, Callback<TempBean> callback) {
        APIService service = ServiceGenerator.createService(APIService.class);
        final File file = new File(fileUri);
        Log.d("文件是否存在", String.valueOf(file.exists()));
        RequestBody requestFile =
                RequestBody.create(MediaType.parse("multipart/form-data"), file);
//将requestFile封装成ProgressRequestBody传入
        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), new ProgressRequestBody(requestFile, listener));//this是在当前类实现了ProgressRequestListener接口
        Call<TempBean> call = service.uploadFile(params, body);
        call.enqueue(callback);
        return call;
    }

//    public static void continueUploadFile(final Context context, String passwd, String fileUri, ProgressInterface.ProgressRequestListener listener, Callback<ResponeBean> callback) {
//        APIService service = ServiceGenerator.createService(APIService.class);
//        final File file = new File(fileUri);
//        RequestBody requestFile = new RequestBody() {
//            @Override
//            public MediaType contentType() {
//                return MediaType.parse("multipart/form-data");
//            }
//
//            @Override
//            public long contentLength() {
//                return file.length();
//            }
//
//            @Override
//            public void writeTo(BufferedSink sink) throws IOException {
//                FileInputStream inputStream = new FileInputStream(file);
//                try {
//                    byte[] buffer = new byte[1024];
//                    int index = SessionKeeper.getUploadFileIndex(context);
//                    int len;
//                    while ((len = inputStream.read(buffer)) != -1) {
//                        sink.write(buffer, index, len);
//                        index += len;
//                    }
//                } finally {
//                    //Util.closeQuietly(source);
//                    inputStream.close();
//                }
//            }
//        };
//        // RequestBody.create(MediaType.parse("multipart/form-data"), file);
////将requestFile封装成ProgressRequestBody传入
//        MultipartBody.Part body =
//                MultipartBody.Part.createFormData("file", file.getName(), new ProgressRequestBody(requestFile, listener));//this是在当前类实现了ProgressRequestListener接口
//        Call<ResponeBean> call = service.uploadFile(passwd, body);
//        call.enqueue(callback);
//    }
}
