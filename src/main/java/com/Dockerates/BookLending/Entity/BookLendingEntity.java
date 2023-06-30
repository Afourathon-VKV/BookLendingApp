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
public class BookLendingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transactionId")
    private int transactionId;

    //need to add foreign key dependency
    @Column(name="studentId" , nullable = false)
    private int studentId;

    @Column(name="bookCode", nullable = false)
    private int bookCode;

    //

    @Column(name = "issued", columnDefinition = "boolean default false")
    private boolean issued;

    @Column(name="issueDate")
    private Date issueDate;

    @Column(name="returnDate")
    private Date returnDate;

}
