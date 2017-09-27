package com.riskmanagement.utils;

import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by WLT on 2016/8/3.
 */
public  class Timer extends CountDownTimer {
    /**
     * @param millisInFuture    The number of millis in the future from the call
     *                          to {@link #start()} until the countdown is done and {@link #onFinish()}
     *                          is called.
     * @param countDownInterval The interval along the way to receive
     *                          {@link #onTick(long)} callbacks.
     */
    private Button button;
    public Timer(long millisInFuture, long countDownInterval, Button button) {
        super(millisInFuture, countDownInterval);
        this.button=button;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        button.setClickable(false);//防止重复点击
        button.setText(millisUntilFinished / 1000 + "s");
    }

    @Override
    public void onFinish() {
        button.setText("发送验证码");
        button.setClickable(true);
    }
}
