package com.ww.config;

import lombok.Data;

import javax.servlet.AsyncContext;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/6 14:41
 * @description：
 */
@Data
public class AsyncTask {

    /**
     * 长轮询请求的上下文，包含请求和响应体
     */
    private AsyncContext asyncContext;

    /**
     * 超时标记
     */
    private boolean timeOut;

    public AsyncTask(AsyncContext asyncContext, boolean timeOut) {
        this.asyncContext = asyncContext;
        this.timeOut = timeOut;
    }
}
