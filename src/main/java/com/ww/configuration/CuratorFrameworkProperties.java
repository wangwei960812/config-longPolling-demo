package com.ww.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/20 15:18
 * @description：
 */
@Data
@ConfigurationProperties(prefix = "curator.framework")
@Configuration
public class CuratorFrameworkProperties {
    private int baseSleepTimeMs;
    private int maxSleepMs;
    private String connectString;
    private int sessionTimeoutMs;
    private int connectionTimeoutMs;
    private boolean enable = false;
}
