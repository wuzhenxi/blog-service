package com.wzx.controller;


import cn.hutool.core.bean.BeanUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.wzx.common.lang.Result;
import com.wzx.constants.MethodName;
import com.wzx.dto.UserFileDTO;
import com.wzx.entity.Blog;
import com.wzx.service.BlogService;
import com.wzx.service.StorageService;
import com.wzx.util.JwtUtils;
import com.wzx.util.ShiroUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.PositiveOrZero;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Slf4j
@Api(tags = "博客操作")
@RestController
public class BlogController {

    private static final String STATUS = "status";
    private static final String USER_ID = "user_id";
    private static final String CREATED = "created";
    @Resource
    private BlogService blogService;

    @Resource
    private JwtUtils jwtUtils;

    @Resource
    private StorageService storageService;

    @GetMapping("/blogs")
    @ApiOperation(MethodName.QUERY_BLOG)
    public Result queryBlog(@RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        String loginUserId = null;
        String token = request.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(token)) {
            loginUserId = jwtUtils.getClaimByToken(token).getSubject();
        }
        // 未登录，仅可看公开博客
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq(STATUS, 0);
        if (Objects.nonNull(loginUserId)) {
            blogQueryWrapper.or().eq(USER_ID, loginUserId);
        }
        Page page = new Page(currentPage, pageSize);
        IPage pageData = blogService.page(page, blogQueryWrapper.orderByDesc(CREATED));

        return Result.succ(pageData);
    }

    @RequiresAuthentication
    @GetMapping("/blogs/my")
    @ApiOperation(MethodName.QUERY_MYSELF)
    public Result queryBlogMyself(@RequestParam(defaultValue = "1") Integer currentPage,
            @RequestParam(defaultValue = "10") Integer pageSize, HttpServletRequest request) {
        Long userId = ShiroUtil.getProfile().getId();
        Assert.notNull(userId, "当前未登录, 请先登录");
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq(USER_ID, userId);
        Page page = new Page(currentPage, pageSize);
        IPage pageData = blogService.page(page, blogQueryWrapper.orderByDesc("is_top", CREATED));

        return Result.succ(pageData);
    }

    @GetMapping("/blog/{id}")
    @ApiOperation(MethodName.DETAILS_BLOG)
    public Result detailBlog(@PathVariable(name = "id") @PositiveOrZero Long id, HttpServletRequest request) {
        String loginUserId = null;
        String token = request.getHeader("Authorization");
        if (!Strings.isNullOrEmpty(token)) {
            loginUserId = jwtUtils.getClaimByToken(token).getSubject();
        }
        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq("id", id).eq(STATUS, 0);
        if (Objects.nonNull(loginUserId)) {
            blogQueryWrapper.or().eq("id", id).eq(USER_ID, loginUserId);
        }
        Blog blog = blogService.getOne(blogQueryWrapper);
        Assert.notNull(blog, "该博客已被删除或仅作者可见");

        return Result.succ(blog);
    }

    @RequiresAuthentication
    @PostMapping("/blog/upload")
    @ApiOperation(MethodName.UPLOAD_FILE)
    public Result uploadFile(@RequestParam("file") MultipartFile multipartFile, HttpServletRequest request)
            throws IOException {
        JSONObject result = new JSONObject();
        if (multipartFile.isEmpty()) {
            Assert.notNull(multipartFile, "空文件");
        } else {
            String contentType = multipartFile.getContentType();
            InputStream inputStream = multipartFile.getInputStream();
            String filename = multipartFile.getOriginalFilename();
            String fileUrl = storageService.upload(request, inputStream, filename, contentType);
            result.put("filename", filename);
            result.put("fileUrl", fileUrl);
        }
        return Result.succ(result);
    }

    @RequiresAuthentication
    @PostMapping("/blog/edit")
    @ApiOperation(MethodName.EDIT_BLOG)
    public Result editBlog(@Valid @RequestBody Blog blog) {
        Blog temp = null;
        if (Objects.nonNull(blog.getId())) {
            // 修改
            temp = blogService.getById(blog.getId());
            // 只能编辑自己的文章
            Assert.isTrue(ShiroUtil.getProfile().getId().equals(temp.getUserId()), "没有权限编辑");
        } else {
            // 新增
            temp = new Blog();
            // 设置是否仅自己可见
            temp.setStatus(blog.getStatus());
            temp.setIsTop(blog.getIsTop());
            temp.setUserId(ShiroUtil.getProfile().getId());
            temp.setCreated(LocalDateTime.now());
        }
        // 修改时间
        temp.setModify(LocalDateTime.now());

        BeanUtil.copyProperties(blog, temp, "id", "userId", CREATED);
        blogService.saveOrUpdate(temp);

        return Result.succ(true);
    }

    @RequiresAuthentication
    @GetMapping("/blog/delete/{blogId}")
    @ApiOperation(MethodName.DELETE_BLOG)
    public Result deleteBLog(@PathVariable(name = "blogId") @PositiveOrZero Long blogId) {
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

    @RequiresAuthentication
    @PostMapping("/file/delete/")
    @ApiOperation(MethodName.DELETE_FILE)
    public Result deleteFile(@Valid @RequestBody UserFileDTO userFileDTO) {
        Assert.isTrue(userFileDTO.getUserId().equals(ShiroUtil.getProfile().getId()), "该博文不属于您，无权删除博文相关内容");
        boolean flag = storageService.deleteFile(userFileDTO.getFileUrl());
        return Result.succ(flag);
    }

}
