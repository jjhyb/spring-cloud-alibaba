package com.yibo.usercenter.mapper;

import com.yibo.usercenter.domain.entity.User;
import tk.mybatis.mapper.common.Mapper;

/**
 * @Service用于标注业务层组件服务 （注入dao）
 *
 * @Controller用于标注控制层组件（如struts中的action）控制器（注入服务）
 *
 * @Repository用于标注数据访问组件，即DAO组件
 *
 * @Component泛指组件，当组件不好归类的时候，我们可以使用这个注解进行标注。(把普通pojo实例化到spring容器中，相当于配置文件中的<bean id="" class=""/>)
 *
 * @Component扩展，被@Service注解的POJO类表示Service层实现，从而见到该注解就想到Service层实现，使用方式和@Component相同；
 *
 * @Component扩展，被@Controller注解的类表示Web层实现，从而见到该注解就想到Web层实现，使用方式和@Component相同；
 *
 * @Component,@Service,@Controller,@Repository注解的类，并把这些类纳入进spring容器中管理。
 */

public interface UserMapper extends Mapper<User> {
}