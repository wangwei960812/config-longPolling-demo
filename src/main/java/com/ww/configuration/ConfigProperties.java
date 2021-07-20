package com.ww.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/6 15:31
 * @description：
 */
@Data
@ConfigurationProperties(prefix = "my-config")
@Configuration
public class ConfigProperties {

    private String url;

    private String dataId;

    private boolean enable = false;
}
