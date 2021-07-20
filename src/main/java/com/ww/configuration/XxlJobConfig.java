package com.ww.configuration;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * @author ：黑洞里的光
 * @date ：Created in 2021/7/27 15:57
 * @description：
 */
@Slf4j
@Configuration
public class XxlJobConfig {
    @Value("${xxl.job.admin.addresses}")
    private String adminAddresses;
    @Value("${xxl.job.accessToken}")
    private String accessToken;
    @Value("${xxl.job.executor.appname}")
    private String appname;
    @Value("${xxl.job.executor.address}")
    private String address;
    @Value("${xxl.job.executor.ip}")
    private String ip;
    @Value("${xxl.job.executor.port}")
    private int port;
    @Value("${xxl.job.executor.logpath}")
    private String logPath;
    @Value("${xxl.job.executor.logretentiondays}")
    private int logRetentionDays;

//    @Bean
//    public XxlJobSpringExecutor xxlJobExecutor() {
//        log.info(">>>>>>>>>>> xxl-job config init.");
//        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
//        xxlJobSpringExecutor.setAdminAddresses(adminAddresses);
//        xxlJobSpringExecutor.setAppname(appname);
//        xxlJobSpringExecutor.setAddress(address);
//        xxlJobSpringExecutor.setIp(ip);
//        xxlJobSpringExecutor.setPort(port);
//        xxlJobSpringExecutor.setAccessToken(accessToken);
//        xxlJobSpringExecutor.setLogPath(logPath);
//        xxlJobSpringExecutor.setLogRetentionDays(logRetentionDays);
//        return xxlJobSpringExecutor;
//    }

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobConfigProperties xxlJobConfigProperties) {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobConfigProperties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAppname(xxlJobConfigProperties.getExecutor().getAppname());
        xxlJobSpringExecutor.setAddress(xxlJobConfigProperties.getExecutor().getAddress());
        xxlJobSpringExecutor.setIp(xxlJobConfigProperties.getExecutor().getIp());
        xxlJobSpringExecutor.setPort(xxlJobConfigProperties.getExecutor().getPort());
        xxlJobSpringExecutor.setAccessToken(xxlJobConfigProperties.getAccessToken());
        xxlJobSpringExecutor.setLogPath(xxlJobConfigProperties.getExecutor().getLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(xxlJobConfigProperties.getExecutor().getLogretentiondays());
        return xxlJobSpringExecutor;
    }
}
