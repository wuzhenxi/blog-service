package com.wzx.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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

    private static final long serialVersionUID = 1L;

    private String operater;

    private String method;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDateTime;

    @NotNull
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDateTime;

    private String city;

    private int currentPage = 1;

    private int pageSize = 10;
}
