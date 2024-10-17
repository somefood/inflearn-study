package sample.cafekiosk.spring.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import sample.cafekiosk.spring.api.ApiResponse;

@RestControllerAdvice
public class ApiControllerAdvice {
    
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BindException.class) // validation 예외는 이걸로 잡힘
    public ApiResponse<Object> bindException(BindException e) {
        return ApiResponse.of(
                HttpStatus.BAD_REQUEST, 
                e.getBindingResult().getAllErrors().get(0).getDefaultMessage(),
                null
        );
    }
}
