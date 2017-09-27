package com.riskmanagement.http.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */

public class ArHomeData {

    /**
     * success : yes
     * msgCode : MG010101
     * message : 操作成功
     * data : {"list":[{"id":"123","projectName":"项目1","status":"0","dateTime":"2017-06-03","arDesc":"按时打卡撒"},{"id":"124","projectName":"项目2","status":"1","dateTime":"2017-06-03","arDesc":"按时打卡撒"},{"id":"125","projectName":"项目3","status":"2","dateTime":"2017-06-03","arDesc":"按时打卡撒"}]}
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
        private List<ListBean> list;

        public List<ListBean> getList() {
            return list;
        }

        public void setList(List<ListBean> list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 123
             * projectName : 项目1
             * status : 0
             * dateTime : 2017-06-03
             * arDesc : 按时打卡撒
             */

            private String id;
            private String projectName;
            private String status;
            private String dateTime;
            private String arDesc;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProjectName() {
                return projectName;
            }

            public void setProjectName(String projectName) {
                this.projectName = projectName;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getArDesc() {
                return arDesc;
            }

            public void setArDesc(String arDesc) {
                this.arDesc = arDesc;
            }
        }
    }
}
