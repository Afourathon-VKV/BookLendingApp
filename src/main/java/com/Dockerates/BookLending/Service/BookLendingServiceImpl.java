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
        List<BookLendingEntity> bookLending1=bookLendingRepository.findByBookCodeAndIssued(bookLending.getBookCode(),true); //checks if a book is issued
        if(!bookLending1.isEmpty()){
            throw new BookLended("This Book is lent to another student");
        }
        bookLending.setIssued(true);
        bookLending.setIssueDate(new Date());
        return bookLendingRepository.save(bookLending); //add a new transaction where the book is lent to a student
    }

    @Override
    public BookLendingEntity ReturnBook(int transactionId) throws BookLended {
        Optional<BookLendingEntity> bookLending=bookLendingRepository.findByTransactionIdAndIssued(transactionId,true); //checks the transaction where the book is issued
        if(bookLending.isPresent()){
            BookLendingEntity bookLending1=bookLending.get(); //on returning the issued flag is set to false indicating that the book is available to be lent
            bookLending1.setIssued(false);
            bookLending1.setReturnDate(new Date());
            return bookLendingRepository.save(bookLending1); //updates a given transaction
        }

        throw new BookLended("This Book has already been returned or not been lent yet.");
    }

    @Override
    public List<Book> getBookDetails(String rollNo) throws StudentNotFoundException, APIError {
        List<BookLendingEntity> studentbooks=bookLendingRepository.findByRollNoAndIssued(rollNo,true); //get all the books a student owns
        if(!studentbooks.isEmpty()) {
            final String uri = Constants.BookUrl+"api/books"; //api call to get all the books
            //need to add exception here
            try{
                RestTemplate restTemplate = new RestTemplate();
                //assumes an array
                ResponseEntity<List<Book>> response = restTemplate.exchange( //makes a get request to the api to retrieve all the books
                        uri,
                        HttpMethod.GET,
                        null,
                        new ParameterizedTypeReference<List<Book>>() {
                        });

                List<Book> objects = response.getBody();
                ArrayList<Book> finallist = new ArrayList<Book>();
                for (int i = 0; i < objects.size(); i++) { //comparing the books from the api call and the books in the studentbooks list by bookcode.
                    for (int j = 0; j < studentbooks.size(); j++) {
                        if (objects.get(i).getCode().equals(studentbooks.get(j).getBookCode())) {
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
    public List<Student> getStudentDetails(String bookId) throws BookNotFoundException, APIError {
        List<BookLendingEntity> bookstudents=bookLendingRepository.findByBookCodeAndIssued(bookId, true); //get all transactions for a given book code and is issued to a student
        if(!bookstudents.isEmpty()) {
            final String uri = Constants.StudentUrl + "api/students"; //gets all students

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
                for (int i = 0; i < objects.size(); i++) { //compares the two lists on rollNo
                    for (int j = 0; j < bookstudents.size(); j++) {
                        if (objects.get(i).getRollNo().equals(bookstudents.get(j).getRollNo())) {
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
