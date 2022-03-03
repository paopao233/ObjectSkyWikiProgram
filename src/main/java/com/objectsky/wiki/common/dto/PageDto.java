package com.objectsky.wiki.common.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

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
    @NotNull(message = "【页码】不能为空")
    private int page;

    /**
     * 数量
     */
    @NotNull(message = "【每页条数】不能为空")
    @Max(value = 1000,message = "【每页条数】不能超过1000")
    private int size;
}
