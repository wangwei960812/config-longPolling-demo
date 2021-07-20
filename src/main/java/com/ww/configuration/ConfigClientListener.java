package com.ww.configuration;

import com.ww.config.ConfigClient;
import com.ww.distributed.lock.RedisLock;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/6 14:02
 * @description：
 */
@Slf4j
@Component
public class ConfigClientListener implements ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private ConfigProperties configProperties;

    /**
     * 初始化启动配置中心客户端
     *
     * @param contextRefreshedEvent
     */
    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        log.info("初始化启动配置中心客户端开始");
        ConfigClient configClient = new ConfigClient();
        configClient.longPolling(configProperties.getUrl(), configProperties.getDataId());
    }
}
