package com.ww.configuration;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/20 15:17
 * @description：
 */
@Configuration
public class ZookeeperConfig {

    @Bean
    public CuratorFramework curatorFramework(CuratorFrameworkProperties curatorFrameworkProperties) {
        if (curatorFrameworkProperties.isEnable()) {
            RetryPolicy retryPolicy = new ExponentialBackoffRetry(curatorFrameworkProperties.getBaseSleepTimeMs(), curatorFrameworkProperties.getMaxSleepMs());
            CuratorFramework client = CuratorFrameworkFactory.newClient(curatorFrameworkProperties.getConnectString(), retryPolicy);
            client.start();
            return client;
        } else {
            return null;
        }


    }
}
