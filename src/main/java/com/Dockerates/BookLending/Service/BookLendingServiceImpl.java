package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Constants;
import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Entity.Student;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.BookNotFoundException;
import com.Dockerates.BookLending.Exception.APIError;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;
import com.Dockerates.BookLending.Repository.BookLendingRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BookLendingServiceImpl implements BookLendingService {

    private final BookLendingRepository bookLendingRepository;
    @Override
    public BookLendingEntity LendBook(BookLendingEntity bookLending) throws BookLended {
        Optional<BookLendingEntity> bookLending1=bookLendingRepository.findByStudentIdAndBookCodeAndIssued(bookLending.getStudentId(),bookLending.getBookCode(),true);
        if(bookLending1.isPresent()){
            throw new BookLended("This Book is lent to another student");
        }
        bookLending.setIssued(true);
        bookLending.setIssueDate(new Date());
       return bookLendingRepository.save(bookLending);
    }

    @Override
    public BookLendingEntity ReturnBook(int transactionId) throws BookLended {
        Optional<BookLendingEntity> bookLending=bookLendingRepository.findByTransactionIdAndIssued(transactionId,true);
        if(bookLending.isPresent()){
            BookLendingEntity bookLending1=bookLending.get();
            bookLending1.setIssued(false);
            bookLending1.setReturnDate(new Date());
            return bookLendingRepository.save(bookLending1);
        }

        throw new BookLended("This Book has already been returned or not been lent yet.");
    }

    @Override
    public List<Book> getBookDetails(int studentId) throws StudentNotFoundException, APIError {
        List<BookLendingEntity> studentbooks=bookLendingRepository.findByStudentIdAndIssued(studentId,true);
        if(!studentbooks.isEmpty()) {
            final String uri = Constants.BookUrl+"/api/books";
            //need to add exception here
            try{
                RestTemplate restTemplate = new RestTemplate();
                //assumes an array
                ResponseEntity<List<Book>> response = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Book>>() {
                        });

                List<Book> objects = response.getBody();
                ArrayList<Book> finallist = new ArrayList<Book>();
                for (int i = 0; i < objects.size(); i++) {
                    for (int j = 0; j < studentbooks.size(); j++) {
                        if (objects.get(i).getId() == studentbooks.get(j).getBookCode()) {
                            objects.get(i).setBookLendingEntity(studentbooks.get(j));
                            finallist.add(objects.get(i));
                            break;
                        }

                    }
                }

                return finallist;
            }
            catch (HttpClientErrorException e){
                throw new APIError(e.getMessage());
            }
            catch (IllegalArgumentException e){
                throw new APIError("URI is not valid");
            }

        }
        throw new StudentNotFoundException("The student doesn't exist or the student has not borrowed any books");
    }

    @Override
    public List<Student> getStudentDetails(int bookId) throws BookNotFoundException, APIError {
        List<BookLendingEntity> bookstudents=bookLendingRepository.findByBookCodeAndIssued(bookId, true);
        if(!bookstudents.isEmpty()) {
            final String uri = Constants.StudentUrl + "/api/students";

            try{

                RestTemplate restTemplate = new RestTemplate();
                //assumes an array
                ResponseEntity<List<Student>> response = restTemplate.exchange(
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Student>>() {
                        });

                List<Student> objects = response.getBody();
                ArrayList<Student> finallist = new ArrayList<Student>();
                for (int i = 0; i < objects.size(); i++) {
                    for (int j = 0; j < bookstudents.size(); j++) {
                        if (objects.get(i).getId() == bookstudents.get(j).getBookCode()) {
                            objects.get(i).setBookLendingEntity(bookstudents.get(j));
                            finallist.add(objects.get(i));
                            break;
                        }

                    }
                }

                return finallist;
            }
            catch (HttpClientErrorException e){
                throw new APIError(e.getMessage());
            }
            catch (IllegalArgumentException e){
                throw new APIError("URI is not valid");
            }
        }
        throw new BookNotFoundException("The Book doesn't exist or no student has borrowed the book");
    }


}
