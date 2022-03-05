package com.objectsky.wiki.common.dto;

import lombok.Data;

/**
 * @Classname: EbookQueryDto
 * @Description:
 * @Date: Create in 11:24 PM 2022/2/25
 * @Created By: JinleWeng
 * @Modified By:
 */
@Data
public class EbookQueryDto extends PageDto{
    /**
     * id
     */
    private Long id;

    /**
     * 二级分类id
     */
    private Long categoryId2;

    /**
     * 名称
     */
    private String name;

}
