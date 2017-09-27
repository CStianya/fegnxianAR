package com.riskmanagement.http;

/**
 * Created by Administrator on 2017/4/24.
 */

public interface HttpListener {
    void Success(Object object);

    void Failed(String reason);

    void requestCompleted();
}
