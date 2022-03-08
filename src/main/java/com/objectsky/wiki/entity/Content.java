package com.objectsky.wiki.entity;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 文档内容
 * </p>
 *
 * @author ale
 * @since 2022-03-08
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class Content implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 文档id，与文档表关联
     */
    private Long id;

    /**
     * 内容
     */
    private String content;


}
