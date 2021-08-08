package com.ww.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/29 17:09
 * @description：
 */
@Data
@ConfigurationProperties(prefix = "zk")
@Configuration
public class ZooKeeperProperties {
    private String connectString;
    private int sessionTimeout;
    private String parentNode;
    private boolean enable = false;
}
