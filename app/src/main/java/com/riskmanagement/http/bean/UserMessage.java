package com.riskmanagement.http.bean;

/**
 * Created by ssh on 2017/6/19.
 */

public class UserMessage {

    private String success;

    private String msgCode;

    private String message;

    private Data data;

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
    public void setData(Data data){
        this.data = data;
    }
    public Data getData(){
        return this.data;
    }

    public class Data {
        private String phoneNo;

        private String sex;

        private String headImg;

        private String nickname;

        public void setPhoneNo(String phoneNo){
            this.phoneNo = phoneNo;
        }
        public String getPhoneNo(){
            return this.phoneNo;
        }
        public void setSex(String sex){
            this.sex = sex;
        }
        public String getSex(){
            return this.sex;
        }
        public void setHeadImg(String headImg){
            this.headImg = headImg;
        }
        public String getHeadImg(){
            return this.headImg;
        }
        public void setNickname(String nickname){
            this.nickname = nickname;
        }
        public String getNickname(){
            return this.nickname;
        }

    }

}
