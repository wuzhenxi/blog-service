package com.wzx.common.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Data
public class LoginDTO implements Serializable {

    @NotBlank(message = "昵称不能为空")
    private String username;

    @NotBlank(message = "密码不能为空")
    private String password;
}
