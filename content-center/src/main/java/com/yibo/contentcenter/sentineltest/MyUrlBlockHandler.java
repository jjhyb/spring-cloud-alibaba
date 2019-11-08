package com.yibo.contentcenter.sentineltest;

import com.alibaba.csp.sentinel.adapter.servlet.callback.UrlBlockHandler;
import com.alibaba.csp.sentinel.slots.block.BlockException;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author: huangyibo
 * @Date: 2019/11/6 17:33
 * @Description: 修改Sentinel的错误也，让其流控和降级，针对性的返回
 */

@Component
public class MyUrlBlockHandler implements UrlBlockHandler {

    @Override
    public void blocked(HttpServletRequest request, HttpServletResponse response, BlockException e) throws IOException {
        ErrorMsg errorMsg = null;
        if(e instanceof FlowException){
            //限流异常
            errorMsg = ErrorMsg.builder().status(100).msg("限流了...").build();
        }else if(e instanceof DegradeException){
            //降级异常
            errorMsg = ErrorMsg.builder().status(101).msg("降级了...").build();
        }else if(e instanceof ParamFlowException){
            //热点参数规则异常
            errorMsg = ErrorMsg.builder().status(102).msg("热点参数限流了...").build();
        }else if(e instanceof SystemBlockException){
            //系统规则异常
            errorMsg = ErrorMsg.builder().status(103).msg("系统规则（负载/...不满足要求）...").build();
        }else if(e instanceof AuthorityException){
            //授权异常
            errorMsg = ErrorMsg.builder().status(104).msg("授权规则不通过...").build();
        }

        //http状态码
        response.setStatus(500);
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type","application/json;charset=UTF-8");
        response.setContentType("application/json;charset=UTF-8");
        //spring mvc自带的json操作工具jackson
        new ObjectMapper().writeValue(response.getWriter(),errorMsg);
    }
}


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorMsg{

    private Integer status;

    private String msg;

}