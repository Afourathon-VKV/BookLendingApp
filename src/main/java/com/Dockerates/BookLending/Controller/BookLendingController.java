package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Service.BookLendingService;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookLendingController {

    private final BookLendingService bookLendingService;

    @PostMapping("/lendBook")
    public BookLendingEntity lendBook( @RequestBody BookLendingEntity bookLendingEntity){
        return bookLendingService.LendBook(bookLendingEntity);
    }

    @PostMapping("returnBook/{transactionId}")
    public BookLendingEntity returnBook(@PathVariable int transactionId){
        return bookLendingService.ReturnBook(transactionId);
    }
}

