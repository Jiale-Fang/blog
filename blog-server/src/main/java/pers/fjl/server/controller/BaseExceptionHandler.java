package pers.fjl.server.controller;

import com.auth0.jwt.exceptions.JWTVerificationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;

import javax.validation.ConstraintViolationException;

/**
 * 异常处理控制器
 *
 * @author fangjiale 2021年01月26日
 */
@Slf4j
@ControllerAdvice
//@Api(value = "全局异常处理模块", description = "全局异常处理的接口信息")
public class BaseExceptionHandler {

//    @ExceptionHandler(Exception.class)
//    @ResponseBody
//    public Result handler(Exception e) {
//        if (e instanceof RuntimeException) {
//            return new Result(false, e.getMessage(), MessageConstant.ERROR);
//        }
//        log.error("Exception:[{}]", e);
//        return new Result(false, e.getMessage(), MessageConstant.ERROR);
//    }
//
//    @ExceptionHandler(value = {RuntimeException.class})
//    @ResponseBody
//    public Result runtimeHandler(RuntimeException e) {
//        System.out.println("出现异常：---------------------------》" + e.getMessage());
//        return new Result(false, e.getMessage(), MessageConstant.RUNTIMEERROR);
//    }
//
//    @ExceptionHandler(value = {JWTVerificationException.class})
//    @ResponseBody
//    public Result jwtHandler(JWTVerificationException e) {
//        log.debug(e.getMessage());
//        return new Result(false, e.getMessage(), MessageConstant.ACCESSERROR);
//    }
//
//    @ExceptionHandler(value = {ConstraintViolationException.class})
//    @ResponseBody
//    public Result validHandle(ConstraintViolationException e) {
//        return new Result(false, e.getMessage(), MessageConstant.ERROR);
//    }
}
