package org.wefresh.wefresh_server.external.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.wefresh.wefresh_server.WefreshServerApplication;

@Configuration
@EnableFeignClients(basePackageClasses = WefreshServerApplication.class)
public class FeignClientConfig {
}
