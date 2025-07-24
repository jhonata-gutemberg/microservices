package dev.gutemberg.comment.api.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import java.time.Duration;

@Component
@RequiredArgsConstructor
public class ModerationRestClientFactory {
    private final RestClient.Builder builder;

    public RestClient build() {
        return builder.baseUrl("http://localhost:8081")
                .defaultStatusHandler(HttpStatusCode::is5xxServerError, (request, response) -> {
                    throw new BadGatewayException();
                })
                .requestFactory(clientHttpRequestFactory())
                .build();
    }

    private ClientHttpRequestFactory clientHttpRequestFactory() {
        final var factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(Duration.ofSeconds(3));
        factory.setReadTimeout(Duration.ofSeconds(5));
        return factory;
    }
}
