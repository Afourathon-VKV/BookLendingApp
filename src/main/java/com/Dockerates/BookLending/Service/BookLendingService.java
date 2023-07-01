package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Entity.Student;

import java.util.List;

public interface BookLendingService {
    BookLendingEntity LendBook(BookLendingEntity bookLending);
    BookLendingEntity ReturnBook(int transactionid);

    List<Book> getBookDetails(int studentId);

    List<Student> getStudentDetails(int bookId);
}
