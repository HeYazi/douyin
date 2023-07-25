package com.hyz.douyin.basic.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 前后端交互协议
 *
 * @author hegd
 * @date 2023/6/13 23:06
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse implements Serializable {
    private static final long serialVersionUID = 2382385883826279935L;

    private int statusCode;
    private String statusMsg;
}
