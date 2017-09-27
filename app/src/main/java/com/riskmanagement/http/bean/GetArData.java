package com.riskmanagement.http.bean;

import android.graphics.Bitmap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */

public class GetArData {

    /**
     * success : yes
     * msgCode : MG010101
     * message : 操作成功
     * data : {"coordinates":"[123,123]","coorDesc":"北京","list":[{"url":"/aaa/asdsa.jpg"},{"url":"/bbb/sadsad.jpg"}],"status":"0","rislLevel":"1"}
     */

    private String success;
    private String msgCode;
    private String message;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * coordinates : [123,123]
         * coorDesc : 北京
         * list : [{"url":"/aaa/asdsa.jpg"},{"url":"/bbb/sadsad.jpg"}]
         * status : 0
         * rislLevel : 1
         */

        private String coordinates;
        private String coorDesc;
        private String status;
        private String rislLevel;
        private List<ListBeana> list;
        private ArrayList<UserList.DataBean.ListBean> userList ;

        public String getCoordinates() {
            return coordinates;
        }

        public void setCoordinates(String coordinates) {
            this.coordinates = coordinates;
        }

        public String getCoorDesc() {
            return coorDesc;
        }

        public void setCoorDesc(String coorDesc) {
            this.coorDesc = coorDesc;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getRislLevel() {
            return rislLevel;
        }

        public void setRislLevel(String rislLevel) {
            this.rislLevel = rislLevel;
        }

        public List<ListBeana> getList() {
            return list;
        }

        public void setList(List<ListBeana> list) {
            this.list = list;
        }

        public ArrayList<UserList.DataBean.ListBean> getUserList() {
            return userList;
        }

        public void setUserList(ArrayList<UserList.DataBean.ListBean> userList) {
            this.userList = userList;
        }

        public static class ListBeana{
            /**
             * url : /aaa/asdsa.jpg
             */

            private String url;

            private String id;

            public  ListBeana(String url,String id){
                this.url = url;
                this.id = id;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }


}
