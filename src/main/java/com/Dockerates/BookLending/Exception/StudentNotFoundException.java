package com.Dockerates.BookLending.Exception;

public class StudentNotFoundException extends CustomException{
    public StudentNotFoundException(String message){
        super(message);
    }
}
