package com.objectsky.wiki.controller;


import com.objectsky.wiki.common.dto.DocQueryDto;
import com.objectsky.wiki.common.dto.DocSaveDto;
import com.objectsky.wiki.common.resp.CommonResp;
import com.objectsky.wiki.common.vo.DocQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.service.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * 文档 前端控制器
 * </p>
 *
 * @author ale
 * @since 2022-03-05
 */
@RestController
@RequestMapping("/doc")
public class DocController {
    @Autowired
    private DocService docService;

    /**
     * 文档列表页-有分页
     *
     * @param docDto
     * @return
     */
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public CommonResp docList(DocQueryDto docDto) {
        CommonResp<PageVo<DocQueryVo>> resp = new CommonResp<>();

        PageVo<DocQueryVo> list = docService.docList(docDto);

        resp.setData(list);
        return resp;
    }

    /**
     * 文档列表页-无分页
     *
     * @param docDto
     * @return
     */
    @RequestMapping(value = "/allList", method = RequestMethod.GET)
    public CommonResp docAllList(DocQueryDto docDto) {
        CommonResp<List<DocQueryVo>> resp = new CommonResp<>();

        List<DocQueryVo> docQueryVoList = docService.docAllList(docDto);

        resp.setData(docQueryVoList);
        return resp;
    }

    /**
     * 保存文档
     *
     * @param docDto
     * @return
     */
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public CommonResp docSave(@Valid @RequestBody DocSaveDto docDto) {
        // RequestBody针对json格式提交的数据
        // 实例化一个结果集
        CommonResp<Object> resp = new CommonResp<>();
        docService.docSaveById(docDto);

        return resp;
    }


    /**
     * 删除文档
     *
     * @param id
     * @return
     */
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    public CommonResp docDeleteById(@PathVariable Long id) {
        CommonResp<Object> resp = new CommonResp<>();
        int count = docService.docDeleteById(id);

        // if delete not working
        if (count == 0) {
            resp.setMessage("不存在要删除的文档！");
            resp.setSuccess(false);
        }

        return resp;
    }
}
