package com.objectsky.wiki.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.objectsky.wiki.common.dto.DocQueryDto;
import com.objectsky.wiki.common.dto.DocSaveDto;
import com.objectsky.wiki.common.utils.CopyUtil;
import com.objectsky.wiki.common.utils.SnowFlake;
import com.objectsky.wiki.common.vo.DocQueryVo;
import com.objectsky.wiki.common.vo.PageVo;
import com.objectsky.wiki.entity.Doc;
import com.objectsky.wiki.mapper.DocMapper;
import com.objectsky.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 文档 服务实现类
 * </p>
 *
 * @author ale
 * @since 2022-03-05
 */
@Service
public class DocServiceImpl extends ServiceImpl<DocMapper, Doc> implements DocService {
    private static final Logger LOG = LoggerFactory.getLogger(DocServiceImpl.class);

    @Autowired
    private DocMapper docMapper;

    @Autowired
    private SnowFlake snowFlake;

    @Override
    public List<DocQueryVo> docAllList(DocQueryDto docDto) {
        // 条件
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();

        // 排序
        wrapper.orderBy(true,true,"sort");

        // 如果用户输入的全是空格 那么就会默认自动查询所有列表
        if (!ObjectUtils.isEmpty(docDto.getName()) && !"list".equals(docDto.getName())) {
            String name = docDto.getName().trim();
            wrapper.like("name", name);
        }

        // 分页查询
        List<Doc> docsListDb = docMapper.selectList(wrapper);

        // 处理查询出来是空的
        if (docsListDb.size() == 0){
            throw new RuntimeException("查询后列表为空，请检查参数");
        }

        // 列表复制, 用工具将listdb的数据复制到vo里面去
        List<DocQueryVo> docVoList = CopyUtil.copyList(docsListDb, DocQueryVo.class);

        return docVoList;
    }


    @Override
    public PageVo<DocQueryVo> docList(DocQueryDto docDto) {

        // 条件
        QueryWrapper<Doc> wrapper = new QueryWrapper<>();

        // 如果用户输入的全是空格 那么就会默认自动查询所有列表
        if (!ObjectUtils.isEmpty(docDto.getName()) && !"list".equals(docDto.getName())) {
            String name = docDto.getName().trim();
            wrapper.like("name", name);
        }

        // 分页查询
        PageHelper.startPage(docDto.getPage(), docDto.getSize()); // 只对遇到对第一个sql起作用
        List<Doc> docsListDb = docMapper.selectList(wrapper);

        // 处理查询出来是空的
        if (docsListDb.size() == 0){
            throw new RuntimeException("查询后列表为空，请检查参数");
        }

        // 获取分页信息
        PageInfo<Doc> pageInfo = new PageInfo<>(docsListDb);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        if (pageInfo.getTotal() == 0){
            throw new RuntimeException("查找不到你要的文档哦～");
        }

        // 列表复制, 用工具将listdb的数据复制到vo里面去
        List<DocQueryVo> docVoList = CopyUtil.copyList(docsListDb, DocQueryVo.class);

        // 设置分页vo
        PageVo<DocQueryVo> pageVo = new PageVo<>();
        pageVo.setTotal(pageInfo.getTotal());
        pageVo.setList(docVoList);

        return pageVo;
    }

    @Override
    public void docSaveById(DocSaveDto docSaveDto) {
        Doc doc = CopyUtil.copy(docSaveDto, Doc.class);
        if (ObjectUtils.isEmpty(docSaveDto.getId())) {
            // 新增
            doc.setId( snowFlake.nextId()); // 雪花id
            docMapper.insert(doc);
        } else {
            // 更新
            docMapper.updateById(doc);
        }
    }

    @Override
    public int docDeleteById(Long id) {
        int count = docMapper.deleteById(id);
        if (count == 0) {

            // TODO to do something
        }
        return count;
    }
}
