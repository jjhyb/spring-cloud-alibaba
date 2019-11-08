package com.yibo.usercenter.auth;

import com.yibo.usercenter.util.JwtOperator;
import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 16:08
 * @Description:
 */

@Aspect
@Component
public class AuthAspect {

    @Autowired
    private JwtOperator jwtOperator;

    /**
     * 只要加了@CheckLogin注解的方法都会进入这个方法
     * @param point
     * @return
     */
    @Around("@annotation(com.yibo.usercenter.auth.CheckLogin)")
    public Object checkLogin(ProceedingJoinPoint point) throws Throwable {
        //验证token是否合法
        this.checkToken();
        //直接执行@CheckLogin注解修饰的方法
        return point.proceed();
    }

    /**
     * 验证token是否合法
     */
    private void checkToken() {
        try {
            //1、从header里面获取token
            ServletWebRequest servletWebRequest = (ServletWebRequest)RequestContextHolder.getRequestAttributes();
            String token = servletWebRequest.getHeader("X-Token");
            //2、校验token是否合法和是否获取，如果不合法且已过期直接抛异常，如果合法则放行
            Boolean isValid = jwtOperator.validateToken(token);
            if(!isValid){
                throw new SecurityException("token不合法！");
            }
            //3、如果校验成功，就将用户信息设置到request的attribute里面并设置过期时间
            Claims claims = jwtOperator.getClaimsFromToken(token);
            servletWebRequest.setAttribute("id",claims.get("id"),1800);
            servletWebRequest.setAttribute("wxNickname",claims.get("wxNickname"),1800);
            servletWebRequest.setAttribute("role",claims.get("role"),1800);
        } catch (Throwable throwable) {
            throw new SecurityException("Token不合法");
        }
    }


    /**
     * 只要加了@CheckAuthorization注解的方法都会进入这个方法
     * @param point
     * @return
     */
    @Around("@annotation(com.yibo.usercenter.auth.CheckAuthorization)")
    public Object checkAuthorization(ProceedingJoinPoint point) throws Throwable {
        try {
            //1、验证token是否合法
            this.checkToken();
            //2、验证用户角色是否匹配
            ServletWebRequest servletWebRequest = (ServletWebRequest)RequestContextHolder.getRequestAttributes();
            String role = (String)servletWebRequest.getAttribute("role", 0);

            MethodSignature signature = (MethodSignature)point.getSignature();
            Method method = signature.getMethod();
            CheckAuthorization checkAuthorization = method.getAnnotation(CheckAuthorization.class);
            String value = checkAuthorization.value();
            if(!value.equals(role)){
                throw new SecurityException("用户无权访问!");
            }

        } catch (Throwable throwable) {
            throw new SecurityException("用户无权访问!",throwable);
        }
        return point.proceed();
    }
}
