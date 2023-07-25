package com.hyz.douyin.basic.common;


import com.hyz.douyin.basic.exception.BusinessException;

/**
 * 返回体工具
 *
 * @author hegd
 * @date 2023/6/13 23:10
 */
public class ResultUtils {
    public static <T> BaseResponse<T> success() {
        return new BaseResponse<>(0, "ok");
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode) {
        return new BaseResponse<>(errorCode.getCode(), errorCode.getMessage());
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message);
    }

    public static <T> BaseResponse<T> error(ErrorCode errorCode, String message) {
        return new BaseResponse<>(errorCode.getCode(), message);
    }

    public static <T> BaseResponse<T> error(BusinessException e) {
        return new BaseResponse<>(e.getCode(), e.getMessage());
    }

}
