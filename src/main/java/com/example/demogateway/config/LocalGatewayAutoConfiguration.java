/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.example.demogateway.config;

import com.example.demogateway.filter.JwtFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 扩展GlobalFilter
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2022-03-06 12:25 下午
 */
@Configuration
public class LocalGatewayAutoConfiguration {

    /**
     * 添加本地jwt校验过滤器
     *
     * @return JwtFilter
     */
    @Bean
    @ConditionalOnProperty(name = "server.jwt.enabled", matchIfMissing = true)
    public JwtFilter jwtFilter() {
        return new JwtFilter();
    }
}
