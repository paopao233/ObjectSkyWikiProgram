package com.objectsky.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.objectsky.wiki.common.dto.CategoryQueryDto;
import com.objectsky.wiki.common.dto.CategorySaveDto;
import com.objectsky.wiki.common.utils.CopyUtil;
import com.objectsky.wiki.common.utils.SnowFlake;
import com.objectsky.wiki.common.vo.CategoryQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.entity.Category;
import com.objectsky.wiki.mapper.CategoryMapper;
import com.objectsky.wiki.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
    private static final Logger LOG = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Override
    public PageVo<CategoryQueryVo> categoryList(CategoryQueryDto categoryDto) {

        // 条件
        QueryWrapper<Category> wrapper = new QueryWrapper<>();

        // 分页查询
        PageHelper.startPage(categoryDto.getPage(), categoryDto.getSize()); // 只对遇到对第一个sql起作用
        List<Category> categorysListDb = categoryMapper.selectList(wrapper);

        // 获取分页信息
        PageInfo<Category> pageInfo = new PageInfo<>(categorysListDb);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        // 列表复制, 用工具将listdb的数据复制到vo里面去
        List<CategoryQueryVo> categoryVoList = CopyUtil.copyList(categorysListDb, CategoryQueryVo.class);

        // 设置分页vo
        PageVo<CategoryQueryVo> pageVo = new PageVo<>();
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setList(categoryVoList);

        return pageVo;
    }

    @Override
    public void categorySaveById(CategorySaveDto categorySaveDto) {
        Category category = CopyUtil.copy(categorySaveDto, Category.class);
        if (ObjectUtils.isEmpty(categorySaveDto.getId())) {
            // 新增
            category.setId( snowFlake.nextId()); // 雪花id
            categoryMapper.insert(category);
        } else {
            // 更新
            categoryMapper.updateById(category);
        }
    }

    @Override
    public int categoryDeleteById(Long id) {
        int count = categoryMapper.deleteById(id);
        if (count == 0) {

            // TODO to do something
        }
        return count;
    }
}
