package com.example.test.exceptions;

import com.example.test.responses.ApiResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlerGeneralException(Exception ex, HttpServletRequest request){
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Co loi xay ra: "+ ex.getMessage())
                .data(null)
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> handlerValidationException(MethodArgumentNotValidException ex){
        StringBuilder errMess = new StringBuilder();
        ex.getBindingResult().getAllErrors().forEach((error)->{
            errMess.append(error.getDefaultMessage()).append("; ");
        });
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Co loi xay ra: "+ errMess.toString())
                .data(null)
                .build();
        return ResponseEntity.badRequest().body(apiResponse);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse> handlerResourceNotfoundException(ResourceNotFoundException ex){
        ApiResponse apiResponse = ApiResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message("Co loi xay ra: "+ ex.getMessage())
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiResponse);
    }
}
