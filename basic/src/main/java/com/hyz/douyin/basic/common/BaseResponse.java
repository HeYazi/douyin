package com.hyz.douyin.basic.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 前后端交互协议
 *
 * @author hegd
 * @date 2023/6/13 23:06
 */
@Data
public class BaseResponse<T> implements Serializable {
    private static final long serialVersionUID = 2382385883826279935L;

    private int status_code;
    private String status_msg;

    public BaseResponse(int status_code, String status_msg) {
        this.status_code = status_code;
        this.status_msg = status_msg;
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), errorCode.getMessage());
    }
}
