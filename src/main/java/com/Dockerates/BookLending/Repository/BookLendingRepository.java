package com.Dockerates.BookLending.Repository;

import com.Dockerates.BookLending.Entity.BookLendingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BookLendingRepository extends JpaRepository<BookLendingEntity, Integer> {
    Optional<BookLendingEntity> findByTransactionIdAndIssued(int transactionId, boolean issued);
    Optional<BookLendingEntity> findByStudentIdAndBookCodeAndIssued(int studentId, int bookCode, boolean issued);
}
