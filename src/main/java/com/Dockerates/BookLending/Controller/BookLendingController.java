package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Constants;
import com.Dockerates.BookLending.Entity.*;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.BookNotFoundException;
import com.Dockerates.BookLending.Exception.APIError;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;
import com.Dockerates.BookLending.Service.BookLendingService;
import com.Dockerates.BookLending.Service.BookWebClient;
import com.Dockerates.BookLending.Service.StudentWebClient;
import com.Dockerates.BookLending.Service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/booklending")
public class BookLendingController {

    private final BookLendingService bookLendingService;
    private final StudentWebClient studentWebClient;
    private final BookWebClient bookWebClient;
    

    @GetMapping("/students/rollNo/{rollNo}")
    public Mono<StudentResp> getStudentByRollNo(@PathVariable String rollNo) {
        Mono<StudentResp> respMono = this.studentWebClient.getWebClient().get().uri(Constants.StudentUrl + "api/students/rollNo/" + rollNo).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "A student with that roll number does not exist"));
                    } else if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(StudentResp.class);

        return respMono;
    }

    @GetMapping("/books/code/{book_code}")
    public Mono<BookResp> getBookByCode(@PathVariable String book_code) {
        Mono<BookResp> respMono = this.bookWebClient.getWebClient().get().uri(Constants.BookUrl + "api/books/code/" + book_code).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "A book with that code does not exist"));
                    } else if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(BookResp.class);

        return respMono;
    }

    @GetMapping("/students")
    public Flux<Map> getAllStudents() {
        Flux<Map> respMono = this.studentWebClient.getWebClient().get().uri(Constants.StudentUrl + "api/students").retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "A student with that roll number does not exist"));
                    } else if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToFlux(Map.class);

        return respMono;
    }

    @GetMapping("/books")
    public Flux<Map> getAllBooks() {
        Flux<Map> respMono = this.bookWebClient.getWebClient().get().uri(Constants.BookUrl + "api/books").retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "A book with that code does not exist"));
                    } else if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToFlux(Map.class);

        return respMono;
    }

    @PostMapping("/students")
    public Mono<StudentResp> addStudent(@RequestBody StudentResp request) {
        Mono<StudentResp> respMono = this.studentWebClient.getWebClient().post().uri(Constants.StudentUrl + "api/students").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(StudentResp.class);

        return respMono;
    }

    @PostMapping("/books")
    public Mono<BookResp> addBook(@RequestBody BookResp request) {
        Mono<BookResp> respMono = this.bookWebClient.getWebClient().post().uri(Constants.BookUrl + "api/books").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(BookResp.class);
        return respMono;
    }

    @PutMapping("/students")
    public Mono<StudentResp> updateStudent(@RequestBody StudentResp request) {
        Mono<StudentResp> respMono = this.studentWebClient.getWebClient().put().uri(Constants.StudentUrl + "api/students").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(StudentResp.class);
        return respMono;
    }

    @PutMapping("/books")
    public Mono<BookResp> updateBook(@RequestBody BookResp request) {
        Mono<BookResp> respMono = this.bookWebClient.getWebClient().put().uri(Constants.BookUrl + "api/books").contentType(MediaType.APPLICATION_JSON).bodyValue(request).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(BookResp.class);
        return respMono;
    }

    @DeleteMapping("/students/RollNo/{rollNo}")
    public Mono<String> deleteStudentByRollNo(@PathVariable String rollNo) {
        Mono<String> respMono = this.studentWebClient.getWebClient().delete().uri(Constants.StudentUrl + "api/students/rollNo/" + rollNo).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(String.class);
        return respMono;
    }

    @DeleteMapping("/books/code/{book_code}")
    public Mono<String> deleteBookByCode(@PathVariable String book_code) {
        Mono<String> respMono = this.bookWebClient.getWebClient().delete().uri(Constants.BookUrl + "api/books/code/" + book_code).retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToMono(String.class);
        return respMono;
    }


    @PostMapping("/lendBook")   // This route allows to lend a book to a student
    public BookLendingEntity lendBook( @RequestBody BookLendingEntity bookLendingEntity) throws BookLended {
        return bookLendingService.LendBook(bookLendingEntity);
    }

    @PostMapping("/returnBook/{transactionId}")     // This route allow to return a book with the transaction Id as parameter
    public BookLendingEntity returnBook(@PathVariable int transactionId) throws BookLended {
        return bookLendingService.ReturnBook(transactionId);
    }

    @GetMapping("/getBook/{rollNo}")    //Get all the books a student owns
    public List<Book> getBookDetails(@PathVariable String rollNo) throws StudentNotFoundException, APIError {

        return this.bookLendingService.getBookDetails(rollNo);
    }

    @GetMapping("/getStudent/{bookCode}")    //Get all the students who own a book
    public List<Student> getStudentDetails(@PathVariable String bookCode) throws BookNotFoundException, APIError {
        return this.bookLendingService.getStudentDetails(bookCode);
    }

}

