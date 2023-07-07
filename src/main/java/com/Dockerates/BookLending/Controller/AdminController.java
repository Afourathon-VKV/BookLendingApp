package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Entity.Role;
import com.Dockerates.BookLending.Entity.User;
import com.Dockerates.BookLending.Exception.UserDuplicateEmailException;
import com.Dockerates.BookLending.Exception.UserNotFoundException;
import com.Dockerates.BookLending.Service.UserService;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RequestMapping("/api/admin")
public class AdminController {
    private final UserService userService;

    @GetMapping("/getLibrarian")
    public List<User> getLibrarians(){
        return this.userService.getUsers(Role.LIBRARIAN);
    }

    @PostMapping("/addLibrarian")
    public User addLibrarian(@RequestBody User user) throws UserDuplicateEmailException {
        return this.userService.addLibrarian(user);

    }

    @Transactional
    @DeleteMapping("/deleteLibrarian")
    public String deleteLibrarian(@RequestParam(name = "email") String email) throws UserNotFoundException {
        return this.userService.deleteLibrarian(email);
    }
}
