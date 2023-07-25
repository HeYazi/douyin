package com.hyz.douyin.basic.exception;

import com.hyz.douyin.basic.common.BaseResponse;
import com.hyz.douyin.basic.common.ErrorCode;
import com.hyz.douyin.basic.common.ResultUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常捕获类
 *
 * @author hegd
 * @date 2023/6/13 23:14
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public BaseResponse<?> businessExceptionHandler(BusinessException e) {

        return ResultUtils.error(e);
    }

    @ExceptionHandler(RuntimeException.class)
    public BaseResponse<?> runtimeExceptionHandler(RuntimeException e) {
        return ResultUtils.error(ErrorCode.SYSTEM_ERROR, "系统错误");
    }
}
