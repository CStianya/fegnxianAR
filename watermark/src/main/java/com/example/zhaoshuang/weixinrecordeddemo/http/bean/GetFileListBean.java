package com.example.zhaoshuang.weixinrecordeddemo.http.bean;

import java.util.List;

/**
 * Created by wcx on 2017/3/29.
 */

public class GetFileListBean {

    /**
     * status : 200
     * msg : 获取已上传片段成功
     * data : [1,2]
     */

    private int status;
    private String msg;
    private List<Integer> data;
    /**
     * success : true
     * messageEncode :
     * totalpages : 0
     * currentpage : 0
     * totalrecords : 0
     * pagerows : 10
     */

    private boolean success;
    private String messageEncode;
    private int totalpages;
    private int currentpage;
    private int totalrecords;
    private int pagerows;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Integer> getData() {
        return data;
    }

    public void setData(List<Integer> data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessageEncode() {
        return messageEncode;
    }

    public void setMessageEncode(String messageEncode) {
        this.messageEncode = messageEncode;
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
