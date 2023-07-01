package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Entity.Student;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.BookNotFoundException;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;
import com.Dockerates.BookLending.Service.BookLendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookLendingController {

    private final BookLendingService bookLendingService;
    @PostMapping("/lendBook")
    public BookLendingEntity lendBook( @RequestBody BookLendingEntity bookLendingEntity) throws BookLended {
        return bookLendingService.LendBook(bookLendingEntity);
    }

    @PostMapping("returnBook/{transactionId}")
    public BookLendingEntity returnBook(@PathVariable int transactionId) throws BookLended {
        return bookLendingService.ReturnBook(transactionId);
    }

    @GetMapping("getBook/{studentId}")
    public List<Book> getBookDetails(@PathVariable int studentId) throws StudentNotFoundException {

        return this.bookLendingService.getBookDetails(studentId);
    }

    @GetMapping("getStudent/{bookId}")
    public List<Student> getStudentDetails(@PathVariable int bookId) throws BookNotFoundException {
        return this.bookLendingService.getStudentDetails(bookId);
    }
}

