package com.riskmanagement.utils;


import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by xiaozhi on 2016/5/20.
 */

public class Boolean_Mobile {
    public static boolean isMobileNO(String mobiles){
        Pattern p = Pattern.compile("^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    public static boolean isEmail(String email){
        String str="^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isName(String name){

        Pattern p = Pattern.compile("^[A-Za-z|@|.|0-9]{4,30}$");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public static boolean isPassWord(String name){

     //   Pattern p = Pattern.compile("^[A-Za-z|@|.|0-9]{6,20}$");
        Pattern p = Pattern.compile("((?=.*[a-z])(?=.*\\d)|(?=[a-z])(?=.*[#@!~%^&*])|(?=.*\\d)(?=.*[#@!~%^&*]))[a-z\\d#@!~%^&*]{6,20}");
        Matcher m = p.matcher(name);
        return m.matches();
    }

    public static boolean isIdCard(String IdCard){
        Pattern p = Pattern.compile("^(\\d{14}|\\d{17})(\\d|[xX])$");
        Matcher m = p.matcher(IdCard);
        return m.matches();
    }

    public static boolean isbankNo(String bankNo){
        Pattern p = Pattern.compile("^([0-9]{16}|[0-9]{19})$");
        Matcher m = p.matcher(bankNo);
        return m.matches();
    }


    public static void main(String[] args) throws IOException {
        System.out.println(Boolean_Mobile.isEmail("121212121212@12-12.co-m.cn"));
    }
}