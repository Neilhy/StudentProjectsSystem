package com.scut.cs.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;

/**
 * Created by Jack on 2016/9/5.\
 * 对异常统一处理的类，与返回Controller的处理一样，可以返回视图.
 */
@ControllerAdvice
public class ExceptionProcessor {
    int size = 1;
    @ExceptionHandler(MultipartException.class)
    @ResponseBody
    public String handleFile(Throwable t) {
        System.out.println("正在处理异常...");
        return  "文件超过大小限制:"+size+"M";
    }
}
