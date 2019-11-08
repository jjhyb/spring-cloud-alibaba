package com.yibo.gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractNameValueGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 23:37
 * @Description: 自定义过滤器工厂
 */

@Slf4j
@Component
public class PreLogGatewayFilterFactory extends AbstractNameValueGatewayFilterFactory {

    @Override
    public GatewayFilter apply(NameValueConfig config) {
        return (exchange,chain) -> {
            log.info("请求进来了。。。{},{}",config.getName(),config.getValue());
            ServerHttpRequest modifiedRequest = exchange.getRequest().mutate().build();
            ServerWebExchange serverWebExchange = exchange.mutate().request(modifiedRequest).build();
            return chain.filter(serverWebExchange);
        };
    }
}
