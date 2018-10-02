package com.tensquare.base;

import entity.Result;
import entity.StatusCode;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Create with www.dezhe.com
 *
 * @Author 德哲
 * @Date 2018/9/30 15:50
 *
 * 统一异常处理类
 */
@ControllerAdvice

public class BaseExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public Result error(Exception e) {
        e.printStackTrace();
        return new Result(false, StatusCode.ERROR,e.getMessage());
    }

}
