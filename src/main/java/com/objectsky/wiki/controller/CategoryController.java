package com.objectsky.wiki.controller;


import com.objectsky.wiki.common.dto.CategoryQueryDto;
import com.objectsky.wiki.common.dto.CategorySaveDto;
import com.objectsky.wiki.common.resp.CommonResp;
import com.objectsky.wiki.common.vo.CategoryQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ale
 * @since 2022-02-25
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    /**
     * 分类列表页-有分页
     *
     * @param categoryDto
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp categoryList(CategoryQueryDto categoryDto) {
        CommonResp<PageVo<CategoryQueryVo>> resp = new CommonResp<>();

        PageVo<CategoryQueryVo> list = categoryService.categoryList(categoryDto);

        resp.setData(list);
        return resp;
    }

    /**
     * 分类列表页-无分页
     *
     * @param categoryDto
     * @return
     */
    @RequestMapping(value = "/allList", method = RequestMethod.GET)
    public CommonResp categoryAllList(CategoryQueryDto categoryDto) {
        CommonResp<List<CategoryQueryVo>> resp = new CommonResp<>();

        List<CategoryQueryVo> categoryQueryVoList = categoryService.categoryAllList(categoryDto);

        resp.setData(categoryQueryVoList);
        return resp;
    }

    /**
     * 保存分类
     *
     * @param categoryDto
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp categorySave(@Valid @RequestBody CategorySaveDto categoryDto) {
        // RequestBody针对json格式提交的数据
        // 实例化一个结果集
        CommonResp<Object> resp = new CommonResp<>();
        categoryService.categorySaveById(categoryDto);

        return resp;
    }


    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResp categoryDeleteById(@PathVariable Long id) {
        CommonResp<Object> resp = new CommonResp<>();
        int count = categoryService.categoryDeleteById(id);

        // if delete not working
        if (count == 0) {
            resp.setMessage("不存在要删除的分类！");
            resp.setSuccess(false);
        }

        return resp;
    }
}
