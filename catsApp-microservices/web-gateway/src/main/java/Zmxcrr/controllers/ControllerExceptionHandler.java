package Zmxcrr.controllers;

import Zmxcrr.users.exceptions.UserServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(UserServiceException.class)
    public ResponseEntity<String> unknownEntityException(UserServiceException e) {
        return ResponseEntity
                .status(HttpStatus.EXPECTATION_FAILED)
                .body(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> exception(Exception e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getMessage());
    }
}
