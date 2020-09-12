package com.wzx.common.exception;

import com.wzx.common.lang.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * @version 1.0
 * @author: Jesse
 * @since: 14/08/2020
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = ShiroException.class)
    public Result handler(ShiroException exception) {
        log.error("运行时异常:----------------{}", exception.getMessage());
        return Result.fail(401, exception.getMessage(), null);
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result handler(MethodArgumentNotValidException exception) {
        log.error("实体校验异常:----------------{}", exception.getMessage());
        BindingResult bindingResult = exception.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return Result.fail(objectError.getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = NumberFormatException.class)
    public Result handler(NumberFormatException exception) {
        log.error("数字类型转换异常:----------------{}", exception.getMessage());
        return Result.fail(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public Result handler(IllegalArgumentException exception) {
        log.error("Assert异常:----------------{}", exception.getMessage());
        return Result.fail(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public Result handler(MethodArgumentTypeMismatchException exception) {
        log.error("方法:{}.调用异常.参数字段:{},值为:{},异常:----------------{}",exception.getParameter(), exception.getName(), exception.getValue(), exception.getMessage());
        return Result.fail(String.format("方法调用异常.参数字段:%s,值为:%s,异常信息:%s",exception.getName(), exception.getValue(), exception.getCause()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public Result handler(RuntimeException exception) {
        log.error("运行时异常:----------------{}", exception.getMessage());
        return Result.fail(exception.getMessage());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = Exception.class)
    public Result handler(Exception exception) {
        log.error("程序异常:----------------{}", exception.getMessage());
        return Result.fail(exception.getMessage());
    }

}
