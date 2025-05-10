package Zmxcrr.controllers;

import Zmxcrr.exceptions.FriendshipException;
import Zmxcrr.exceptions.UnknownEntityIdException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(UnknownEntityIdException.class)
    public ResponseEntity<String> unknownEntityIdException(UnknownEntityIdException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler(FriendshipException.class)
    public ResponseEntity<String> friendshipException(FriendshipException e) {
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