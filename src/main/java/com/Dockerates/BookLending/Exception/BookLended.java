package com.Dockerates.BookLending.Exception;

public class BookLended extends CustomException{
    public BookLended(String message){
        super(message);
    }
}
