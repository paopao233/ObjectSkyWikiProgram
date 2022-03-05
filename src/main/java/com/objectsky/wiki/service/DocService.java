package com.objectsky.wiki.service;

import com.objectsky.wiki.common.dto.DocQueryDto;
import com.objectsky.wiki.common.dto.DocSaveDto;
import com.objectsky.wiki.common.vo.DocQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.entity.Doc;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 文档 服务类
 * </p>
 *
 * @author ale
 * @since 2022-03-05
 */
public interface DocService extends IService<Doc> {
    /**
     * 文档列表 无分页的 查询全部
     * @param docDto
     * @return
     */
    List<DocQueryVo> docAllList(DocQueryDto docDto);

    /**
     * 文档列表 有分页的
     * @param docDto
     * @return
     */
    PageVo<DocQueryVo> docList(DocQueryDto docDto);

    /**
     * 文档保存
     * @param docSaveDto
     */
    void docSaveById(DocSaveDto docSaveDto);

    /**
     * 文档删除
     * @param id
     */
    int docDeleteById(Long id);
}
