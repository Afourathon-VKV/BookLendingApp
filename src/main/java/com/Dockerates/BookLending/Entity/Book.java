package com.Dockerates.BookLending.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    private int id;
    private String title; // Title of Book
    private String author; // Author of Book
    private String description; // Description of Book
    private String code;

    private BookLendingEntity bookLendingEntity;


}
