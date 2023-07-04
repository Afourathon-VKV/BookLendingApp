package com.Dockerates.BookLending.Repository;

import com.Dockerates.BookLending.Entity.BookLendingEntity;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@AutoConfigureTestDatabase
public class BookLendingAppRepoTest {

    @Autowired
    private BookLendingRepository bookLendingRepository;

//    @Test
//    @Order(1)
//    @Rollback(value = false)
//    public void LendBookTestSuccess(){
//        int transactionId = 1;
//        // Student with ID 40 borrows book of id 100 for the test
//        BookLendingEntity bookLendingEntity = new BookLendingEntity(transactionId,40,100,true,new Date(),null);
//        bookLendingRepository.save(bookLendingEntity);
//        Assertions.assertEquals(40, bookLendingRepository.findByTransactionIdAndIssued(transactionId, true).get().getStudentId());
//    }
//
//    @Test
//    @Order(2)
//    @Rollback(value = false)
//    public void getBookDetailsTest(){
//        int studentId = 40;
//        List<BookLendingEntity> studentBooks=bookLendingRepository.findByStudentIdAndIssued(studentId,true);
//        // Checking if the book borrowed by studentId 40 is bookId 100
//        Assertions.assertEquals(100,studentBooks.get(0).getBookCode());
//    }
//
//    @Test
//    @Order(3)
//    @Rollback(value = false)
//    public void getStudentDetailsTest(){
//        int bookId = 100;
//        List<BookLendingEntity> studentBooks=bookLendingRepository.findByBookCodeAndIssued(bookId,true);
//        // Checking if the student who borrowed bookId 100 is studentId 40
//        Assertions.assertEquals(40,studentBooks.get(0).getStudentId());
//    }
//
//    @Test
//    @Order(4)
//    @Rollback(value = false)
//    public void ReturnBookTest(){
//        int transactionId = 1;
//        Optional<BookLendingEntity> bookLending=bookLendingRepository.findByTransactionIdAndIssued(transactionId,true);
//        BookLendingEntity bookLending1=bookLending.get();
//        bookLending1.setIssued(false);
//        bookLending1.setReturnDate(new Date());
//        bookLendingRepository.save(bookLending1);
//        // Checking if the book got returned.
//        Assertions.assertFalse(bookLendingRepository.findByTransactionIdAndIssued(transactionId, true).isPresent());
//    }
}
