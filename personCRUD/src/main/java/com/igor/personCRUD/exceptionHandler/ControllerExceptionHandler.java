package com.igor.personCRUD.exceptionHandler;

import com.igor.personCRUD.exception.ExceptionResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Date;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> MethodArgumentNotValid(MethodArgumentNotValidException exception) {
        ExceptionResponse response = new ExceptionResponse(new Date(),
                "Invalid data at create user",
                "422");

        return new ResponseEntity<>(response, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<Object> resourceNotFound(ResourceNotFoundException exception) {
        ExceptionResponse response = new ExceptionResponse(new Date(),
                exception.getMessage(),
                "404");

        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UserAlreadyRegisteredException.class)
    public ResponseEntity<Object> emailAlreadyUsed(UserAlreadyRegisteredException exception) {
        ExceptionResponse response = new ExceptionResponse(new Date(),
                exception.getMessage(),
                "409");

        return new ResponseEntity<>(response, HttpStatus.CONFLICT);
    }


}
