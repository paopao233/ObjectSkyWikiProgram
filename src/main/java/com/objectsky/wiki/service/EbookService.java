package com.objectsky.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.objectsky.wiki.common.dto.EbookQueryDto;
import com.objectsky.wiki.common.dto.EbookSaveDto;
import com.objectsky.wiki.common.vo.EbookVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.entity.Ebook;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author ale
 * @since 2022-02-25
 */

public interface EbookService extends IService<Ebook> {
    /**
     * 电子书列表
     * @param ebookDto
     * @return
     */
    PageVo<EbookVo> ebookList(EbookQueryDto ebookDto);

    /**
     * 电子书保存
     * @param ebookSaveDto
     */
    void ebookSaveById(EbookSaveDto ebookSaveDto);
}
