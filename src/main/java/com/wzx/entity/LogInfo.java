package com.wzx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 系统日志记录.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/18</pre>
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_log")
public class LogInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @NotBlank
    private String operater;

    @NotBlank
    private String method;

    @NotBlank
    private String ip;

    private LocalDateTime operationTime;

    private String country;

    private String region;

    private String city;

    private String cityId;

    private String isp;


}
