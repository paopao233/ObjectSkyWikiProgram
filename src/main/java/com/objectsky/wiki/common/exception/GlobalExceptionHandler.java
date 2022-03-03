package com.objectsky.wiki.common.exception;


import com.objectsky.wiki.common.resp.CommonResp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 运行时异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = RuntimeException.class)
    public CommonResp handler(RuntimeException e) {
        log.error("运行时异常：--------------{}", e);
        return CommonResp.fail(e.getMessage());
    }

    //    @ResponseStatus(HttpStatus.UNAUTHORIZED)
//    @ExceptionHandler(value = ShiroException.class)
//    public Result handler(ShiroException e) {
//        log.error("运行时异常：--------------{}", e);
//        return Result.fail(401, e.getMessage(), null);
//    }

    /**
     * 实体校验异常 针对Validator的异常返回
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = BindException.class)
    public CommonResp BindExceptionHandler(BindException e) {
        log.error("实体校验异常：--------------{}", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().get(0);
        return CommonResp.fail(objectError.getDefaultMessage());
    }

    /**
     * 实体校验异常 针对MethodArgumentNotValidException
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public CommonResp MethodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e) {
        log.error("实体校验异常：--------------{}", e);
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().get(0);
        return CommonResp.fail(objectError.getDefaultMessage());
    }

    /**
     * Assert异常
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = IllegalArgumentException.class)
    public CommonResp handler(IllegalArgumentException e) {
        log.error("Assert异常：--------------{}", e);
        return CommonResp.fail(e.getMessage());
    }
}
