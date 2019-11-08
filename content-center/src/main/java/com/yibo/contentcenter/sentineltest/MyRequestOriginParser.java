package com.yibo.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.RequestOriginParser;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 17:54
 * @Description: 实现分区来源，意思就是针对的是sentinel控制台新增流控规则的针对来源
 */

//@Component
public class MyRequestOriginParser implements RequestOriginParser {

    @Override
    public String parseOrigin(HttpServletRequest request) {
        //从请求参数中获取名为origin的参数并返回
        //如果获取不到origin参数，那么就抛异常，意思就是服务调用方必须要提供这个参数，否则不让其调用
        String origin = request.getParameter("origin");
        if(StringUtils.isEmpty(origin)){
            throw new IllegalArgumentException("origin must be specified");
        }
        return origin;
    }
}
