package com.objectsky.wiki.service.impl;

import com.objectsky.wiki.entity.Category;
import com.objectsky.wiki.mapper.CategoryMapper;
import com.objectsky.wiki.service.CategoryService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 分类 服务实现类
 * </p>
 *
 * @author ale
 * @since 2022-03-02
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

}
