package com.example.zhaoshuang.weixinrecordeddemo.util;

import android.content.Context;
import android.util.Log;

import com.example.zhaoshuang.weixinrecordeddemo.MyApplication;
import com.example.zhaoshuang.weixinrecordeddemo.session.SessionKeeper;
import com.yixia.camera.MediaRecorderNative;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wcx on 2017/3/28.
 */

public class FileDivisionHelper {
    private static final int divisionSize = 1024 * 1024;
    private RandomAccessFile inputStream;
    private FileOutputStream outputStream;
    private File file;
    private File outFile;
    private int lastLen;
    private int divisionFileCount;
    private List<String> urlList = new ArrayList<>();
    private String warranty;


    public FileDivisionHelper(String fileUrl, Context context) {
        try {
            this.file = new File(fileUrl);
            this.inputStream = new RandomAccessFile(this.file, "r");
            this.divisionFileCount = getFileSize() / divisionSize + 1;
            this.warranty = SessionKeeper.getWarranty(context);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    public List<String> getUrlList() {
        return urlList;
    }

    /**
     * 获取总分片数
     *
     * @return
     */
    public int getDivisionFileCount() {
        return divisionFileCount;
    }

    /**
     * 获得文件大小
     */
    public int getFileSize() {
        return (int) file.length();
    }

    /**
     * 获取文件名称
     */
    public String getFileName() {
        return file.getName();
    }

    /**
     * 分割文件
     */
    public String divisiveFile(int index) throws IOException {
        StringBuffer stringBuffer = new StringBuffer(getFileName());
        if(index == 0)
        Log.e("ssh",stringBuffer.toString());
        //去掉后缀
        stringBuffer.delete(stringBuffer.length()-MediaRecorderNative.VIDEO_SUFFIX.length(),stringBuffer.length());
        String outFileUrl = MyApplication.VIDEO_PATH + stringBuffer.toString()+ warranty + String.valueOf(index) + MediaRecorderNative.VIDEO_SUFFIX;
        Log.e("ssh","outFileUrl=="+outFileUrl);
        outFile = new File(outFileUrl);
        if(outFile.exists()){
            //outFile.delete();
            boolean temp = outFile.createNewFile();
            Log.d("create new file","--------------"+String.valueOf(temp));
        }
        outputStream = new FileOutputStream(outFile);
        //偏移量
        int offset = index * divisionSize;
        //转写buffer
        byte[] buffer = new byte[divisionSize];
        Log.d("fileLength--->", String.valueOf(getFileSize()));
        //判断是否是最后一段
        lastLen = (int) (file.length() - offset);
        if (lastLen < 0) {
            return null;
        }
        Log.d("SeekOffset",String.valueOf(offset));
        inputStream.seek(offset);
        if (lastLen > divisionSize) {
            inputStream.readFully(buffer);
            Log.d("Buffer的长度----",String.valueOf(buffer.length));
            outputStream.write(buffer);
        } else {
            byte[] b = new byte[lastLen];
            inputStream.readFully(b);
            outputStream.write(b);
            Log.d("文件长度",String.valueOf(lastLen));
        }
        urlList.add(outFileUrl);
        return outFileUrl;
    }

    public void close() throws IOException {
        if (outputStream != null) {
            outputStream.close();
        }
        if (inputStream != null) {
            inputStream.close();
        }
    }
}
