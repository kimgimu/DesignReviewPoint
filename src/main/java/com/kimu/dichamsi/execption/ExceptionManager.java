package com.kimu.dichamsi.execption;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionManager {

    @ExceptionHandler(AppException.class)
    public String runtimeExceptionHandler(AppException e){
        return e.getErrorCode().name()+" "+e.getMessage();
    }

    @ExceptionHandler(RuntimeException.class)
    public String runtimeExceptionHandler(RuntimeException e){
        return e.getMessage();
    }
}
