package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Constants;
import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Entity.Student;
import com.Dockerates.BookLending.Entity.StudentResp;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.BookNotFoundException;
import com.Dockerates.BookLending.Exception.APIError;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;
import com.Dockerates.BookLending.Service.BookLendingService;
import com.Dockerates.BookLending.Service.BookWebClient;
import com.Dockerates.BookLending.Service.StudentWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/booklending")
public class BookLendingController {

    private final BookLendingService bookLendingService;
    private final StudentWebClient studentWebClient;
    private final BookWebClient bookWebClient;

    @GetMapping("/students/rollNo/{rollNo}")
    public Mono<String> getStudentByRollNo(@PathVariable String rollNo) {
        return this.studentWebClient.getWebClient().get().uri(Constants.StudentUrl + "api/students/rollNo/" + rollNo).retrieve().bodyToMono(String.class);
    }

    @GetMapping("/books/code/{book_code}")
    public Mono<String> getBookByCode(@PathVariable String book_code) {
        return this.bookWebClient.getWebClient().get().uri(Constants.BookUrl + "api/books/code/" + book_code).retrieve().bodyToMono(String.class);
    }

    @PostMapping("/students")
    public Mono<String> addStudent(@RequestBody StudentResp request) {
        return this.studentWebClient.getWebClient().post().uri(Constants.StudentUrl + "api/students").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().bodyToMono(String.class);
    }

    @PostMapping("/books")
    public Mono<String> addBook(Object request) {
        return this.bookWebClient.getWebClient().post().uri(Constants.BookUrl + "api/books").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().bodyToMono(String.class);
    }

    @PutMapping("/students")
    public Mono<String> updateStudent(@RequestBody StudentResp request) {
        return this.studentWebClient.getWebClient().put().uri(Constants.StudentUrl + "api/students").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().bodyToMono(String.class);
    }

    @PutMapping("/books")
    public Mono<String> updateBook(Object request) {
        return this.bookWebClient.getWebClient().put().uri(Constants.BookUrl + "api/books").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().bodyToMono(String.class);
    }

    @DeleteMapping("/students/RollNo/{rollNo}")
    public Mono<String> deleteStudentByRollNo(@PathVariable String rollNo) {
        return this.studentWebClient.getWebClient().delete().uri(Constants.StudentUrl + "api/students/rollNo/" + rollNo).retrieve().bodyToMono(String.class);
    }

    @DeleteMapping("/books/code/{book_code}")
    public Mono<String> deleteBookByCode(@PathVariable String book_code) {
        return this.bookWebClient.getWebClient().delete().uri(Constants.BookUrl + "api/books/code/" + book_code).retrieve().bodyToMono(String.class);
    }


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

