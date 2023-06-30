package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Repository.BookLendingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.swing.text.html.Option;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookLendingServiceImpl implements BookLendingService {

    private final BookLendingRepository bookLendingRepository;

    @Override
    public BookLendingEntity LendBook(BookLendingEntity bookLending) {
        Optional<BookLendingEntity> bookLending1=bookLendingRepository.findByStudentIdAndBookCodeAndIssued(bookLending.getStudentId(),bookLending.getBookCode(),true);
        if(bookLending1.isPresent()){
            throw new RuntimeException(); //need to add exception handler
        }
        bookLending.setIssued(true);
        bookLending.setIssueDate(new Date());
       return bookLendingRepository.save(bookLending);
    }

    @Override
    public BookLendingEntity ReturnBook(int transactionId) {
        Optional<BookLendingEntity> bookLending=bookLendingRepository.findByTransactionIdAndIssued(transactionId,true);
        if(bookLending.isPresent()){
            BookLendingEntity bookLending1=bookLending.get();
            bookLending1.setIssued(false);
            bookLending1.setReturnDate(new Date());
            return bookLendingRepository.save(bookLending1);
        }

        throw new RuntimeException(); //need to add exception handler
    }
}
