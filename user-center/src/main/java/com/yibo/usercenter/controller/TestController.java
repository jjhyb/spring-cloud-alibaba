package com.yibo.usercenter.controller;

import com.yibo.usercenter.domain.entity.User;
import com.yibo.usercenter.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author: huangyibo
 * @Date: 2019/11/1 18:03
 * @Description:
 */

@RestController
public class TestController {

    /**
     * @Autowired和@Resource区别
     * @Autowired 是spring提供的注解，采取的策略为按照类型注入
     * @Resource 由J2EE提供，默认按照ByName自动注入
     *      1、如果同时指定了name和type，则从Spring上下文中找到唯一匹配的bean进行装配，找不到则抛出异常。
     *
     *      2、如果指定了name，则从上下文中查找名称（id）匹配的bean进行装配，找不到则抛出异常。
     *
     *      3、如果指定了type，则从上下文中找到类似匹配的唯一bean进行装配，找不到或是找到多个，都会抛出异常。
     *
     *      4、如果既没有指定name，又没有指定type，则自动按照byName方式进行装配；如果没有匹配，则回退为一个原始类型进行匹配，如果匹配则自动装配
     */
    @Autowired
    private UserMapper userMapper;

    @GetMapping("/test")
    public User testInsert(){
        User user = new User();
        user.setAvatarUrl("xxx");
        user.setBonus(100);
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        userMapper.insert(user);
        return user;
    }

    @GetMapping("/query")
    public User query(User user){
        //测试方法
        return user;
    }
}
