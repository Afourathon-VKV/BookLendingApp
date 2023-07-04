package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class StudentWebClient {
    private WebClient webClient;

    public StudentWebClient() {
        this.webClient = WebClient.create(Constants.StudentUrl);
    }

    public WebClient getWebClient() {
        return webClient;
    }
}
