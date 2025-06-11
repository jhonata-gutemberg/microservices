package dev.gutemberg.comment.api.config.client;

import dev.gutemberg.comment.api.client.ModerationClient;
import dev.gutemberg.comment.api.client.ModerationRestClientFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpExchangeAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@RequiredArgsConstructor
public class ModerationClientConfig {
    private final ModerationRestClientFactory moderationRestClientFactory;

    @Bean
    ModerationClient moderationClient() {
        final RestClient restClient = moderationRestClientFactory.build();
        final HttpExchangeAdapter adapter = RestClientAdapter.create(restClient);
        final var proxyFactory = HttpServiceProxyFactory.builderFor(adapter).build();
        return proxyFactory.createClient(ModerationClient.class);
    }
}
