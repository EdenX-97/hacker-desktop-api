package com.eden.hackerdesktopapi.handler;

import com.eden.hackerdesktopapi.constant.consists.Result;
import com.eden.hackerdesktopapi.utils.ReturnResultUtil;
import com.rometools.rome.io.FeedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;


@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(IOException.class)
    public Result<?> IOException(IOException e) {
        String message = e.getMessage();
        //if (message.contains("not present")) {
        //    message = "Input cannot be null";
        //}

        return ReturnResultUtil.failed(e);
    }

    @ExceptionHandler(FeedException.class)
    public Result<?> FeedException(FeedException e) {
        String message = e.getMessage();
        //if (message.contains("not present")) {
        //    message = "Input cannot be null";
        //}

        return ReturnResultUtil.failed(message);
    }
}
