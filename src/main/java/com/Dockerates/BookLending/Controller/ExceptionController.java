package com.Dockerates.BookLending.Controller;


import com.Dockerates.BookLending.Entity.ErrorMessage;
import com.Dockerates.BookLending.Exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@ControllerAdvice
@ResponseStatus
public class ExceptionController extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BookLended.class)
    public ResponseEntity<ErrorMessage> handleBookAlreadyLendedException(BookLended exception){
        ErrorMessage message=new ErrorMessage(HttpStatus.BAD_REQUEST,exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(StudentNotFoundException.class)
    public ResponseEntity<ErrorMessage> studentNotFoundException(StudentNotFoundException exception){
        ErrorMessage message= new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<ErrorMessage> bookNotFoundException(BookNotFoundException exception){
        ErrorMessage message= new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(APIError.class)
    public ResponseEntity<ErrorMessage> ClientNotFoundException(APIError exception){
        ErrorMessage message= new ErrorMessage(HttpStatus.BAD_REQUEST,exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity<ErrorMessage> handleSQLIntegrityException(SQLIntegrityConstraintViolationException exception){
        ErrorMessage message=new ErrorMessage(HttpStatus.BAD_REQUEST,"The Student or Book or both don't exist");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(UserDuplicateEmailException.class)
    public ResponseEntity<ErrorMessage> handleUserDuplicateEmailException(UserDuplicateEmailException exception){
        ErrorMessage message=new ErrorMessage(HttpStatus.BAD_REQUEST,"A user with given email already exists.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> handleUserNotFoundException(UserNotFoundException exception){
        ErrorMessage message=new ErrorMessage(HttpStatus.BAD_REQUEST,"Requested user not found.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }

    @ExceptionHandler(UserWrongPasswordException.class)
    public ResponseEntity<ErrorMessage> handleUserWrongPasswordException(UserWrongPasswordException exception){
        ErrorMessage message=new ErrorMessage(HttpStatus.BAD_REQUEST,"Wrong password for this user.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(message);
    }
}
