package com.yibo.usercenter.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 18:21
 * @Description:
 */

@Component
@Slf4j
public class MyTestStreamConsumer {

    @StreamListener(MySink.MY_INPUT)
    public void receive(String messageBody){

        log.info("自定义接口消费：通过Stream收到了消息：messageBody={}",messageBody);
    }

    /**
     * 全局异常处理
     * 如果上面的receive()方法主动往外抛异常的话，会进入此方法
     * @param message 发生异常的消息
     */
//    @StreamListener("error_channel")
//    public void error(Message<?> message){
//        ErrorMessage errorMessage = (ErrorMessage)message;
//        log.error("发生异常：errorMessage={}",errorMessage);
//    }
}
