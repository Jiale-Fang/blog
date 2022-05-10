package pers.fjl.server.exception;

import lombok.Getter;


/**
 * 处理认证逻辑异常
 *
 */
@Getter
public class CertificationException extends RuntimeException {

    /**
     * 错误信息
     */
    private final String message;

    public CertificationException(String message) {
        this.message = message;
    }

}
