package com.wzx.logger;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.common.base.Strings;
import com.wzx.common.lang.Result;
import com.wzx.entity.Blog;
import com.wzx.entity.LogInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Objects;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * File Description.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/18</pre>
 */
@Slf4j
@Api(tags = "日志操作")
@RestController
@RequestMapping("/log")
public class LogInfoController {

    @RequiresAuthentication
    @PostMapping("/query")
    @ApiOperation("日志查看")
    public Result queryLog(@RequestBody LogInfo logInfo) {
        String loginUserId = null;
//        String token = request.getHeader("Authorization");
//        if(!Strings.isNullOrEmpty(token)) {
//            loginUserId = jwtUtils.getClaimByToken(token).getSubject();
//        }
//        QueryWrapper<Blog> blogQueryWrapper = new QueryWrapper<Blog>().eq("id", id).eq(STATUS, 0);
//        if(Objects.nonNull(loginUserId)) {
//            blogQueryWrapper.or().eq("id", id).eq("user_id", loginUserId);
//        }
//        Blog blog = blogService.getOne(blogQueryWrapper);
//        Assert.notNull(blog, "该博客已被删除或仅作者可见");

        return Result.succ("blog");
    }
}
