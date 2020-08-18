package com.wzx.task;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.wzx.entity.LogInfo;
import com.wzx.service.LogInfoService;
import java.time.LocalDateTime;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时清理日志.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/19</pre>
 */
@Slf4j
@Component
public class LogScheduledClearTask {

    @Value("${log.keep.day-time}")
    private int keepDayTime;

    @Resource
    private LogInfoService logInfoService;

    @Scheduled(cron = "0 0 5 * * ?")
    public void scheduledClearLog() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oldDateTime = now.minusDays(keepDayTime);
        QueryWrapper<LogInfo> queryWrapper = new QueryWrapper<LogInfo>().lt("operation_time", oldDateTime);
        boolean flag = logInfoService.remove(queryWrapper);
        log.info("now:{} ,doMethod scheduledClearLog flag:{}", now, flag);
    }

}
