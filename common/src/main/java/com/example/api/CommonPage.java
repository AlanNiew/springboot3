package com.example.api;

import lombok.Data;

import java.util.List;

/**
 * Author:Niu
 * Date:2025/1/18 10:23
 * Description: 通用分页返回对象
 */
@Data
public class CommonPage<T> {
    /**
     * 当前页码
     */
    private Integer pageNum;
    /**
     * 每页数量
     */
    private Integer pageSize;
    /**
     * 总页数
     */
    private Integer totalPage;
    /**
     * 总条数
     */
    private Long total;
    /**
     * 分页数据列表
     */
    private List<T> list;
}
