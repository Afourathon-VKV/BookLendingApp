package com.Dockerates.BookLending.Exception;

public class BookNotFoundException extends CustomException{
    public BookNotFoundException(String message){
        super(message);
    }
}
