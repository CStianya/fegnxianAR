package com.riskmanagement.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.riskmanagement.R;
import com.riskmanagement.adapter.GridAdapter;
import com.riskmanagement.http.HttpHelper;
import com.riskmanagement.http.bean.GetArData;
import static com.riskmanagement.MyApplication.MPdeleteId;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


public class LiveSceneFragment extends Fragment implements View.OnClickListener,AdapterView.OnItemClickListener {
    private GridView gv_liveScene_content;
    private Button btn_ls_take;
    private Button btn_ls_editor;
    // 返回码
    private static final int CODE = 1;
    private GridAdapter gridAdapter;
    private int position=0;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private String id;
    private ArrayList<GetArData.DataBean.ListBeana> getArDataList=new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_livescene, container, false);
        initView(view);
        initWidget();
        initData();
        return view;
    }

    private void initData() {
        HashMap<String,String> params=new HashMap<>();
        params.put("id",id);
        Log.d("ssh", id);
        final Call<GetArData> getArDataCall= HttpHelper.getArData(params, new Callback<GetArData>() {
            @Override
            public void onResponse(Call<GetArData> call, Response<GetArData> response) {
                getArDataList.clear();
                gridAdapter.getList().clear();
                getArDataList=(ArrayList<GetArData.DataBean.ListBeana>)response.body().getData().getList();
                gridAdapter.getList().addAll(getArDataList);
                gridAdapter.notifyDataSetChanged();
                Log.d("isSuccess",String.valueOf(response.isSuccessful()));
            }
            @Override
            public void onFailure(Call<GetArData> call, Throwable t) {
                Toast.makeText(getContext(),"网络访问异常",Toast.LENGTH_LONG);
            }
        });
    }

    private void initView(View view) {
        sharedPreferences=getContext().getSharedPreferences("myData",MODE_PRIVATE);
        id=sharedPreferences.getString("id","");
        gv_liveScene_content = (GridView) view.findViewById(R.id.gv_liveScene_content);
        btn_ls_editor = (Button) view.findViewById(R.id.btn_ls_editor);
        btn_ls_take = (Button) view.findViewById(R.id.btn_ls_take);
//        photoList = new ArrayList<>();
        gridAdapter = new GridAdapter(getArDataList, getContext());
        gv_liveScene_content.setAdapter(gridAdapter);
        RecursionDeleteFile(new File("/sdcard/Image/"));
        File file = new File("/sdcard/Image/");
        file.mkdirs();// 创建文件夹
        MPdeleteId = "";
    }

    private void initWidget() {
        btn_ls_take.setOnClickListener(this);
        btn_ls_editor.setOnClickListener(this);
        gv_liveScene_content.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_ls_editor:
                int j=0;
                for (int i = 0; i < gv_liveScene_content.getChildCount(); i++) {
                    LinearLayout layout = (LinearLayout) gv_liveScene_content.getChildAt(i);// 获得子item的layout
                    CheckBox checkBox = (CheckBox) layout.findViewById(R.id.cb_image_selected);// 从layout中获得控件,根据其id
                    if(checkBox.isChecked()){
                        Log.d("myIndex",String.valueOf(i-j)+"count:"+String.valueOf(gv_liveScene_content.getChildCount()));
//                        getArDataList.remove(i-j);
                        MPdeleteId = getArDataList.get(i-j).getId()+","+ MPdeleteId;
                        gridAdapter.getList().remove(i-j);
                        j++;
                        checkBox.setChecked(false);
                    }
                    Log.d("mySize",String.valueOf(getArDataList.size()));
                }

                gridAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_ls_take:
                Log.e("ssh","之前 = "+getArDataList.size());
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE); //指定拍照
                startActivityForResult(intent, CODE);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);
        // 判断是否返回值
        if (resultCode == RESULT_OK) {
            // 判断返回值是否正确
            if (requestCode == CODE) {

                // 获取图片
                Bundle bundle = data.getExtras();
                // 转换图片的二进制流
                Bitmap bitmap = (Bitmap) bundle.get("data");
                // 设置图片
//                photoList.add(bitmap);

//                IV_a.setImageBitmap(bitmap);

                FileOutputStream b = null;
                Date date = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
                String str = sdf.format(date);
                String fileName = "/sdcard/Image/"+str+".jpg";

                try {
                    b = new FileOutputStream(fileName);
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
                    gridAdapter.getList().add(0,new GetArData.DataBean.ListBeana(fileName,"10"));
                    handler.sendEmptyMessage(10);

                    Log.e("ssh","现在 = "+getArDataList.size());
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        b.flush();
                        b.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

    android.os.Handler handler = new android.os.Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10:
                    gridAdapter.notifyDataSetChanged();
                    break;
            }
        }
    };
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }
    /**
     * 递归删除文件和文件夹
     * @param file    要删除的根目录
     */
    public void RecursionDeleteFile(File file){
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

}


