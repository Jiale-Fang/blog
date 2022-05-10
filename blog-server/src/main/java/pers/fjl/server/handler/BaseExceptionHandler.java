package pers.fjl.server.handler;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import pers.fjl.common.constant.MessageConstant;
import pers.fjl.common.entity.Result;
import pers.fjl.server.exception.BizException;

import javax.validation.ConstraintViolationException;

/**
 * 异常处理控制器
 *
 * @author fangjiale 2021年01月26日
 */
@Slf4j
@ControllerAdvice
@Api(value = "全局异常处理模块", description = "全局异常处理的接口信息")
public class BaseExceptionHandler {

//    @ExceptionHandler(value = {JWTVerificationException.class})
//    @ResponseBody
//    public Result jwtHandler(JWTVerificationException e) {
//        log.debug(e.getMessage());
//        return new Result(false, e.getMessage(), MessageConstant.ACCESSERROR);
//    }

    @ExceptionHandler(value = BizException.class)
    @ResponseBody
    public Result bizHandler(BizException e) {
        e.printStackTrace();
        log.debug(e.getMessage());
        System.out.println("业务异常");
        return Result.fail(e.getMessage(), e.getCode());
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseBody
    public Result validHandle(ConstraintViolationException e) {
        e.printStackTrace();
        return Result.fail(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Result handler(Exception e) {
        e.printStackTrace();
        log.error("Exception:[{}]", e);
        return Result.fail(e.getMessage());
    }
}
