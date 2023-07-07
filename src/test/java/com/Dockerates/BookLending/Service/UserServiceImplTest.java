package com.Dockerates.BookLending.Service;
import com.Dockerates.BookLending.Entity.User;
import com.Dockerates.BookLending.Exception.UserDuplicateEmailException;
import com.Dockerates.BookLending.Exception.UserNotFoundException;
import com.Dockerates.BookLending.Exception.UserWrongPasswordException;
import com.Dockerates.BookLending.Repository.UserRepository;
import com.Dockerates.BookLending.Entity.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void signup_Successful() throws UserDuplicateEmailException {
        // Arrange
        User user = new User("1","john","john", "password", "john@example.com", Role.LIBRARIAN);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenReturn(user);

        when(jwtService.generateToken(any())).thenReturn("token");

        String token = userService.signup(user);

        // Assert
        assertNotNull(token);
        verify(passwordEncoder).encode("password");
        verify(userRepository).save(any());
    }

    @Test
    void signup_DuplicateEmail_ExceptionThrown() {
        // Arrange
        User user = new User("1","john","john", "password", "john@example.com", Role.LIBRARIAN);
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");
        when(userRepository.save(any())).thenThrow(DataIntegrityViolationException.class);

        // Act & Assert
        assertThrows(UserDuplicateEmailException.class, () -> userService.signup(user));
    }

    @Test
    void login_Successful() throws UserNotFoundException, UserWrongPasswordException {
        // Arrange
        User user = new User("1","john","john", "password", "john@example.com", Role.LIBRARIAN);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenReturn(null);
        when(jwtService.generateToken(user)).thenReturn("token");

        // Act
        String token = userService.login(user);

        // Assert
        assertNotNull(token);
        verify(userRepository).findByEmail("john@example.com");
        verify(authenticationManager).authenticate(any());
        verify(jwtService).generateToken(user);
    }

    @Test
    void login_UserNotFound_ExceptionThrown() {
        // Arrange
        User user = new User("1","john", "john", "password", "john@example.com", Role.LIBRARIAN);
        when(userRepository.findByEmail(any())).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(UserNotFoundException.class, () -> userService.login(user));
    }

    @Test
    void login_WrongPassword_ExceptionThrown() {

        // Arrange
        User user = new User("1","john", "john", "password", "john@example.com", Role.LIBRARIAN);
        when(userRepository.findByEmail(any())).thenReturn(Optional.of(user));
        when(authenticationManager.authenticate(any())).thenThrow(BadCredentialsException.class);

        // Act & Assert
        assertThrows(UserWrongPasswordException.class, () -> userService.login(user));
    }

}

