package com.wzx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wzx.common.lang.Result;
import com.wzx.entity.Blog;
import com.wzx.service.BlogService;
import com.wzx.service.StorageService;
import com.wzx.shiro.AccountProfile;
import com.wzx.util.ShiroUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Slf4j
@RestController
public class BlogController {

    private static final String STATUS = "status";
    @Autowired
    private BlogService blogService;

    @Autowired
    private StorageService storageService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage) {
        Long loginUserId = Optional.ofNullable(ShiroUtil.getProfile()).orElse(new AccountProfile()).getId();
        // 未登录，仅可看公开博客
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq(STATUS, 1);
        if(Objects.nonNull(loginUserId)) {
            blogQueryWrapper.or().eq("userId", loginUserId);
        }
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, blogQueryWrapper.orderByDesc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id) {
        Long loginUserId = Optional.ofNullable(ShiroUtil.getProfile()).orElse(new AccountProfile()).getId();
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq("id", id).eq(STATUS, 1);
        if(Objects.nonNull(loginUserId)) {
            blogQueryWrapper.or().eq("userId", loginUserId);
        }
        Blog blog = blogService.getOne(blogQueryWrapper);
        Assert.notNull(blog, "该博客已被删除或仅作者可见");

        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/upload")
    public Result upload(@Validated @RequestBody MultipartFile multipartFile) throws IOException {
        JSONObject result = new JSONObject();
        if (multipartFile.isEmpty()) {
            Assert.notNull(multipartFile, "空文件");
        } else {
            String contentType = multipartFile.getContentType();
            InputStream inputStream = multipartFile.getInputStream();
            String originalFilename = multipartFile.getOriginalFilename();
            String systemFileName = storageService.upload(inputStream, originalFilename);
            result.put("originalFilename", originalFilename);
            result.put("systemFileName", systemFileName);
        }
        return Result.succ(result);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    public Result edit(@Validated @RequestBody Blog blog) {

        Blog temp = null;
        if (blog.getId() != null) {
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            log.info("query blog:{}", ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {
            temp = new Blog();
            // 设置是否仅自己可见
            temp.setStatus(blog.getStatus());
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created", STATUS);
        blogService.saveOrUpdate(temp);

        return Result.succ(null);
    }

    @RequiresAuthentication
    @PostMapping("/blog/delete/{blogId}")
    public Result delete(@Validated @PathVariable(name = "blogId") Long blogId) {
        boolean flag = false;
        Blog temp = null;
        if (Objects.nonNull(blogId)) {
            temp = blogService.getById(blogId);
            // 只能删除自己的文章
            log.info("query blog:{}", ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");

        } else {
            // 删除blog相关的文件
            String content = temp.getContent();
            log.info("blog contents:{}", content);
            flag = blogService.removeById(blogId);
        }
        return Result.succ(flag);
    }

}
