package com.hyz.douyin.gateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.Route;
import org.springframework.cloud.gateway.support.ServerWebExchangeUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@ConditionalOnProperty(value = "log.request.enabled", havingValue = "true", matchIfMissing = true)
public class RequestLogFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // 获得路由
        Route route = exchange.getAttribute(ServerWebExchangeUtils.GATEWAY_ROUTE_ATTR);
        // 配置文件中配置的route的uri属性(匹配到的route),本例中是http://127.0.0.1:8001
        String uri = route.getUri().toString();

        ServerHttpRequest request = exchange.getRequest();
        // 请求路径中域名之后的部分,本例中是/api/name/get
        String path = request.getPath().toString();

        // 转发后的完整地址,本例中是http://127.0.0.1:8001/api/name/get
        String address = uri + path;

        log.info("uri: " + uri);
        log.info("path: " + path);
        log.info("转发后的完整地址 address: " + address);


//        // **构建成一条长 日志，避免并发下日志错乱**
//        StringBuilder reqLog = new StringBuilder(200);
//        // 日志参数
//        List<Object> reqArgs = new ArrayList<>();
//        reqLog.append("\n\n================ Gateway Request Start  ================\n");
//        // 打印路由添加占位符
//        reqLog.append("===> {}: {}\n");
//        // 参数
//        String requestMethod = request.getMethodValue();
//        reqArgs.add(requestMethod);
//        reqArgs.add(requestUrl);
//
//        reqLog.append("================  Gateway Request End  =================\n");
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return Ordered.LOWEST_PRECEDENCE;
    }
}