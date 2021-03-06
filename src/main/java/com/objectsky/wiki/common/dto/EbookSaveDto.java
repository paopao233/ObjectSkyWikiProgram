package com.objectsky.wiki.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author ale
 * @since 2022-02-25
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class EbookSaveDto implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 名称
     */
    @NotNull(message = "【名称】不能为空")
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
