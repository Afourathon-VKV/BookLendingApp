package com.Dockerates.BookLending.Config;

import com.Dockerates.BookLending.Constants;
import com.Dockerates.BookLending.Repository.UserRepository;
import com.Dockerates.BookLending.Service.BookWebClient;
import com.Dockerates.BookLending.Service.StudentWebClient;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        // For authenticating the user.
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // For loading user-specific data for auth process.
        return username -> userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public BookWebClient BookWebClientProvider() {
        BookWebClient bookWebClient = new BookWebClient();
        return bookWebClient;
    }

    @Bean
    public StudentWebClient StudentWebClientProvider() {
        StudentWebClient studentWebClient = new StudentWebClient();
        return studentWebClient;
    }
}
