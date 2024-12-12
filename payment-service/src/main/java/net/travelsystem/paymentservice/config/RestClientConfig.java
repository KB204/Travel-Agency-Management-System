package net.travelsystem.paymentservice.config;

import net.travelsystem.paymentservice.clients.CardRestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${server.rest.api}")
    private String apiUrl;

    @Bean
    RestClient restClient(RestClient.Builder builder) {
        return builder.baseUrl(apiUrl)
                .build();
    }

    @Bean
    CardRestClient cardRest(RestClient restClient) {
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();
        return factory.createClient(CardRestClient.class);
    }
}
