package com.yibo.contentcenter.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import java.io.IOException;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 17:36
 * @Description: 通过此拦截器给RestTemplate请求添加Header传递Token
 */
public class TestRestTemplateTokenRelayInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest httpRequest, byte[] bytes, ClientHttpRequestExecution clientHttpRequestExecution) throws IOException {
        //1、从header里面获取token
        ServletWebRequest servletWebRequest = (ServletWebRequest)RequestContextHolder.getRequestAttributes();
        String token = servletWebRequest.getHeader("X-Token");
        HttpHeaders headers = httpRequest.getHeaders();
        headers.add("X-Token",token);

        //保证RestTemplate请求继续执行
        return clientHttpRequestExecution.execute(httpRequest,bytes);
    }
}
