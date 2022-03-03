package com.objectsky.wiki.common.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 分类
 * </p>
 *
 * @author ale
 * @since 2022-03-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CategoryQueryVo implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 父id
     */
    private Long parent;

    /**
     * 名称
     */
    private String name;

    /**
     * 顺序
     */
    private Integer sort;


}
