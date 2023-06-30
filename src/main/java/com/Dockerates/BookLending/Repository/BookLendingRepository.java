package com.Dockerates.BookLending.Repository;

import com.Dockerates.BookLending.Entity.BookLendingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookLendingRepository extends JpaRepository<BookLendingEntity, Integer> {

}
