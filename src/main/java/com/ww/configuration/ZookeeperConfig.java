package com.ww.configuration;

import com.ww.distributed.zookeeper.DistributedClient;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    @Bean
    public DistributedClient distributedClient(ZooKeeperProperties zooKeeperProperties) throws Exception {
        if(zooKeeperProperties.isEnable()){
            DistributedClient distributedClient = new DistributedClient(zooKeeperProperties);
            distributedClient.connect();
            distributedClient.registerServer();
            return distributedClient;
        }else {
            return null;
        }
    }
}
