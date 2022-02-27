package pers.fjl.server.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pers.fjl.common.constant.MessageConstant;


/**
 * 处理业务逻辑异常
 *
 */
@Getter
public class BizException extends RuntimeException {

    /**
     * 错误信息
     */
    private final String message;

    public BizException(String message) {
        this.message = message;
    }


}
