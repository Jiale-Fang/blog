package pers.fjl.encrypt.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.crypto.BadPaddingException;

@ResponseBody
@ControllerAdvice
public class BaseExceptionHandler {

    @ExceptionHandler(value = {BadPaddingException.class})
    @ResponseBody
    public void zuulHandler(BadPaddingException e) {
        System.out.println("未使用网关加密，是密文传输");
    }

}
