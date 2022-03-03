package com.objectsky.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.objectsky.wiki.common.dto.CategoryQueryDto;
import com.objectsky.wiki.common.dto.CategorySaveDto;
import com.objectsky.wiki.common.vo.CategoryQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.entity.Category;

/**
 * <p>
 * 分类 服务类
 * </p>
 *
 * @author ale
 * @since 2022-03-02
 */
public interface CategoryService extends IService<Category> {
    /**
     * 分类列表
     * @param categoryDto
     * @return
     */
    PageVo<CategoryQueryVo> categoryList(CategoryQueryDto categoryDto);

    /**
     * 分类保存
     * @param categorySaveDto
     */
    void categorySaveById(CategorySaveDto categorySaveDto);

    /**
     * 分类删除
     * @param id
     */
    int categoryDeleteById(Long id);
}
