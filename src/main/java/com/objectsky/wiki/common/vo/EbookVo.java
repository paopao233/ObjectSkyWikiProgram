package com.objectsky.wiki.common.vo;

import lombok.Data;

/**
 * @Classname: EbookVo
 * @Description:
 * @Date: Create in 11:33 PM 2022/2/25
 * @Created By: JinleWeng
 * @Modified By:
 */
@Data
public class EbookVo  {
    /**
     * 分类2
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 分类1
     */
    private Long category1Id;

    /**
     * 分类2
     */
    private Long category2Id;

    /**
     * 描述
     */
    private String description;

    /**
     * 封面
     */
    private String cover;

    /**
     * 文档数
     */
    private Integer docCount;

    /**
     * 阅读数
     */
    private Integer viewCount;

    /**
     * 点赞数
     */
    private Integer voteCount;
}
