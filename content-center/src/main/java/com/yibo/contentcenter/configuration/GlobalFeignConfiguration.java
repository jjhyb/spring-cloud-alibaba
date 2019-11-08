package com.yibo.contentcenter.configuration;

import feign.Logger;
import org.springframework.context.annotation.Bean;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 22:11
 * @Description:
 *
 * 通过java代码方式，全局配置配置Feign的日志级别
 *
 * 这个类别加@Configuration注解了，否则必须挪到@ComponentScan能扫描到的包以外
 *
 */

public class GlobalFeignConfiguration {

    @Bean
    public Logger.Level level(){
        //让Feign打印所有请求的细节
        return Logger.Level.FULL;
    }
}
