package com.example.zhaoshuang.weixinrecordeddemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zhaoshuang.weixinrecordeddemo.session.SessionKeeper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class StartActivity extends BaseActivity implements View.OnClickListener {
    Button btn_commit;
    EditText edt_institution;
    EditText edt_warranty;
    EditText edt_name;
    TextView tv_version;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_start);
        initView();
        //清除经纬度的记录
        SessionKeeper.clearSession(this);
    }

    private void initView() {
        btn_commit = (Button) findViewById(R.id.btn_commit);
        edt_institution = (EditText) findViewById(R.id.edt_institution);
        edt_name = (EditText) findViewById(R.id.edt_name);
        edt_warranty = (EditText) findViewById(R.id.edt_warranty);
        tv_version = (TextView) findViewById(R.id.tv_version);
        //固定机构号
        edt_institution.setText("9f28cacfb6e54c2798587b00bbbffaa5");
        createWarranty();
        btn_commit.setOnClickListener(this);

        tv_version.setText(getAppInfo());
    }

    @Override
    protected void onResume() {
        super.onResume();
        createWarranty();
    }

    private void createWarranty() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String time = formatter.format(curDate);
        Log.e("curTime", time);
       /* String time = String.valueOf(Calendar.getInstance().getTime().getTime());
        Log.d("时间",time);*/
        StringBuffer sb = new StringBuffer();
        sb.append(time);
        //生成随机字符串
        int length = 5;//length表示生成字符串的长度
        String base = "0123456789";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }
        if (edt_warranty!=null){
            edt_warranty.setText(sb.toString());
        }
    }

    @Override
    public void onClick(View view) {
            if(view.getId() == R.id.btn_commit){
                if (edt_institution.getText() == null || edt_institution.getText().toString().equals("")) {
                    Toast.makeText(StartActivity.this, "机构号不可为空", Toast.LENGTH_SHORT).show();
                    return;
                } else if (edt_name.getText() == null || edt_name.getText().toString().equals("")) {
                    Toast.makeText(StartActivity.this, "姓名不可为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                //缓存3个数据在text
                SessionKeeper.setInstitution(edt_institution.getText().toString(), this);
                SessionKeeper.setWarranty(edt_warranty.getText().toString(), this);
                SessionKeeper.setUseName(edt_name.getText().toString(), this);
                Intent intent = new Intent(StartActivity.this, MActivity.class);
                startActivity(intent);
                finish();

        }
    }

//    @Override
//    public void onBackPressed() {
//
//        //按返回键回到桌面
////        Intent home = new Intent(Intent.ACTION_MAIN);
////        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////        home.addCategory(Intent.CATEGORY_HOME);
////        startActivity(home);
//    }

    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return versionName;
        } catch (Exception e) {
        }
        return null;
    }
}
