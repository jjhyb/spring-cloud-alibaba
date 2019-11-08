package com.yibo.contentcenter.controller;

import com.alibaba.csp.sentinel.Entry;
import com.alibaba.csp.sentinel.SphU;
import com.alibaba.csp.sentinel.Tracer;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.alibaba.csp.sentinel.context.ContextUtil;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.RuleConstant;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.yibo.contentcenter.domain.dto.UserDTO;
import com.yibo.contentcenter.feignclient.TestBaiduFeignClient;
import com.yibo.contentcenter.feignclient.TestUserCenterFeignClient;
import com.yibo.contentcenter.rocketmq.MySource;
import com.yibo.contentcenter.sentineltest.TestControllerBlockHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 0:43
 * @Description:
 */

@RestController
@Slf4j
@RefreshScope//Nacos作为分布式配置服务中心的配置动态刷新
public class TestController {

    @Autowired
    private DiscoveryClient discoveryClient;

    @Autowired
    private TestUserCenterFeignClient testUserCenterFeignClient;

    @Autowired
    private TestBaiduFeignClient testBaiduFeignClient;

    @Autowired
    private TestService testService;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 测试服务发信啊证明内容中心总能找到用户中心
     * @return 用户中心所有实例的地址信息
     */
    @GetMapping("/t1")
    public List<ServiceInstance> setDiscoveryClient(){
        //查询指定服务的所有实例的信息
        //consul/eureka/zookeeper 都可以使用下面的api
        return discoveryClient.getInstances("user-center");
    }

    /**
     * 测试查询当前服务发现组件里面注册了哪些微服务
     * @return 返回当前服务发现组件里面注册的微服务列表
     */
    @GetMapping("/t2")
    public List<String> setServices(){
        //查询当前服务发现组件里面注册了哪些微服务
        return discoveryClient.getServices();
    }

    @GetMapping("/t3")
    public UserDTO query(UserDTO userDTO){

        return testUserCenterFeignClient.query(userDTO);
    }

    @GetMapping("/baidu")
    public String baiduIndex(UserDTO userDTO){

        return testBaiduFeignClient.index();
    }

    @GetMapping("/test-a")
    public String testA(){
        testService.common();
        return "test-a";
    }

    @GetMapping("/test-b")
    public String testB(){
        testService.common();
        return "test-b";
    }

    @GetMapping("/test-hot")
    @SentinelResource("hot")
    public String testHot(@RequestParam(required = false) String a,
                          @RequestParam(required = false)String b){
        return a + "：" + b;
    }

    @GetMapping("/test-add-flow-rule")
    @SentinelResource("hot")
    public String testHot(){
        this.initFlowQpsRule();
        return "success";
    }

    /**
     * 使用代码的方式配置Sentinel的流控规则
     */
    private void initFlowQpsRule(){
        List<FlowRule> rules = new ArrayList<>();
        FlowRule rule = new FlowRule("/shares/1");
        rule.setCount(20);
        rule.setGrade(RuleConstant.FLOW_GRADE_QPS);
        rule.setLimitApp("default");
        rules.add(rule);
        FlowRuleManager.loadRules(rules);
    }

    @GetMapping("/test-sentinel-api")
    public String testSentinelAPI(@RequestParam(required = false) String a){
        String resourceName = "test-sentinel-api";
        ContextUtil.enter(resourceName,"test-wfw");

        Entry entry = null;
        try {
            //定义一个sentinel保护的资源 名称是test-sentinel-api
            entry = SphU.entry(resourceName);
            //被保护的业务逻辑
            if(StringUtils.isEmpty(a)){
                throw new IllegalArgumentException("a不能为空");
            }
            return a;
        } catch (BlockException e) {
            //如果被保护的资源被限流或者降级了，就会抛BlockException
            log.warn("限流，或者降级了...",e);
            return "限流，或者降级了...";
        }catch (IllegalArgumentException e) {
            //统计IllegalArgumentException发生的次数，发生的占比等
            Tracer.trace(e);
            return "参数非法...";
        } finally {
            if(entry != null){
                //退出entry
                entry.exit();
            }
            ContextUtil.exit();
        }
    }

    @GetMapping("/test-sentinel-resource")
    @SentinelResource(value = "test-sentinel-api",blockHandler = "block",
            fallback = "fallback",blockHandlerClass = TestControllerBlockHandler.class)
    public String testSentinelResource(@RequestParam(required = false) String a){
        //被保护的业务逻辑
        if(StringUtils.isEmpty(a)){
            throw new IllegalArgumentException("a cannot be blank");
        }
        return a;
    }



    /**
     * Sentinel 1.5 处理降级
     * Sentinel 1.5不能将此方法移出到其他类中，1.6中SentinelResource注解有fallbackclass属性，可以将方法移动到单独的类中
     * 升级为Sentinel 1.6 fallback可以处理Throwable，意思就是不管什么异常都可以走到fallback方法中
     * @param a
     * @return
     */
    public String fallback(String a){

        return "限流，或者降级了... fallback";
    }

    @GetMapping("/test-rest-template-sentinel/{id}")
    public UserDTO test(@PathVariable("id") Integer id){
        return restTemplate.getForObject("http://user-center/users/{id}",UserDTO.class,id);
    }

    /**
     * 此方法为演示restTemplate传递token
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/tokenRelay/{id}")
    public ResponseEntity<UserDTO> tokenRelay(@PathVariable("id") Integer id, HttpServletRequest request){
        String token = request.getHeader("X-Token");
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("X-Token",token);
        return restTemplate
                .exchange("http://user-center/users/{id}",
                        HttpMethod.GET,
                        new HttpEntity<>(httpHeaders),
                        UserDTO.class,
                        id);
    }

    //用于测试Nacos作为分布式配置服务中心
    @Value("${your.configuration}")
    private String yourConfiguration;

    @GetMapping("/test-config")
    public String testConfiguration(){
        return yourConfiguration;
    }



    @Autowired
    private Source source;

    @GetMapping("/test-stream")
    public String testStream(@PathVariable("id") Integer id){
        source.output().send(MessageBuilder.withPayload("消息体").build());
        return "success";
    }

    @Autowired
    private MySource mySource;

    @GetMapping("/test-stream2")
    public String testStream2(@PathVariable("id") Integer id){
        mySource.output().send(MessageBuilder.withPayload("消息体").build());
        return "success";
    }
}
