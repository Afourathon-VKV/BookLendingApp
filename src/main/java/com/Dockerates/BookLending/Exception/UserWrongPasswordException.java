package com.Dockerates.BookLending.Exception;

public class UserWrongPasswordException extends CustomException {
    public UserWrongPasswordException(String message) {
        super(message);
    }
}
