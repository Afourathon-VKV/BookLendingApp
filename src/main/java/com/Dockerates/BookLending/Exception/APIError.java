package com.Dockerates.BookLending.Exception;

public class APIError extends CustomException{
    public APIError(String message){
        super(message);
    }
}
