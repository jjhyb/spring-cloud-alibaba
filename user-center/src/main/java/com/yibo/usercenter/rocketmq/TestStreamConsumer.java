package com.yibo.usercenter.rocketmq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

/**
 * @author: huangyibo
 * @Date: 2019/11/7 18:21
 * @Description:
 */

@Component
@Slf4j
public class TestStreamConsumer {

    @StreamListener(Sink.INPUT)
    public void receive(String messageBody){
        log.info("通过Stream收到了消息：messageBody={}",messageBody);
    }
}
