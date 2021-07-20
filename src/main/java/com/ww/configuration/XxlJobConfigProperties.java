package com.ww.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/27 14:32
 * @description：
 */
@Data
@ConfigurationProperties(prefix = "xxl.job")
@Configuration
public class XxlJobConfigProperties {
    private String accessToken;
    private XxlJobConfigProperties.Admin admin;
    private XxlJobConfigProperties.Executor executor;

    @Data
    public static class Admin{
        private String addresses;
    }

    @Data
    public static class Executor {
        private String appname;
        private String address;
        private String ip;
        private int port;
        private String logpath;
        private int logretentiondays;
    }
}
