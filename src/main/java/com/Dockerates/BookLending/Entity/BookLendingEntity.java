package com.Dockerates.BookLending.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.NonNull;

import java.util.Date;

@Entity
@Table(name="BookLending")
@Embeddable
public class BookLendingEntity {

    //need to add foreign key dependency
    @NonNull
    @Column(name="StudentId", nullable = false)
    private int studentId;
    @NonNull
    @Column(name="BookCode",nullable = false)
    private int bookCode;

    @NonNull
    @Column(name="IssueDate", nullable = false)
    private Date issueDate;

    @Column(name="ReturnDate")
    private Date returnDate;

    @Column(name="Issued")
    private boolean issued;

}
