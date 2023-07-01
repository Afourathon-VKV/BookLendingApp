package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Entity.Student;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.BookNotFoundException;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;

import java.util.List;

public interface BookLendingService {
    BookLendingEntity LendBook(BookLendingEntity bookLending) throws BookLended;
    BookLendingEntity ReturnBook(int transactionid) throws BookLended;

    List<Book> getBookDetails(int studentId) throws StudentNotFoundException;

    List<Student> getStudentDetails(int bookId) throws BookNotFoundException;
}
