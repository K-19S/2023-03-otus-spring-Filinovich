package ru.otus.filinovich.resilince.clients;

import lombok.RequiredArgsConstructor;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ru.otus.filinovich.resilince.dto.BookCommentDto;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookCommentsClient extends AbstractClient {

    private final RestTemplate restTemplate;

    private final CircuitBreakerFactory circuitBreakerFactory;


    public ResponseEntity<List> getCommentsByBookId(String bookId, String authorization) {
        return circuitBreakerFactory.create("getCommentsByBook").run(
                () -> restTemplate.exchange("http://library/comments?bookId=" + bookId,
                        HttpMethod.GET, getEntity(authorization), List.class),
                throwable -> fallbackEmptyList());
    }

    public ResponseEntity<?> addComment(BookCommentDto dto, String authorization) {
        return circuitBreakerFactory.create("addComment").run(
                () -> restTemplate.exchange("http://library/comments",
                        HttpMethod.POST, getEntity(dto, authorization), Object.class),
                throwable -> fallbackError());
    }



    private ResponseEntity<List> fallbackEmptyList() {
        return ResponseEntity.ok(new ArrayList<>());
    }

    private ResponseEntity<?> fallbackError() {
        return ResponseEntity.status(429).build();
    }
}
