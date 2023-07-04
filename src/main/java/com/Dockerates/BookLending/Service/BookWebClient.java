package com.Dockerates.BookLending.Service;

import com.Dockerates.BookLending.Constants;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class BookWebClient {
    private WebClient webClient;

    public BookWebClient() {
        this.webClient = WebClient.create(Constants.BookUrl);
    }

    public WebClient getWebClient() {
        return webClient;
    }
}
