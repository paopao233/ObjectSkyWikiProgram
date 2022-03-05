package com.objectsky.wiki.common.dto;

import lombok.Data;

/**
 * @Classname: CategoryQueryDto
 * @Description:
 * @Date: Create in 11:24 PM 2022/2/25
 * @Created By: JinleWeng
 * @Modified By:
 */
@Data
public class DocQueryDto extends PageDto{
    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    private String name;
}
