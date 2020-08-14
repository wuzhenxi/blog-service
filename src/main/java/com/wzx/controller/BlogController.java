package com.wzx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.wzx.common.lang.Result;
import com.wzx.entity.Blog;
import com.wzx.service.BlogService;
import com.wzx.service.StorageService;
import com.wzx.shiro.AccountProfile;
import com.wzx.util.JwtUtils;
import com.wzx.util.ShiroUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
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
    private JwtUtils jwtUtils;

    @Autowired
    private StorageService storageService;

    @GetMapping("/blogs")
    public Result list(@RequestParam(defaultValue = "1") Integer currentPage, HttpServletRequest request) {
        String loginUserId = null;
        String token = request.getHeader("Authorization");
        if(!Strings.isNullOrEmpty(token)) {
            loginUserId = jwtUtils.getClaimByToken(token).getSubject();
        }
        // 未登录，仅可看公开博客
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq(STATUS, 0);
        if(Objects.nonNull(loginUserId)) {
            blogQueryWrapper.or().eq("user_id", loginUserId);
        }
        Page page = new Page(currentPage, 5);
        IPage pageData = blogService.page(page, blogQueryWrapper.orderByDesc("created"));

        return Result.succ(pageData);
    }

    @GetMapping("/blog/{id}")
    public Result detail(@PathVariable(name = "id") Long id, HttpServletRequest request) {
        String loginUserId = null;
        String token = request.getHeader("Authorization");
        if(!Strings.isNullOrEmpty(token)) {
            loginUserId = jwtUtils.getClaimByToken(token).getSubject();
        }
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq("id", id).eq(STATUS, 0);
        if(Objects.nonNull(loginUserId)) {
            blogQueryWrapper.or().eq("id", id).eq("user_id", loginUserId);
        }
        Blog blog = blogService.getOne(blogQueryWrapper);
        Assert.notNull(blog, "该博客已被删除或仅作者可见");

        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/upload")
    public Result upload(@RequestParam("file") MultipartFile multipartFile) throws IOException {
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
        if(Objects.nonNull(blog.getId())) {
            // 修改
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            Assert.isTrue(ShiroUtil.getProfile().getId().equals(temp.getUserId()), "没有权限编辑");
        } else {
            // 新增
            temp = new Blog();
            // 设置是否仅自己可见
            temp.setStatus(blog.getStatus());
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
        }

        BeanUtil.copyProperties(blog, temp, "id", "userId", "created");
        blogService.saveOrUpdate(temp);

        return Result.succ(true);
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete/{blogId}")
    public Result delete(@Validated @PathVariable(name = "blogId") Long blogId) {
        boolean flag = false;
        if (Objects.nonNull(blogId)) {
            Blog temp = blogService.getById(blogId);
            // 只能删除自己的文章
            log.info("query blog:{}", ShiroUtil.getProfile().getId());
            Assert.isTrue(temp.getUserId().longValue() == ShiroUtil.getProfile().getId().longValue(), "没有权限编辑");
            // 删除blog相关的文件
            String content = temp.getContent();
            log.info("blog contents:{}", content);
            flag = blogService.removeById(blogId);
        }
        return Result.succ(flag);
    }

}
