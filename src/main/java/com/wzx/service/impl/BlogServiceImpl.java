package com.wzx.service.impl;

import com.wzx.entity.Blog;
import com.wzx.mapper.BlogMapper;
import com.wzx.service.BlogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Service
public class BlogServiceImpl extends ServiceImpl<BlogMapper, Blog> implements BlogService {

}
