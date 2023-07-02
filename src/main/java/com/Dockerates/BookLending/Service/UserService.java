package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.User;
import com.Dockerates.BookLending.Exception.UserDuplicateEmailException;
import com.Dockerates.BookLending.Exception.UserNotFoundException;
import com.Dockerates.BookLending.Exception.UserWrongPasswordException;

public interface UserService {
    String signup(User user) throws UserDuplicateEmailException;
    String login(User user) throws UserNotFoundException, UserWrongPasswordException;
}
