package com.Dockerates.BookLending.Entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student { //used when retrieving student from student api
    private int id; // Primary key of the student table to identify students

    private String rollNo; // Roll number of the student


    private String name; // Name of the student

    private String email; // Email of the student

    private String phone; //phone number of student

    private BookLendingEntity bookLendingEntity;
}
