package com.Dockerates.BookLending.Controller;

import com.Dockerates.BookLending.Entity.User;
import com.Dockerates.BookLending.Exception.UserDuplicateEmailException;
import com.Dockerates.BookLending.Exception.UserNotFoundException;
import com.Dockerates.BookLending.Exception.UserWrongPasswordException;
import com.Dockerates.BookLending.Service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public String signup(@RequestBody User user) throws UserDuplicateEmailException {
        System.out.println("dddd" + user.getRole());
        return userService.signup(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody User user, HttpServletResponse response) throws UserWrongPasswordException, UserNotFoundException {
        String token = userService.login(user);
        if (token != null) {
            Number maxAge = 1000 * 60 * 60 * 10; // 10 hour
            Cookie cookie = new Cookie("jwt", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(maxAge.intValue());
            response.addCookie(cookie);
            // response.setHeader("Set-Cookie", "jwt="+ token+"; HttpOnly; SameSite=None; Secure; Path=/; Max-Age="+maxAge.toString());
        }
        return token;
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("jwt", "");
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "User logged out successfully";
    }
}
