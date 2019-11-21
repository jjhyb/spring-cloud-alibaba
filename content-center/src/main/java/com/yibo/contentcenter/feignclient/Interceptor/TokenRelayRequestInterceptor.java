package com.yibo.contentcenter.feignclient.Interceptor;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 16:57
 * @Description:
 */
public class TokenRelayRequestInterceptor implements RequestInterceptor {

    @Override
    public void apply(RequestTemplate requestTemplate) {
        //1、从header里面获取token
        ServletWebRequest servletWebRequest = (ServletWebRequest)RequestContextHolder.getRequestAttributes();
        String token = servletWebRequest.getHeader("X-Token");

        //2、将token传递
        if(!StringUtils.isEmpty(token)){
            requestTemplate.header("X-Token",token);
        }

    }
}
