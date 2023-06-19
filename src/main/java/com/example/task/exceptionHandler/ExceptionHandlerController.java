package com.example.task.exceptionHandler;


import com.example.task.dto.response.ResponseDto;
import com.example.task.exp.EmailAlreadyExistsException;
import com.example.task.exp.JWTTokenExpiredException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@ControllerAdvice
public class ExceptionHandlerController extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("timestamp", new Date());
        body.put("status", status.value());

        List<String> errors = new LinkedList<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        body.put("errors", errors);
        return new ResponseEntity<>(body, headers, status);
    }

    @ExceptionHandler({EmailAlreadyExistsException.class})
    private ResponseEntity<?> handler(EmailAlreadyExistsException e) {
        return ResponseDto.response(e.getMessage(), HttpStatus.BAD_REQUEST);

//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler({JWTTokenExpiredException.class})
    private ResponseEntity<?> handler(JWTTokenExpiredException e) {
        return ResponseDto.response(e.getMessage(), HttpStatus.UNAUTHORIZED);

//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    @ExceptionHandler({MailSendException.class})
    private ResponseEntity<?> handler(MailSendException e) {
        return ResponseDto.response(e.getMessage(), HttpStatus.CONFLICT);

//        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

}
