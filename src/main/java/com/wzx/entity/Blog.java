package com.wzx.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("m_blog")
public class Blog implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Long userId;

    @NotBlank(message = "标题不能为空")
    private String title;

    @NotBlank(message = "摘要不能为空")
    private String description;

    @NotBlank(message = "内容不能为空")
    private String content;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime created;

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime modify;

    @NotNull(message = "状态不能为空")
    private Integer status;

    private Integer isTop = 0;

    // 附件标识文件位置 后续可单独建张放附件的表
    private String attachment;


}
