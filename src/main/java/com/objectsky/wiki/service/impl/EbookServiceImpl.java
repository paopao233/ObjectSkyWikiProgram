package com.objectsky.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.objectsky.wiki.common.dto.EbookDto;
import com.objectsky.wiki.common.vo.EbookVo;
import com.objectsky.wiki.entity.Ebook;
import com.objectsky.wiki.mapper.EbookMapper;
import com.objectsky.wiki.service.EbookService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        List<EbookVo> ebookList = new ArrayList<>();
        for (Ebook ebook : ebooksListDb) {
            EbookVo ebookVo = new EbookVo();
            // spring tools
            BeanUtils.copyProperties(ebook, ebookVo);
            ebookList.add(ebookVo);
        }

        return ebookList;
    }
}
