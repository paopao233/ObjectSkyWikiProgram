package com.objectsky.wiki.common.vo;

import lombok.Data;

import java.util.List;

/**
 * @Classname: PageVo
 * @Description:
 * @Date: Create in 9:43 PM 2022/3/1
 * @Created By: JinleWeng
 * @Modified By:
 */
@Data
public class PageVo<T> {
    // 对于不确定的参数 可以加一个范型

    /**
     * 数量
     */
    private Long total;

    /**
     *
     */
    private List<T> list;
}
