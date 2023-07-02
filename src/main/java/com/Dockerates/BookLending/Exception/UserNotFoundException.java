package com.Dockerates.BookLending.Exception;

public class UserNotFoundException extends  CustomException{
    public UserNotFoundException(String message) {
        super(message);
    }
}
