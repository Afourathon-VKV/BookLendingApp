package com.Dockerates.BookLending.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Entity
@Table(name="bookLending")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BookLendingEntity { // The tables that stores the history of all transactions where a transaction is lending or returning a book to a student

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private int transactionId;

    //need to add foreign key dependency
    @Column(name="rollNo" , nullable = false)
    private String rollNo; //rollNo of student

    @Column(name="bookCode", nullable = false)
    private String bookCode; //bookCode of the book

    //

    @Column(name = "issued", columnDefinition = "boolean default false")
    private boolean issued; //boolean value to check if a book is issued or is available

    @Column(name="issueDate")
    private Date issueDate;

    @Column(name="returnDate")
    private Date returnDate;

}
