package own.eteryz.customer.config;

import io.micrometer.observation.ObservationRegistry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.security.oauth2.client.registration.ReactiveClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.client.web.server.ServerOAuth2AuthorizedClientRepository;
import org.springframework.web.reactive.function.client.DefaultClientRequestObservationConvention;
import org.springframework.web.reactive.function.client.WebClient;
import own.eteryz.customer.client.WebClientFavouriteProductClient;
import own.eteryz.customer.client.WebClientProductReviewsClient;
import own.eteryz.customer.client.WebClientProductsClient;

@Configuration
public class ClientConfig {

    @Bean
    @Scope("prototype")
    public WebClient.Builder eteryzServicesWebClientBuilder(
            ReactiveClientRegistrationRepository clientRegistrationRepository,
            ServerOAuth2AuthorizedClientRepository authorizedClientRepository,
            ObservationRegistry observationRegistry
    ) {
        var filter = new ServerOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository, authorizedClientRepository);
        filter.setDefaultClientRegistrationId("keycloak");
        return WebClient.builder()
                .observationRegistry(observationRegistry)
                .observationConvention(new DefaultClientRequestObservationConvention())
                .filter(filter);
    }

    @Bean
    public WebClientProductsClient webClientProductsClient(
            @Value("${eteryz.services.catalog.url:http://localhost:8081}") String baseUrl,
            WebClient.Builder eteryzServicesWebClientBuilder
    ) {
        return new WebClientProductsClient(eteryzServicesWebClientBuilder
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public WebClientProductReviewsClient webClientProductReviewsClient(
            @Value("${eteryz.services.feedback.url:http://localhost:8084}") String baseUrl,
            WebClient.Builder eteryzServicesWebClientBuilder
    ) {
        return new WebClientProductReviewsClient(eteryzServicesWebClientBuilder
                .baseUrl(baseUrl)
                .build());
    }

    @Bean
    public WebClientFavouriteProductClient webClientFavouriteProductsClient(
            @Value("${eteryz.services.feedback.url:http://localhost:8084}") String baseUrl,
            WebClient.Builder eteryzServicesWebClientBuilder
    ) {
        return new WebClientFavouriteProductClient(eteryzServicesWebClientBuilder
                .baseUrl(baseUrl)
                .build());
    }
}
