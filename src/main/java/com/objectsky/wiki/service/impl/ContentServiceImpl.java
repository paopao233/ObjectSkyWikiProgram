package com.objectsky.wiki.service.impl;

import com.objectsky.wiki.entity.Content;
import com.objectsky.wiki.mapper.ContentMapper;
import com.objectsky.wiki.service.ContentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文档内容 服务实现类
 * </p>
 *
 * @author ale
 * @since 2022-03-08
 */
@Service
public class ContentServiceImpl extends ServiceImpl<ContentMapper, Content> implements ContentService {

}
