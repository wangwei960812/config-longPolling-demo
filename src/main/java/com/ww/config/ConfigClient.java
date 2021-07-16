package com.ww.config;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/6 13:30
 * @description：配置中心客户端
 */
@Slf4j
public class ConfigClient {

    /**
     * http客户端
     */
    private CloseableHttpClient httpClient;

    /**
     * http配置
     */
    private RequestConfig requestConfig;

    public ConfigClient() {
        this.httpClient = HttpClientBuilder.create().build();
        //配置超时时间，客户端超时时间要大于长轮询约定的超时时间
        this.requestConfig = RequestConfig.custom().setSocketTimeout(40000).build();
    }

    @SneakyThrows
    public void longPolling(String url,String dataId){
        String endpoint = url+"?dataId="+dataId;
        log.info("endpoint:{}",endpoint);
        HttpGet request = new HttpGet(endpoint);
        CloseableHttpResponse response = httpClient.execute(request);
        switch (response.getStatusLine().getStatusCode()){
            //响应成功
            case 200:{
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                StringBuilder result = new StringBuilder();
                String line;
                while((line = bufferedReader.readLine())!=null){
                    result.append(line);
                }
                response.close();
                String newConfigInfo = result.toString();
                log.info("dataId:[{}] changed,receive configInfo:{}",dataId,newConfigInfo);
                longPolling(url, dataId);
            }
            //304 响应码标记配置未变更
            case 304:{
                log.info("longPolling dataId:[{}] once finished, configInfo is unChanged, longPolling again",dataId);
                longPolling(url, dataId);
            }
            default:{
                throw new RuntimeException(String.format("unExcepted HTTP status code %s",response.getStatusLine().getStatusCode()));
            }
        }
    }
}
