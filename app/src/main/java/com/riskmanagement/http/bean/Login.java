package com.riskmanagement.http.bean;

/**
 * Created by Administrator on 2017/6/5.
 */

public class Login {
    /**
     * success : yes
     * msgCode : MG010101
     * message : 操作成功
     */

    private String success;

    private String msgCode;

    private String message;

    private String data;

    public void setSuccess(String success){
        this.success = success;
    }
    public String getSuccess(){
        return this.success;
    }
    public void setMsgCode(String msgCode){
        this.msgCode = msgCode;
    }
    public String getMsgCode(){
        return this.msgCode;
    }
    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
