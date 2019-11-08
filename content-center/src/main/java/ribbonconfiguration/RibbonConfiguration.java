package ribbonconfiguration;

import com.netflix.loadbalancer.IRule;
import com.yibo.contentcenter.configuration.NacosSameClusterWeightedRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: huangyibo
 * @Date: 2019/11/2 18:08
 * @Description: 如果将此类放进启动类的包下，那么此工程的所有ribbon都会使用这种负载均衡规则
 */

@Configuration
public class RibbonConfiguration {

    //自定义负载均衡配置，通过Nacos client拓展Ribbon，进行同一集群下服务优先调用，这个是针对的异地灾备的
    @Bean
    public IRule ribbonRule(){
        return new NacosSameClusterWeightedRule();
    }

    //自定义负载均衡配置，通过Nacos client自动通过基于权重的负载均衡算法，给我们选择一个实例
    /*@Bean
    public IRule ribbonRule(){
        return new NacosWeightedRule();
    }*/

    //Ribbon提供的负载均衡策略
    /*@Bean
    public IRule ribbonRule(){
        return new RandomRule();
    }*/
}
