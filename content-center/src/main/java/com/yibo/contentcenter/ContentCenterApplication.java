package com.yibo.contentcenter;

import com.yibo.contentcenter.configuration.GlobalFeignConfiguration;
import com.yibo.contentcenter.controller.TestRestTemplateTokenRelayInterceptor;
import com.yibo.contentcenter.rocketmq.MySource;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.alibaba.sentinel.annotation.SentinelRestTemplate;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;
import tk.mybatis.spring.annotation.MapperScan;

import java.util.Collections;

@MapperScan("com.yibo.contentcenter.mapper")//扫描mybatis的指定包下的接口
@SpringBootApplication
//通过java代码方式配置feign的全局日志输入级别
//@EnableFeignClients(defaultConfiguration = GlobalFeignConfiguration.class)
@EnableFeignClients(defaultConfiguration = GlobalFeignConfiguration.class)
@EnableBinding({Source.class,MySource.class})
public class ContentCenterApplication {

    public static void main(String[] args) {

        SpringApplication.run(ContentCenterApplication.class, args);
    }

    //在Spring容器中创建一个对象，类型是RestTemplate，名称/Id为restTemplate
    @Bean
    @LoadBalanced
    @SentinelRestTemplate //RestTemplate整合Sentinel
    //@SentinelRestTemplate注解有blockHandler()和fallback()用于返回系统特定的返回信息(比如更加友好的异常提示)，使用方式和@SentinelResource的使用方法一样
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        //通过给RestTemplate添加拦截器，达到通过RestTemplate传递Header
        restTemplate.setInterceptors(Collections.singletonList(new TestRestTemplateTokenRelayInterceptor()));
        return restTemplate;
    }
}
