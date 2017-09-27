package com.example.zhaoshuang.weixinrecordeddemo.session;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by wcx on 2017/3/27.
 */

public class SessionKeeper {
    protected static final String TAG = SessionKeeper.class.getSimpleName();
    protected static final String UPLOAD_FILE_INDEX = "upload_file_index";
    protected static final String INSTITUTION = "institution";
    protected static final String WARRANTY = "warranty";
    protected static final String USE_NAME = "name";
    protected static final String LONGITUDE = "longitude";
    protected static final String LATITUDE = "latitude";


    public static void setUploadFileIndex(int index, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(TAG, 0).edit();
        editor.putInt(UPLOAD_FILE_INDEX,index);
        editor.apply();
    }

    public static int getUploadFileIndex(Context context){
        return context.getSharedPreferences(TAG,0).getInt(UPLOAD_FILE_INDEX,0);
    }

    public static void setInstitution(String s, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(TAG, 0).edit();
        editor.putString(INSTITUTION,s);
        editor.apply();
    }

    public static String getInstitution(Context context){
        return context.getSharedPreferences(TAG,0).getString(INSTITUTION,null);
    }

    public static void setWarranty(String s, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(TAG, 0).edit();
        editor.putString(WARRANTY,s);
        editor.apply();
    }

    public static String getWarranty(Context context){
        return context.getSharedPreferences(TAG,0).getString(WARRANTY,null);
    }

    public static void setUseName(String s, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(TAG, 0).edit();
        editor.putString(USE_NAME,s);
        editor.apply();
    }

    public static String getUseName(Context context){
        return context.getSharedPreferences(TAG,0).getString(USE_NAME,null);
    }

    public static void setLongitude(String s, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(TAG, 0).edit();
        editor.putString(LONGITUDE,s);
        editor.apply();
    }

    public static String getLongitude(Context context){
        return context.getSharedPreferences(TAG,0).getString(LONGITUDE,"0");
    }

    public static void setLatitude(String s, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(TAG, 0).edit();
        editor.putString(LATITUDE,s);
        editor.apply();
    }

    public static String getLatitude(Context context){
        return context.getSharedPreferences(TAG,0).getString(LATITUDE,"0");
    }

    //清除session
    public static boolean clearSession(Context context) {
        SharedPreferences sp = context.getSharedPreferences(TAG, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(LONGITUDE);
        editor.remove(LATITUDE);
        //editor.clear();
        return editor.commit();
    }

}
