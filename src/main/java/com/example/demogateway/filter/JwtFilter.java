/*
 * Copyright 2013-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.demogateway.filter;

import com.example.demogateway.exception.LocalException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.List;

import static org.springframework.cloud.gateway.support.ServerWebExchangeUtils.GATEWAY_REQUEST_URL_ATTR;

/**
 * jwt校验过滤器
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2022-03-06 12:25 下午
 */
public class JwtFilter implements GlobalFilter, Ordered {

    private static final Log log = LogFactory.getLog(JwtFilter.class);

    public JwtFilter() {
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        URI requestUrl = exchange.getRequiredAttribute(GATEWAY_REQUEST_URL_ATTR);

        // 获取请求头
        HttpHeaders headers = exchange.getRequest().getHeaders();

        // 获取请求头里的"JwtToken"(可能有多个值,默认取第一个)
        List<String> jwtTokenList = headers.get("JwtToken");
        if (jwtTokenList == null || jwtTokenList.isEmpty()) {
            // 未传递jwtToken时,返回错误信息
            // 校验不通过
            return Mono.error(new LocalException("JwtToken is missing."));
            //throw new IllegalStateException("JwtToken is missing.");
        }

        // TODO: check JWT token
        String token = jwtTokenList.get(0);

        if (tokenIsOk(token)) {
            // 校验通过
            return chain.filter(exchange);
        } else {
            // 校验不通过
            return Mono.error(new LocalException("JwtToken is error."));
            //throw new IllegalStateException("JwtToken is error.");
        }
    }

    /**
     * 校验token是否合法
     *
     * @param token token
     * @return true(通过校验)
     */
    private boolean tokenIsOk(String token) {
        return true;
    }
}
