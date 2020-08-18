package com.wzx.common.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 日志查询实体配.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/19</pre>
 */
@Data
public class LogDTO implements Serializable {
    private String operater;

    private String method;

    @NotNull
    private LocalDateTime startDateTime;

    @NotNull
    private LocalDateTime endDateTime;

    private String city;

    private int currentPage = 1;

    private int pageSize = 10;
}
