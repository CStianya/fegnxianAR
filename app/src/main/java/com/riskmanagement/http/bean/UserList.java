package com.riskmanagement.http.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/6/5.
 */

public class UserList {

    /**
     * success : yes
     * msgCode : MG010101
     * message : 操作成功
     * data : {"list":[{"realName":"小五","roleDesc":"系统管理员","id":"12321432432","officeDesc":"山东省总公司"},{"realName":"小六","roleDesc":"公司管理员","id":"12322423434","officeDesc":"公司领导"},{"realName":"小七","roleDesc":"本公司管理员","id":"12398213","officeDesc":"综合部"}]}
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
             * realName : 小五
             * roleDesc : 系统管理员
             * id : 12321432432
             * officeDesc : 山东省总公司
             */

            private String realName;
            private String roleDesc;
            private String id;
            private String officeDesc;

            public String getRealName() {
                return realName;
            }

            public void setRealName(String realName) {
                this.realName = realName;
            }

            public String getRoleDesc() {
                return roleDesc;
            }

            public void setRoleDesc(String roleDesc) {
                this.roleDesc = roleDesc;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOfficeDesc() {
                return officeDesc;
            }

            public void setOfficeDesc(String officeDesc) {
                this.officeDesc = officeDesc;
            }
        }
    }
}
