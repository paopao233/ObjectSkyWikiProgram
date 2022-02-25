package com.objectsky.wiki.controller;


import com.objectsky.wiki.common.dto.EbookDto;
import com.objectsky.wiki.common.resp.CommonResp;
import com.objectsky.wiki.common.vo.EbookVo;
import com.objectsky.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/ebook")
public class EbookController {
    @Autowired
    private EbookService ebookService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp ebookList(EbookDto ebookDto) {
        CommonResp<List<EbookVo>> resp = new CommonResp<>();

        List<EbookVo> list = ebookService.ebookList(ebookDto);
        if (list.size() <= 0) {
            resp.setSuccess(false);
            resp.setMessage("failed to search!");
        }

        resp.setData(list);
        return resp;
    }
}
