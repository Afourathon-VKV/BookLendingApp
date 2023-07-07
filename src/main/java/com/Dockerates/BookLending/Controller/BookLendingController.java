package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Constants;
import com.Dockerates.BookLending.Entity.*;
import com.Dockerates.BookLending.Exception.*;
import com.Dockerates.BookLending.Service.BookLendingService;
import com.Dockerates.BookLending.Service.BookWebClient;
import com.Dockerates.BookLending.Service.StudentWebClient;
import com.Dockerates.BookLending.Service.UserService;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
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
    public ResponseEntity<StudentResp> getStudentByRollNo(@PathVariable String rollNo) throws StudentNotFoundException {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<StudentResp> response = restTemplate.exchange( //makes a get request to the api to retrieve all the books
                Constants.StudentUrl + "api/students/rollNo/" + rollNo,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<StudentResp>() {
                });
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new StudentNotFoundException("A student with this roll number does not exist");
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Bad request");
            } else {
                throw new RuntimeException("Client error");
            }
        }
        return response;
    }


    @GetMapping("/books/code/{book_code}")
    public ResponseEntity<BookResp> getBookByCode(@PathVariable String book_code) throws BookNotFoundException {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<BookResp> response = restTemplate.exchange( //makes a get request to the api to retrieve all the books
                Constants.BookUrl + "api/books/code/" + book_code,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BookResp>() {
                });
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new BookNotFoundException("A book with this code does not exist");
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Bad request");
            } else {
                throw new RuntimeException("Client error");
            }
        }
        return response;
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
    public ResponseEntity<StudentResp> addStudent(@RequestBody StudentResp request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<StudentResp> response = restTemplate.postForEntity(Constants.StudentUrl + "api/students", entity, StudentResp.class);
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Bad request");
            } else {
                throw new RuntimeException("Client error");
            }
        }
        return response;
    }


    @PostMapping("/books")
    public ResponseEntity<BookResp> addBook(@RequestBody BookResp request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<BookResp> response = restTemplate.postForEntity(Constants.BookUrl + "api/books", entity, BookResp.class);
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                throw new RuntimeException("Bad request");
            } else {
                throw new RuntimeException("Client error");
            }
        }
        return response;
    }


    @PutMapping("/students")
    public String updateStudent(@RequestBody StudentResp request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        try{
            restTemplate.put(Constants.StudentUrl + "api/students", entity);
        }
        catch (RestClientException e){
            throw new RuntimeException(e.getMessage());
        }
        return "Success!";
    }


    @PutMapping("/books")
    public String updateBook(@RequestBody BookResp request) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        try{
            restTemplate.put(Constants.BookUrl + "api/books", entity);
        }
        catch (RestClientException e){
            throw new RuntimeException(e.getMessage());
        }
        return "Success!";
    }


    @DeleteMapping("/students/RollNo/{rollNo}")
    public String deleteStudentByRollNo(@PathVariable String rollNo) {
        RestTemplate restTemplate = new RestTemplate();

        try{
            restTemplate.delete(Constants.StudentUrl + "api/students/rollNo/" + rollNo);
        }
        catch (RestClientException e){
            throw new RuntimeException(e.getMessage());
        }
        return "Success!";
    }


    @DeleteMapping("/books/code/{book_code}")
    public String deleteBookByCode(@PathVariable String book_code) {
        RestTemplate restTemplate = new RestTemplate();

        try{
            restTemplate.delete(Constants.BookUrl + "api/books/code/" + book_code);
        }
        catch (RestClientException e){
            throw new RuntimeException(e.getMessage());
        }
        return "Success!";
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

