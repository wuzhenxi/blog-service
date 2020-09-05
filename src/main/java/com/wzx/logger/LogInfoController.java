package com.wzx.logger;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.google.common.base.Strings;
import com.wzx.dto.LogDTO;
import com.wzx.common.lang.Result;
import com.wzx.entity.LogInfo;
import com.wzx.service.LogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.time.Duration;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.validation.annotation.Validated;
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

    @Resource
    private LogInfoService logInfoService;

    @RequiresAuthentication
    @PostMapping("/query")
    @ApiOperation("日志查看")
    public Result queryLog(@Validated @RequestBody LogDTO logDTO) {
        Page page = new Page(logDTO.getCurrentPage(), logDTO.getPageSize());
        QueryWrapper<LogInfo> queryWrapper = handlerQueryParam(logDTO);
        IPage pageData = logInfoService.page(page, queryWrapper.orderByDesc("operation_time"));
        return Result.succ(pageData);
    }

    private QueryWrapper<LogInfo> handlerQueryParam(LogDTO logDTO) {
        Assert.isFalse(Duration.between(logDTO.getStartDateTime(), logDTO.getEndDateTime()).isNegative(),
                "查询开始时间与结束时间有误");
        QueryWrapper<LogInfo> queryWrapper = new QueryWrapper<>();
        queryWrapper.lambda().and(item -> item.ge(LogInfo::getOperationTime, logDTO.getStartDateTime())
                .le(LogInfo::getOperationTime, logDTO.getEndDateTime()));
        if (!Strings.isNullOrEmpty(logDTO.getOperater())) {
            queryWrapper.eq("operater", logDTO.getOperater());
        }

        if (!Strings.isNullOrEmpty(logDTO.getMethod())) {
            queryWrapper.eq("method", logDTO.getMethod());
        }

        if (!Strings.isNullOrEmpty(logDTO.getCity())) {
            queryWrapper.eq("city", logDTO.getCity());
        }
        return queryWrapper;
    }
}
