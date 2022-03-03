package com.objectsky.wiki.controller;


import com.objectsky.wiki.common.dto.CategoryQueryDto;
import com.objectsky.wiki.common.dto.CategorySaveDto;
import com.objectsky.wiki.common.resp.CommonResp;
import com.objectsky.wiki.common.vo.CategoryQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
     * 电子书列表页
     * @param CategoryQueryVo
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp categoryList(CategoryQueryDto categoryDto) {
        CommonResp<PageVo<CategoryQueryVo>> resp = new CommonResp<>();

        PageVo<CategoryQueryVo> list = categoryService.categoryList(categoryDto);

        // is empty?
        if (list.getList().isEmpty()){
            resp.setSuccess(false);
        }

        resp.setData(list);
        return resp;
    }

    /**
     * 保存电子书
     * @param categoryDto
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp categorySave(@RequestBody CategorySaveDto categoryDto) {
        // RequestBody针对json格式提交的数据
        // 实例化一个结果集
        CommonResp<Object> resp = new CommonResp<>();
        categoryService.categorySaveById(categoryDto);

        return resp;
    }

}
