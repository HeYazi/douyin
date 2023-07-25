package com.hyz.douyin.basic.utils;

import com.hyz.douyin.basic.common.ErrorCode;
import com.hyz.douyin.basic.exception.BusinessException;

/**
 * 异常抛出工具类
 *
 * @author HYZ
 * @date 2023/7/25 14:05
 */
public class ThrowUtils {
    public static void throwIf(boolean condition, RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
