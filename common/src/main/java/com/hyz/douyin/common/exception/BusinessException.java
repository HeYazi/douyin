package com.hyz.douyin.common.exception;


import com.hyz.douyin.common.common.ErrorCode;

/**
 * 自定义异常类
 *
 * @author hegd
 * @date 2023/6/13 23:02
 */

public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 9101241087442171256L;
    /**
     * 错误码
     */
    private final int code;

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.code = errorCode.getCode();
    }

    public BusinessException(ErrorCode errorCode, String message) {
        super(message);
        this.code = errorCode.getCode();
    }

    public int getCode() {
        return code;
    }
}
