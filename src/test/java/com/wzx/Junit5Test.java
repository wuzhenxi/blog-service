/*
 * Copyright (c) 2020. 天喻软件
 */

package com.wzx;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * File Description.
 *
 * @author wuzhenxi
 * @version 1.0
 * @since <pre>2020/8/13</pre>
 */
@Slf4j
public class Junit5Test {

    @Test
    @DisplayName("测试Java8 LocalDateTime")
    public void testLocalDateTime() {
        LocalDateTime now = LocalDateTime.now();
        int year = now.getYear();
        int month = now.getMonth().getValue();
        int dayOfMonth = now.getDayOfMonth();
        log.info("path:{}", year + File.separator + month + File.separator + dayOfMonth);
        log.info("year:{},month:{},dayOfMonth:{}", year, month, dayOfMonth);
    }

    @Test
    @DisplayName("测试Java8 LocalDateTime.now() 7天前")
    public void testLocalDateTimeMinu() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oldDateTime = now.minusDays(7);

        Duration between = Duration.between(oldDateTime, now);
        long days = between.toDays();
        long hours = between.toHours();

        log.info("now:{},oldDateTime:{},间隔天数:{},间隔小时数:{}", now, oldDateTime, days, hours);
    }


}
