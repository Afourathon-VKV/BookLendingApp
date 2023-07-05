package com.Dockerates.BookLending.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book { // Book class with all the book related parameters and used to retrieve objects via a api call to book service
    private int id;
    private String title; // Title of Book
    private String author; // Author of Book
    private String description; // Description of Book
    private String code;

    private BookLendingEntity bookLendingEntity;


}
