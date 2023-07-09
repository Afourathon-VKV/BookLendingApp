package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Entity.User;
import com.Dockerates.BookLending.Exception.UserDuplicateEmailException;
import com.Dockerates.BookLending.Exception.UserNotFoundException;
import com.Dockerates.BookLending.Exception.UserWrongPasswordException;
import com.Dockerates.BookLending.Repository.UserRepository;
import com.Dockerates.BookLending.Entity.Role;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Override
    public String signup(User user) throws UserDuplicateEmailException {
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String userId = UuidService.getUUID();
        User newUser = User
                .builder()
                .id(userId)
                .username(user.getUsername())
                .email(user.getEmail())
                .password(encodedPassword)
                .role(user.getRole())
                .build();
        try {
            logger.info(String.format("Added User with email %s.", user.getEmail()));
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            logger.error(String.format("Could not add User of email %s as account with that email exists.",user.getEmail()));
            throw new UserDuplicateEmailException("Email already exists");
        }
        return jwtService.generateToken(newUser);
    }

    @Override
    public String login(User user) throws UserNotFoundException, UserWrongPasswordException {
        logger.info("Authenticating user: " + user.getEmail());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            user.getEmail(),
                            user.getPassword()
                    )
            );
        } catch (AuthenticationException e) {
            logger.error(String.format("User with email %s tried logging in with the wrong password.",user.getEmail()));
            throw new UserWrongPasswordException("Wrong password");
        }
        logger.info("Authentication successful: " + user.getEmail());
        User newUser = userRepository.findByEmail(user.getEmail()).orElseThrow(() -> new UserNotFoundException("User not found"));
        return jwtService.generateToken(newUser);
    }

    @Override
    public List<User> getUsers(Role role){
        logger.info("Fetching all users of type " + role.toString());
        return this.userRepository.findByRole(role);
    }

    @Override
    public User addLibrarian(User user) throws UserDuplicateEmailException{
        String encodedPassword = passwordEncoder.encode(user.getPassword());
        String userId = UuidService.getUUID();
        User newUser = User
                .builder()
                .id(userId)
                .username(user.getUsername())
                .email(user.getEmail())
                .password(encodedPassword)
                .role(Role.LIBRARIAN)   // Default role is LIBRARIAN.
                .build();
        try {
            newUser.setName(user.getName());
            userRepository.save(newUser);
        } catch (DataIntegrityViolationException e) {
            logger.error("Could not add librarian with email " + user.getEmail() + "as they are already added.");
            throw new UserDuplicateEmailException("Email already exists");
        }
        logger.info("Added librarian with email" + user.getEmail());
        return newUser;
    }

    @Override
    public String deleteLibrarian(String email) throws UserNotFoundException{
        if(this.userRepository.deleteByEmail(email).equals("0")){
            logger.error("Could not delete librarian with email " + email + "As librarian does not exist");
            throw new UserNotFoundException("This email doesn't exist");
        }
        logger.info("Deleted librarian with email " + email);
        return this.userRepository.deleteByEmail(email);
    }
}
