package own.eteryz.customer.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import own.eteryz.customer.client.WebClientFavouriteProductClient;
import own.eteryz.customer.client.WebClientProductReviewsClient;
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

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${eteryz.services.feedback.url:http://localhost:8084}") String baseUrl
    ) {
        return new WebClientProductReviewsClient(WebClient.builder()
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public WebClientFavouriteProductClient webClientFavouriteProductsClient(
            @Value("${eteryz.services.feedback.url:http://localhost:8084}") String baseUrl
    ) {
        return new WebClientFavouriteProductClient(WebClient.builder()
                .baseUrl(baseUrl)
                .build());
    }
}
