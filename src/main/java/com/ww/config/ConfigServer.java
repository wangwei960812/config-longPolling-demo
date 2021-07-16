package com.ww.config;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;


/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/6 14:44
 * @description：配置中心服务端
 */
@Slf4j
@RestController
public class ConfigServer {

    /**
     * guava 提供的多值 Map，一个 key 可以对应多个 value
     */
    private Multimap<String, AsyncTask> dataIdContext = Multimaps.synchronizedSetMultimap(HashMultimap.create());

    private ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat("longPolling-timeout-checker-%d").build();

    private ScheduledExecutorService timeoutChecker = new ScheduledThreadPoolExecutor(1,threadFactory);

    /**
     * 配置监听接入点
     */
    @GetMapping("/listener")
    public void addListener(HttpServletRequest request, HttpServletResponse response){

        String dataId = request.getParameter("dataId");

        //开启异步
        AsyncContext asyncContext = request.startAsync(request, response);
        AsyncTask asyncTask = new AsyncTask(asyncContext, true);

        // 维护 dataId 和异步请求上下文的关联
        dataIdContext.put(dataId,asyncTask);

        // 启动定时器，30s 后写入 304 响应
        timeoutChecker.schedule(()->{
            if(asyncTask.isTimeOut()){
                dataIdContext.remove(dataId,asyncTask);
                response.setStatus(HttpServletResponse.SC_NOT_MODIFIED);
                asyncContext.complete();
            }

        },30000, TimeUnit.MILLISECONDS);
    }

    /**
     * 配置发布接入点
     * @param dataId
     * @param configInfo
     * @return
     */
    @RequestMapping("/publishConfig")
    @SneakyThrows
    public String publishConfig(String dataId, String configInfo){
        log.info("publish configInfo dataId: [{}], configInfo: {}", dataId, configInfo);
        Collection<AsyncTask> asyncTasks = dataIdContext.removeAll(dataId);
        for (AsyncTask asyncTask : asyncTasks) {
            asyncTask.setTimeOut(false);
            HttpServletResponse response = (HttpServletResponse) asyncTask.getAsyncContext().getResponse();
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().println(configInfo);
            asyncTask.getAsyncContext().complete();
        }
        return "success";
    }



}
