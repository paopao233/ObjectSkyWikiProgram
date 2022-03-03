package com.objectsky.wiki.common.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;
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
public class CategorySaveDto implements Serializable {

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
    @NotNull(message = "【名称不能为空】")
    private String name;

    /**
     * 顺序
     */
    @NotNull(message = "【排序不能为空】")
    private Integer sort;


}
