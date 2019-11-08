package com.yibo.contentcenter.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author: huangyibo
 * @Date: 2019/11/8 16:30
 * @Description:
 */

@RestControllerAdvice
@Slf4j
public class GlobalExceptionErrorHandler {

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ErrorBody> error(SecurityException e){
        log.warn("发生SecurityException异常,e={}",e);
        ErrorBody errorBody = ErrorBody.builder().body(e.getMessage()).status(HttpStatus.UNAUTHORIZED.value()).build();
        ResponseEntity<ErrorBody> responseEntity = new ResponseEntity<>(errorBody, HttpStatus.UNAUTHORIZED);
        return responseEntity;
    }
}

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class ErrorBody{
    private String body;
    private int status;
}
