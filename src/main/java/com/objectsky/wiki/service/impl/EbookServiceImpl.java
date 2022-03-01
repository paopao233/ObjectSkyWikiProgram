package com.objectsky.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.objectsky.wiki.common.dto.EbookDto;
import com.objectsky.wiki.common.utils.CopyUtil;
import com.objectsky.wiki.common.vo.EbookVo;
import com.objectsky.wiki.entity.Ebook;
import com.objectsky.wiki.mapper.EbookMapper;
import com.objectsky.wiki.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger LOG = LoggerFactory.getLogger(EbookServiceImpl.class);

    @Autowired
    private EbookMapper ebookMapper;

    @Override
    public List<EbookVo> ebookList(EbookDto ebookDto) {
        
        // 条件
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();
        if(!ObjectUtils.isEmpty(ebookDto.getName())){
            wrapper.like("name", ebookDto.getName());
        }

        // 查询
        // 分页
        PageHelper.startPage(1,3); // 只对遇到对第一个sql起作用
        List<Ebook> ebooksListDb = ebookMapper.selectList(wrapper);


        // 用工具将listdb的数据复制到vo里面去
        List<EbookVo> ebookVoList = CopyUtil.copyList(ebooksListDb, EbookVo.class);
        PageInfo<EbookVo> pageInfo = new PageInfo<>(ebookVoList);
        LOG.info("总行：{}",pageInfo.getTotal());


        return ebookVoList;
    }
}
