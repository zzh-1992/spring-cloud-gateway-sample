/*
 *Copyright @2021 Grapefruit. All rights reserved.
 */

package com.example.demogateway.error;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理(参考自:https://zhuanlan.zhihu.com/p/144180181)
 *
 * @author zhihuangzhang
 * @version 1.0
 * @date 2022-03-06 1:19 下午
 */
@Service
public class LocalErrorWebExceptionHandler implements ErrorWebExceptionHandler {
    private static final Log log = LogFactory.getLog(LocalErrorWebExceptionHandler.class);

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        ServerHttpResponse response = exchange.getResponse();

        if (response.isCommitted()) {
            return Mono.error(ex);
        }

        // header set
        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        if (ex instanceof ResponseStatusException) {
            response.setStatusCode(((ResponseStatusException) ex).getStatus());
        }

        // 定义错误信息
        Map<String, String> errMsg = new HashMap<>();
        errMsg.put("msg", ex.getMessage());
        errMsg.put("code", "101");
        errMsg.put("time", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE));

        return response
                .writeWith(Mono.fromSupplier(() -> {
                    DataBufferFactory bufferFactory = response.bufferFactory();
                    try {
                        return bufferFactory.wrap(new ObjectMapper().writeValueAsBytes(errMsg));
                    } catch (JsonProcessingException e) {
                        log.warn("Error writing response", ex);
                        return bufferFactory.wrap(new byte[0]);
                    }
                }));
    }
}
