package com.yibo.contentcenter;

import org.springframework.web.client.RestTemplate;

/**
 * @author: huangyibo
 * @Date: 2019/11/5 21:15
 * @Description:
 */
public class SentinelTest {

    public static void main1(String[] args) throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();
        for(int i= 0;i<1000;i++){
            String forObject = restTemplate.getForObject("http://127.0.0.1:8010/actuator/sentinel", String.class);

            Thread.sleep(500);
        }
    }

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        for(int i= 0;i<1000;i++){
            String forObject = restTemplate.getForObject("http://127.0.0.1:8010/test-a", String.class);
            System.out.println("------------"+forObject+"------------");
        }
    }
}
