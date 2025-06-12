package dev.gutemberg.comment.api.client;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
@RequiredArgsConstructor
public class ModerationRestClientFactory {
    private final RestClient.Builder builder;

    public RestClient build() {
        return builder.baseUrl("http://localhost:8081").build();
    }
}
