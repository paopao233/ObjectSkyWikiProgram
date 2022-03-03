package com.objectsky.wiki.controller;


import com.objectsky.wiki.common.dto.EbookQueryDto;
import com.objectsky.wiki.common.dto.EbookSaveDto;
import com.objectsky.wiki.common.resp.CommonResp;
import com.objectsky.wiki.common.vo.EbookQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author ale
 * @since 2022-02-25
 */
@RestController
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;

    /**
     * 电子书列表页
     * @param ebookDto
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp ebookList(@Valid EbookQueryDto ebookDto) {
        CommonResp<PageVo<EbookQueryVo>> resp = new CommonResp<>();

        PageVo<EbookQueryVo> list = ebookService.ebookList(ebookDto);

        // is empty?
        if (list.getList().isEmpty()){
            resp.setSuccess(false);
        }

        resp.setData(list);
        return resp;
    }

    /**
     * 保存电子书
     * @param ebookDto
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp ebookSave(@RequestBody EbookSaveDto ebookDto) {
        // RequestBody针对json格式提交的数据
        // 实例化一个结果集
        CommonResp<Object> resp = new CommonResp<>();
        ebookService.ebookSaveById(ebookDto);

        return resp;
    }

    /**
     * 删除电子书
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResp ebookDeleteById(@PathVariable Long id) {
        CommonResp<Object> resp = new CommonResp<>();
        int count = ebookService.ebookDeleteById(id);

        // delete not working
        if (count == 0){
            resp.setSuccess(false);
        }

        return resp;
    }

}
