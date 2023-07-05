package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Entity.Student;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.BookNotFoundException;
import com.Dockerates.BookLending.Exception.APIError;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;
import com.Dockerates.BookLending.Service.BookLendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booklending")
public class BookLendingController {

    private final BookLendingService bookLendingService;
    @PostMapping("/lendBook")
    public BookLendingEntity lendBook( @RequestBody BookLendingEntity bookLendingEntity) throws BookLended { // This route allows to lend a book to a student
        return bookLendingService.LendBook(bookLendingEntity);
    }

    @PostMapping("/returnBook/{transactionId}")
    public BookLendingEntity returnBook(@PathVariable int transactionId) throws BookLended { // This route allow to return a book with the transaction Id as parameter
        return bookLendingService.ReturnBook(transactionId);
    }

    @GetMapping("/getBook/{rollNo}")
    public List<Book> getBookDetails(@PathVariable String rollNo) throws StudentNotFoundException, APIError { //Get all the books a student owns

        return this.bookLendingService.getBookDetails(rollNo);
    }

    @GetMapping("/getStudent/{bookCode}")
    public List<Student> getStudentDetails(@PathVariable String bookCode) throws BookNotFoundException, APIError { //Get all the students who own a book
        return this.bookLendingService.getStudentDetails(bookCode);
    }
}

