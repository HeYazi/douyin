package com.hyz.douyin.basic.common;

import lombok.Data;

/**
 * @author hegd
 * @date 2023/6/20 22:23
 */
@Data
public class PageDTO {
    /**
     * 当前页号
     */
    private long current = 1;

    /**
     * 页面大小
     */
    private long pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;
}
