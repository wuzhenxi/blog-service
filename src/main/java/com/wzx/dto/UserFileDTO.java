package com.wzx.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.Data;

/**
 * 删除文件DTO类.
 *
 * @author Jesse
 * @version 1.0
 * @since <pre>2020/8/22</pre>
 */
@Data
public class UserFileDTO {

    @NotNull(message = "用户ID不能为null")
    private Long userId;

    private String fileName;

    @NotBlank(message = "文件链接不能为空")
    private String fileUrl;

}
