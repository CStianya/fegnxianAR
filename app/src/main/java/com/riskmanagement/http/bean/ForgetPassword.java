package com.riskmanagement.http.bean;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ForgetPassword {
    /**
     * success : yes
     * msgCode : MG010101
     * message : 操作成功
     */

    private String success;
    private String msgCode;
    private String message;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(String msgCode) {
        this.msgCode = msgCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
