package com.objectsky.wiki.common.dto;

import lombok.Data;

/**
 * @Classname: PageVo
 * @Description:
 * @Date: Create in 9:43 PM 2022/3/1
 * @Created By: JinleWeng
 * @Modified By:
 */
@Data
public class PageDto {
    /**
     * 页数
     */
    private int page;

    /**
     * 数量
     */
    private int size;
}
