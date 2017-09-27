package com.riskmanagement.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.riskmanagement.R;
import com.riskmanagement.activity.ReviewWorkActivity;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.UpProject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.riskmanagement.MyApplication.MPcoordinates;
import static com.riskmanagement.MyApplication.MPdeleteId;
import static com.riskmanagement.MyApplication.mLocation;


public class RiskIdentificationFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemSelectedListener {
    private Spinner sp_risk_select;
    private String[] items;
    private ArrayAdapter<String> mAdapter;
    private int statueID=2;
    private Button btn_risk_next;
    private SharedPreferences sharedPreferences;
    private String id;
    String[] filePaths;
    private String riskLevel ="1";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_risk, container, false);
        initView(view);
        return view;
    }
    private void initView(View view){
        sharedPreferences=getContext().getSharedPreferences("myData",MODE_PRIVATE);
        id=sharedPreferences.getString("id","");
        statueID=getArguments().getInt("statuNum");
        sp_risk_select=(Spinner)view.findViewById(R.id.sp_risk_select);
        btn_risk_next=(Button)view.findViewById(R.id.btn_risk_next);
        if(statueID==1)btn_risk_next.setVisibility(view.GONE);
        Log.d("myID",String.valueOf(statueID));
        items=new String[]{"风险识别1","风险识别2","风险识别3","风险识别4"};
        mAdapter=new ArrayAdapter<String>(getContext(),android.R.layout.simple_spinner_dropdown_item,items);
        mAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_risk_select.setAdapter(mAdapter);
        sp_risk_select.setOnItemSelectedListener(this);
        btn_risk_next.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        List<File> files = new ArrayList<>();
        File f=new File("/sdcard/Image/");
        String[] f1=f.list();
        filePaths = new String[f1.length];
        for(int i = 0;i < f1.length ; i++)
        {
            files.add(new File("/sdcard/Image/"+ f1[i]));
            filePaths[i] = "/sdcard/Image/"+ f1[i];
        }
//list 输出的是文件名	没有c:\\ 开头的 如：AUTOEXEC.BAT
//listfiles 输出的是路劲目录 如：c:\AUTOEXEC.BAT
//listfiles 调用getName()时两者达到的效果就相同了

        List<MultipartBody.Part> listpart = files2Parts("file",filePaths);



//        Call<UpProject> call = HttpHelper.requestUploadWork(params, new Callback<UpProject>() {
//            @Override
//            public void onResponse(Call<UpProject> call, Response<UpProject> response) {
//                Log.e("ssh","========成功"+response.toString());
////            Log.e("ssh","========"+response.body().toString());
//                startActivity(new Intent(getActivity(), ReviewWorkActivity.class));
//            }
//
//            @Override
//            public void onFailure(Call<UpProject> call, Throwable t) {
//                Log.e("ssh","========失败");
//            }
//        });


/*参数名	必选	类型	说明
        id	是	string	项目id
        coordinates	是	string	坐标
        coordDesc	是	string	坐标描述
        riskLevel	是	string	风险级别 1开始
        delPics	是	string	删除图片id用逗号隔开
        file	否	File	图片*/
        HashMap<String, String> params = new HashMap<>();
        params.put("id", id);
        params.put("coordinates",MPcoordinates);
        params.put("coordDesc",mLocation);
        params.put("riskLevel",riskLevel);
        params.put("delPics", MPdeleteId);
        Log.e("ssh","======================");
        Log.e("ssh",id);
        Log.e("ssh",MPcoordinates);
        Log.e("ssh",mLocation);
        Log.e("ssh",riskLevel);
        Log.e("ssh"," = "+MPdeleteId);
        Log.e("ssh","======================");
        Call<UpProject> jsonModelCall= HttpHelper.UploadFiles(files,params, new Callback<UpProject>() {
            @Override
            public void onResponse(Call<UpProject> call, Response<UpProject> response) {

                try{
                    if("yes".equals(String.valueOf(response.body().getSuccess()))){
                        mHandler.sendEmptyMessage(10);
                    }else{
                        mHandler.sendEmptyMessage(20);
                    }
                }catch (Exception e){
                    mHandler.sendEmptyMessage(20);
                }

            }
            @Override
            public void onFailure(Call<UpProject> call, Throwable t) {
                Log.e("ssh",t.toString());
            }
        });


    }

    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    startActivity(new Intent(getActivity(), ReviewWorkActivity.class));
                    getActivity().finish();
                    break;
                case 20:
                    Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
                    break;

            }
        }
    };

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

        riskLevel = ""+(i+1);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    /**
     * 将文件路径数组封装为{@link List<MultipartBody.Part>}
     * @param key 对应请求正文中name的值。目前服务器给出的接口中，所有图片文件使用<br>
     * 同一个name值，实际情况中有可能需要多个
     * @param filePaths 文件路径数组
     */
    public static List<MultipartBody.Part> files2Parts(String key,
                                                       String[] filePaths) {
        List<MultipartBody.Part> parts = new ArrayList<>(filePaths.length);
        for (String filePath : filePaths) {
            File file = new File(filePath);
            // 根据类型及File对象创建RequestBody（okhttp的类）
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), file);
            // 将RequestBody封装成MultipartBody.Part类型（同样是okhttp的）
            MultipartBody.Part part = MultipartBody.Part.
                    createFormData(key, file.getName(), requestBody);
            // 添加进集合
            parts.add(part);
        }
        return parts;
    }

    /**
     * 其实也是将File封装成RequestBody，然后再封装成Part，<br>
     * 不同的是使用MultipartBody.Builder来构建MultipartBody
     * @param key 同上
     * @param filePaths 同上
     * @param imageType 同上
     */
    public static MultipartBody filesToMultipartBody(String key,
                                                     String[] filePaths,
                                                     MediaType imageType) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (String filePath : filePaths) {
            File file = new File(filePath);
            RequestBody requestBody = RequestBody.create(imageType, file);
            builder.addFormDataPart(key, file.getName(), requestBody);
        }
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }
}




