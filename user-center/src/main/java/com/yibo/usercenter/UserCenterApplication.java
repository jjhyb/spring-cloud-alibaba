package com.yibo.usercenter;

import com.yibo.usercenter.rocketmq.MySink;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import tk.mybatis.spring.annotation.MapperScan;

@MapperScan("com.yibo.usercenter.mapper")//扫描mybatis的指定包下的接口
@SpringBootApplication
@EnableBinding({Sink.class,MySink.class})
public class UserCenterApplication {

    public static void main(String[] args) {

        SpringApplication.run(UserCenterApplication.class, args);
    }

}
