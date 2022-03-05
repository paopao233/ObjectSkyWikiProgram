package com.objectsky.wiki.service.impl;

import com.objectsky.wiki.entity.Doc;
import com.objectsky.wiki.mapper.DocMapper;
import com.objectsky.wiki.service.DocService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

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

}
