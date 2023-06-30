package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.BookLendingEntity;

import java.util.List;

public interface BookLendingService {
    BookLendingEntity LendBook(BookLendingEntity bookLending);
    BookLendingEntity ReturnBook(int transactionid);

}
