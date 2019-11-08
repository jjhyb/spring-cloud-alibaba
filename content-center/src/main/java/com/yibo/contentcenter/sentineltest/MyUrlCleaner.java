package com.yibo.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlCleaner;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 18:21
 * @Description:
 */

@Component
@Slf4j
public class MyUrlCleaner implements UrlCleaner {

    @Override
    public String clean(String url) {
        //让/shares/1和/shares/2的返回路径相同
        //返回/shares/{number}
        String[] split = url.split("/");
        return Arrays.stream(split).map(str -> {
            if(NumberUtils.isNumber(str)){
                return "{number}";
            }
            return str;
        }).reduce((a,b) -> a +"/" + b)
                .orElse("");
    }
}
