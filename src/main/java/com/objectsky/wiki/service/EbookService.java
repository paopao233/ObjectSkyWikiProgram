package com.objectsky.wiki.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.objectsky.wiki.common.dto.EbookDto;
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
    PageVo<EbookVo> ebookList(EbookDto ebookDto);

}
