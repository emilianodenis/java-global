package com.example.demo.controller.advice;

import com.example.demo.model.exception.ProfessionNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProfessionNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ProfessionNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String professionNotFoundHandler(ProfessionNotFoundException ex) {
        return ex.getMessage();
    }
}
