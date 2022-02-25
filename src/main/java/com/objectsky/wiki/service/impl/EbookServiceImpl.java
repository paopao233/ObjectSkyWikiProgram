package com.objectsky.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.objectsky.wiki.common.dto.EbookDto;
import com.objectsky.wiki.common.utils.CopyUtil;
import com.objectsky.wiki.common.vo.EbookVo;
import com.objectsky.wiki.entity.Ebook;
import com.objectsky.wiki.mapper.EbookMapper;
import com.objectsky.wiki.service.EbookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author ale
 * @since 2022-02-25
 */
@Service
public class EbookServiceImpl extends ServiceImpl<EbookMapper, Ebook> implements EbookService {
    @Autowired
    private EbookMapper ebookMapper;

    @Override
    public List<EbookVo> ebookList(EbookDto ebookDto) {
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        wrapper.like("name", ebookDto.getName());

        List<Ebook> ebooksListDb = ebookMapper.selectList(wrapper);

//        List<EbookVo> ebookList = new ArrayList<>();
//        for (Ebook ebook : ebooksListDb) {
//            // EbookVo ebookVo = new EbookVo();
//            // BeanUtils.copyProperties(ebook, ebookVo);
//
//            EbookVo ebookVo = CopyUtil.copy(ebook, EbookVo.class);
//            ebookList.add(ebookVo);
//        }

        List<EbookVo> ebookVoList = CopyUtil.copyList(ebooksListDb, EbookVo.class);

        return ebookVoList;
    }
}
