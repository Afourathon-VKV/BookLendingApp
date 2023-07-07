package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.User;
import com.Dockerates.BookLending.Exception.UserDuplicateEmailException;
import com.Dockerates.BookLending.Exception.UserNotFoundException;
import com.Dockerates.BookLending.Exception.UserWrongPasswordException;
import com.Dockerates.BookLending.Entity.Role;
import java.util.List;
public interface UserService {
    String signup(User user) throws UserDuplicateEmailException;
    String login(User user) throws UserNotFoundException, UserWrongPasswordException;
    List<User> getUsers(Role role);
    public User addLibrarian(User user) throws UserDuplicateEmailException;

    public String deleteLibrarian(String email) throws UserNotFoundException;
}
