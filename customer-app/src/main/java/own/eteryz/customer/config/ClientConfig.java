package own.eteryz.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import own.eteryz.customer.client.WebClientProductsClient;

@Configuration
public class ClientConfig {

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${eteryz.services.catalog.url:http://localhost:8081}") String baseUrl
    ) {
        return new WebClientProductsClient(WebClient.builder()
                .baseUrl(baseUrl)
                .build());
    }
}
