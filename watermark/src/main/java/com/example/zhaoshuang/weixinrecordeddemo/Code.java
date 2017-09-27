package com.example.zhaoshuang.weixinrecordeddemo;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.StringReader;
import java.util.Calendar;
import java.util.Random;


/**
 * Created by Administrator on 2017/3/20.
 */

public class Code {
    private String path="/sdcard/WeiXinRecordedDemo/";
    private String txtName="video.txt";
    public Code(String text){
        writeTxtToFile(text);
    }


    /*
    * 加密
    * @throws Exception
    * */

    public String encode(String txtName,String sourceFileName,String objectFileName)throws Exception {
        //获取密钥文件
       // BufferedInputStream bis = new BufferedInputStream(new FileInputStream(new File(path+"/"+txtName)));
       // byte[] passwd=new byte[bis.available()];
        byte[] passwd= "abcde".getBytes();
        //bis.read(passwd);
      //  bis.close();
         String timeStart;
         String timeEnd;
        timeStart = Calendar.getInstance().getTime().toString();
        Log.e("timeStart",timeStart);
        Log.e("ssfile",path+"/"+sourceFileName);
        Log.e("eenfile",path+"/"+objectFileName+".mp4");
        //获取原视频文件
        BufferedInputStream bisOld = new BufferedInputStream(new FileInputStream(new File(path+"/"+sourceFileName+".mp4")));
        Log.e("writepwd","writepwd");
        //输出加密后的流文件
        BufferedOutputStream bosNew = new BufferedOutputStream(new FileOutputStream(new File(path+"/"+objectFileName+".mp4")));
        //写入密钥
        bosNew.write(passwd);
        byte[] buffer=new byte[1024];
        int len=0;
        Log.e("ggggggg",String.valueOf(buffer));
        //加密文件
        while((len=bisOld.read(buffer))>0){
            bosNew.write(buffer, 0, len);
        }
        bosNew.flush();
        bosNew.close();
        bisOld.close();
        timeEnd=Calendar.getInstance().getTime().toString();
        Log.e("timeEnd",timeEnd);
        Log.e("startAndend",timeStart+"-->"+timeEnd);
        return String.valueOf(passwd);
    }
    /**
     * 解密
     * @throws Exception
     */
    public void decode(String txtName,String sourceFileName,String objectFileName)throws Exception {

        byte[] passwd= "abcde".getBytes();

        //获取加密文件
        BufferedInputStream bisOld = new BufferedInputStream(new FileInputStream(new File(path+"/"+sourceFileName+".mp4")));
        //输出解密后流文件
        BufferedOutputStream bosNew = new BufferedOutputStream(new FileOutputStream(new File(path+"/"+objectFileName+".mp4")));
        //获取密钥
        bisOld.read(passwd);
        passwd=null;

        byte[] buffer=new byte[1024];
        int len=0;

        while((len=bisOld.read(buffer))>0){
            bosNew.write(buffer, 0, len);
        }

        bosNew.flush();
        bosNew.close();

        bisOld.close();
    }
    // 将字符串写入到文本文件中
    public void writeTxtToFile(String strcontent) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath();
        // 每次写入时，都换行写
        String strContent = strcontent + "\r\n";
        try {
            File file = new File(path+txtName);
                Log.d("TestFile", "Create the file:" + path);
                file.getParentFile().mkdirs();
                file.createNewFile();
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
            raf.seek(file.length());
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

    // 生成文件
    public File makeFilePath() {
        File file = null;
        makeRootDirectory();
        try {
            file = new File(path + txtName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    // 生成文件夹
    public  void makeRootDirectory() {
        File file = null;
        try {
            file = new File(path);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }


}
