package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Entity.Student;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.BookNotFoundException;
import com.Dockerates.BookLending.Exception.APIError;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;

import java.util.List;

public interface BookLendingService {
    BookLendingEntity LendBook(BookLendingEntity bookLending) throws BookLended;
    BookLendingEntity ReturnBook(int transactionid) throws BookLended;

    List<Book> getBookDetails(String rollNo) throws StudentNotFoundException, APIError;

    List<Student> getStudentDetails(String bookId) throws BookNotFoundException, APIError;
}
