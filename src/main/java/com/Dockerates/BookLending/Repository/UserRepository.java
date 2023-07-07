package com.Dockerates.BookLending.Repository;

import com.Dockerates.BookLending.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;
import com.Dockerates.BookLending.Entity.Role;
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);

    String deleteByEmail(String email);
}
