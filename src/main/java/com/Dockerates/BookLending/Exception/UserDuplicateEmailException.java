package com.Dockerates.BookLending.Exception;
public class UserDuplicateEmailException extends  CustomException {
    public UserDuplicateEmailException (String message) {
        super(message);
    }
}
