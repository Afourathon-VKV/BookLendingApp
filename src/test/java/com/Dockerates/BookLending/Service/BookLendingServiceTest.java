package com.Dockerates.BookLending.Service;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import java.util.*;
import com.Dockerates.BookLending.Entity.Book;
import com.Dockerates.BookLending.Entity.BookLendingEntity;
import com.Dockerates.BookLending.Exception.APIError;
import com.Dockerates.BookLending.Exception.BookLended;
import com.Dockerates.BookLending.Exception.StudentNotFoundException;
import com.Dockerates.BookLending.Repository.BookLendingRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
public class BookLendingServiceTest {

    @Mock
    private BookLendingRepository bookLendingRepository;

    @InjectMocks
    private BookLendingServiceImpl bookLendingService;

    @Test
    public void testSuccessfulLendBook() throws BookLended {
        // Arrange
        BookLendingEntity bookLending = new BookLendingEntity(1, "40", "123", true, new Date(), null);

        when(bookLendingRepository.findByBookCodeAndIssued("123", true)).thenReturn(new ArrayList<>());
        when(bookLendingRepository.save(any(BookLendingEntity.class))).thenReturn(bookLending);

        // Act
        BookLendingEntity result = bookLendingService.LendBook(bookLending);

        // Assert
        Assertions.assertTrue(result.isIssued());
        Assertions.assertNotNull(result.getIssueDate());
        verify(bookLendingRepository, times(1)).findByBookCodeAndIssued("123", true);
        verify(bookLendingRepository, times(1)).save(bookLending);
    }


    @Test
    public void testFailureBookLend() {
        BookLendingEntity bookLending = new BookLendingEntity(1, "40", "123", true, new Date(), null);

        List<BookLendingEntity> existingLendingList = new ArrayList<>();
        existingLendingList.add(new BookLendingEntity());

        when(bookLendingRepository.findByBookCodeAndIssued("123", true)).thenReturn(existingLendingList);

        // Act & Assert
        Assertions.assertThrows(BookLended.class, () -> bookLendingService.LendBook(bookLending));
        verify(bookLendingRepository, times(1)).findByBookCodeAndIssued(eq("123"), eq(true));
        verify(bookLendingRepository, never()).save(any(BookLendingEntity.class));
    }


    @Test
    public void testSuccessfulReturnBook() throws BookLended {
        // Arrange
        int transactionId = 1;
        BookLendingEntity bookLendingEntity = new BookLendingEntity(1, "40", "123", true, new Date(), null);

        when(bookLendingRepository.findByTransactionIdAndIssued(transactionId, true))
                .thenReturn(Optional.of(bookLendingEntity));
        when(bookLendingRepository.save(any(BookLendingEntity.class))).thenReturn(bookLendingEntity);

        // Act
        BookLendingEntity result = bookLendingService.ReturnBook(transactionId);

        // Assert
        Assertions.assertFalse(result.isIssued());
        Assertions.assertNotNull(result.getReturnDate());
        verify(bookLendingRepository, times(1)).findByTransactionIdAndIssued(eq(transactionId), eq(true));
        verify(bookLendingRepository, times(1)).save(eq(bookLendingEntity));
    }

    @Test
    public void testFailureReturnBook() {
        int transactionId = 1;

        when(bookLendingRepository.findByTransactionIdAndIssued(transactionId, true)).thenReturn(Optional.empty());

        Assertions.assertThrows(BookLended.class, () -> bookLendingService.ReturnBook(transactionId));
        verify(bookLendingRepository, times(1)).findByTransactionIdAndIssued(eq(transactionId), eq(true));
        verify(bookLendingRepository, never()).save(any(BookLendingEntity.class));
    }

}

