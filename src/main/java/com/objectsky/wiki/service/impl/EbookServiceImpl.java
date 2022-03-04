package com.objectsky.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.objectsky.wiki.common.dto.EbookQueryDto;
import com.objectsky.wiki.common.dto.EbookSaveDto;
import com.objectsky.wiki.common.utils.CopyUtil;
import com.objectsky.wiki.common.utils.SnowFlake;
import com.objectsky.wiki.common.vo.EbookQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
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

    @Autowired
    private SnowFlake snowFlake;

    @Override
    public PageVo<EbookQueryVo> ebookList(EbookQueryDto ebookDto) {

        // 条件
        QueryWrapper<Ebook> wrapper = new QueryWrapper<>();

        // 如果用户输入的全是空格 那么就会默认自动查询所有列表
        if (!ObjectUtils.isEmpty(ebookDto.getName()) && !"list".equals(ebookDto.getName())) {
            String name = ebookDto.getName().trim();
            wrapper.like("name", name);
        }

        // 分页查询
        PageHelper.startPage(ebookDto.getPage(), ebookDto.getSize()); // 只对遇到对第一个sql起作用
        List<Ebook> ebooksListDb = ebookMapper.selectList(wrapper);

        // 处理查询出来是空的
        if (ebooksListDb.size() == 0){
            throw new RuntimeException("查询后列表为空，请检查参数");
        }

        // 获取分页信息
        PageInfo<Ebook> pageInfo = new PageInfo<>(ebooksListDb);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        if (pageInfo.getTotal() == 0){
            throw new RuntimeException("查找不到你要的电子书哦～");
        }

        // 列表复制, 用工具将listdb的数据复制到vo里面去
        List<EbookQueryVo> ebookVoList = CopyUtil.copyList(ebooksListDb, EbookQueryVo.class);

        // 设置分页vo
        PageVo<EbookQueryVo> pageVo = new PageVo<>();
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setList(ebookVoList);

        return pageVo;
    }

    @Override
    public void ebookSaveById(EbookSaveDto ebookSaveDto) {
        Ebook ebook = CopyUtil.copy(ebookSaveDto, Ebook.class);
        if (ObjectUtils.isEmpty(ebookSaveDto.getId())) {
            // 新增
            ebook.setId(snowFlake.nextId()); // 雪花id
            ebookMapper.insert(ebook);
        } else {
            // 更新
            ebookMapper.updateById(ebook);
        }
    }

    @Override
    public int ebookDeleteById(Long id) {
        int count = ebookMapper.deleteById(id);
        return count;
    }
}
