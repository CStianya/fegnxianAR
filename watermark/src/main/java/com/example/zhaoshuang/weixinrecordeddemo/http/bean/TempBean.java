package com.example.zhaoshuang.weixinrecordeddemo.http.bean;

/**
 * Created by wcx on 2017/3/28.
 */

public class TempBean<T> {


    /**
     * success : true
     * msg : 上传第4片成功!
     * messageEncode :
     * status :
     * totalpages : 0
     * currentpage : 0
     * totalrecords : 0
     * pagerows : 10
     */

    private boolean success;
    private String msg;
    private String messageEncode;
    private String status;
    private int totalpages;
    private int currentpage;
    private int totalrecords;
    private int pagerows;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getMessageEncode() {
        return messageEncode;
    }

    public void setMessageEncode(String messageEncode) {
        this.messageEncode = messageEncode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalpages() {
        return totalpages;
    }

    public void setTotalpages(int totalpages) {
        this.totalpages = totalpages;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getTotalrecords() {
        return totalrecords;
    }

    public void setTotalrecords(int totalrecords) {
        this.totalrecords = totalrecords;
    }

    public int getPagerows() {
        return pagerows;
    }

    public void setPagerows(int pagerows) {
        this.pagerows = pagerows;
    }
}
