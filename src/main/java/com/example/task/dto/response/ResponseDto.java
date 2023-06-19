package com.example.task.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseDto<T> {

    private T data;
    private String errorMessage;
    private Long timestamp;


    public ResponseDto(T data, String errorMessage) {
        this.data = data;
        this.errorMessage = errorMessage;
        this.timestamp = System.currentTimeMillis();
    }

    public ResponseDto(T data) {
        this.data = data;
        this.errorMessage = "";
        this.timestamp = System.currentTimeMillis();
    }

    public static <T> ResponseEntity<ResponseDto<T>> response(T data, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ResponseDto<>(data), httpStatus);
    }

    public static <T> ResponseEntity<ResponseDto<T>> response(String errorMessage, HttpStatus httpStatus) {
        return new ResponseEntity<>(new ResponseDto<>(null, errorMessage), httpStatus);
    }
}
