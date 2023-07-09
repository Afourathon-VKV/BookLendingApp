package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Constants;
import com.Dockerates.BookLending.Entity.*;
import com.Dockerates.BookLending.Exception.*;
import com.Dockerates.BookLending.Service.*;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@RequestMapping("/api/booklending")
public class BookLendingController {

    private final BookLendingService bookLendingService;
    private final StudentWebClient studentWebClient;
    private final BookWebClient bookWebClient;
    private final Logger logger = LoggerFactory.getLogger(BookLendingController.class);

    @GetMapping("/students/rollNo/{rollNo}")
    public ResponseEntity<StudentResp> getStudentByRollNo(@PathVariable String rollNo) throws StudentNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Trying to get details of student of roll Number " + rollNo);
        ResponseEntity<StudentResp> response = restTemplate.exchange( //makes a get request to the api to retrieve all the books
                Constants.StudentUrl + "api/students/rollNo/" + rollNo,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<StudentResp>() {
                });
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("A student with roll number" + rollNo + "does not exist");
                throw new StudentNotFoundException("A student with this roll number does not exist");
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.error("Bad request while fetching student with roll number" + rollNo);
                throw new RuntimeException("Bad request");
            } else {
                logger.error("Client error while fetching student with roll number" + rollNo);
                throw new RuntimeException("Client error");
            }
        }
        logger.info("Successfully fetched details of student of roll Number " + rollNo);
        return response;
    }


    @GetMapping("/books/code/{book_code}")
    public ResponseEntity<BookResp> getBookByCode(@PathVariable String book_code) throws BookNotFoundException {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Trying to get details of book of code " + book_code);
        ResponseEntity<BookResp> response = restTemplate.exchange( //makes a get request to the api to retrieve all the books
                Constants.BookUrl + "api/books/code/" + book_code,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<BookResp>() {
                });
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                logger.error("A book with code" + book_code + "does not exist");
                throw new BookNotFoundException("A book with this code does not exist");
            } else if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.error("Bad request while fetching book with code" + book_code);
                throw new RuntimeException("Bad request");
            } else {
                logger.error("Client error while fetching book with code" + book_code);
                throw new RuntimeException("Client error");
            }
        }
        logger.info("Successfully fetched details of book of code " + book_code);
        return response;
    }


    @GetMapping("/students")
    public Flux<Map> getAllStudents() {
        logger.info("Trying to fetch all students");
        Flux<Map> respMono = this.studentWebClient.getWebClient().get().uri(Constants.StudentUrl + "api/students").retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        logger.error("Could not fetch all students");
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "A student with that roll number does not exist"));
                    } else if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        logger.error("Bad Request while trying to fetch all students");
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        logger.error("Client error while fetching all students");
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToFlux(Map.class);
        logger.info("Successfully fetched all students");
        return respMono;
    }


    @GetMapping("/books")
    public Flux<Map> getAllBooks() {
        logger.info("Trying to fetch all books");
        Flux<Map> respMono = this.bookWebClient.getWebClient().get().uri(Constants.BookUrl + "api/books").retrieve().onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.NOT_FOUND) {
                        logger.error("Could not fetch all books");
                        return Mono.error(new ResponseStatusException(HttpStatus.NOT_FOUND, "A book with that code does not exist"));
                    } else if (clientResponse.statusCode() == HttpStatus.BAD_REQUEST) {
                        logger.error("Bad Request while trying to fetch all books");
                        return Mono.error(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request"));
                    } else {
                        logger.error("Client error while trying to fetch all books");
                        return Mono.error(new ResponseStatusException(clientResponse.statusCode(), "Client error"));
                    }
                })
                .bodyToFlux(Map.class);
        logger.info("Successfully fetched all books");
        return respMono;
    }

    @PostMapping("/students")
    public ResponseEntity<StudentResp> addStudent(@RequestBody StudentResp request) {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Trying to add student of roll number "+ request.getRollNo());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<StudentResp> response = restTemplate.postForEntity(Constants.StudentUrl + "api/students", entity, StudentResp.class);
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.info("Bad request while trying to add student of roll number " + request.getRollNo());
                throw new RuntimeException("Bad request");
            } else {
                logger.info("Client Error while trying to add student of roll number " + request.getRollNo());
                throw new RuntimeException("Client error");
            }
        }
        logger.info("Successfully added student of roll number "+ request.getRollNo());
        return response;
    }


    @PostMapping("/books")
    public ResponseEntity<BookResp> addBook(@RequestBody BookResp request) {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Trying to add book of code "+ request.getCode());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        ResponseEntity<BookResp> response = restTemplate.postForEntity(Constants.BookUrl + "api/books", entity, BookResp.class);
        if(response.getStatusCode().is4xxClientError()){
            if (response.getStatusCode() == HttpStatus.BAD_REQUEST) {
                logger.info("Bad request while trying to add book of code " + request.getCode());
                throw new RuntimeException("Bad request");
            } else {
                logger.info("Client Error while trying to add book of code " + request.getCode());
                throw new RuntimeException("Client error");
            }
        }
        logger.info("Successfully added book of code " + request.getCode());
        return response;
    }


    @PutMapping("/students")
    public String updateStudent(@RequestBody StudentResp request) {
        logger.info("Trying to update Student of roll no " + request.getRollNo());
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        try{
            restTemplate.put(Constants.StudentUrl + "api/students", entity);
        }
        catch (RestClientException e){
            logger.error("Could not update Student of roll no " + request.getRollNo() + " because: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        logger.info("Successfully updated Student of roll no " + request.getRollNo());
        return "Success!";
    }


    @PutMapping("/books")
    public String updateBook(@RequestBody BookResp request) {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Trying to update Book of code " + request.getCode());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Object> entity = new HttpEntity<>(request, headers);
        try{
            restTemplate.put(Constants.BookUrl + "api/books", entity);
        }
        catch (RestClientException e){
            logger.error("Could not update Book of code " + request.getCode() + " because: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        logger.info("Successfully updated book of code " + request.getCode());
        return "Success!";
    }


    @DeleteMapping("/students/RollNo/{rollNo}")
    public String deleteStudentByRollNo(@PathVariable String rollNo) {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Trying to delete Student of roll no " + rollNo);
        try{
            restTemplate.delete(Constants.StudentUrl + "api/students/rollNo/" + rollNo);
        }
        catch (RestClientException e){
            logger.error("Could not delete Student of roll no " + rollNo + " because: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        logger.info("Successfully deleted student of roll no " + rollNo);
        return "Success!";
    }


    @DeleteMapping("/books/code/{book_code}")
    public String deleteBookByCode(@PathVariable String book_code) {
        RestTemplate restTemplate = new RestTemplate();
        logger.info("Trying to delete Book of code " + book_code);
        try{
            restTemplate.delete(Constants.BookUrl + "api/books/code/" + book_code);
        }
        catch (RestClientException e){
            logger.error("Could not delete Book of code " + book_code + " because: " + e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
        logger.info("Successfully deleted book of code " + book_code);
        return "Success!";
    }


    @PostMapping("/lendBook")   // This route allows to lend a book to a student
    public BookLendingEntity lendBook( @RequestBody BookLendingEntity bookLendingEntity) throws BookLended {
        return bookLendingService.LendBook(bookLendingEntity);
    }

    @PutMapping("/returnBook/{transactionId}")     // This route allow to return a book with the transaction Id as parameter
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

