package own.eteryz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.cloud.client.loadbalancer.LoadBalancerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import own.eteryz.client.RestClientProductsRestClientImpl;
import own.eteryz.security.OAuthClientRequestInterceptor;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClientImpl productsRestClient(
            @Value("${catalog.service.baseUrl:http://localhost:8080}/api/v1") String catalogBaseUrl,
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientRepository authorizedClientRepository,
            @Value("${catalog.service.registration-id:keycloak}") String registrationId,
            LoadBalancerClient loadBalancerClient
    ) {
        return new RestClientProductsRestClientImpl(
                RestClient.builder()
                        .requestInterceptor(new LoadBalancerInterceptor(loadBalancerClient))
                        .baseUrl(catalogBaseUrl)
                        .requestInterceptor(
                                new OAuthClientRequestInterceptor(
                                        new DefaultOAuth2AuthorizedClientManager(clientRegistrationRepository,
                                                authorizedClientRepository),
                                        registrationId
                                )
                        )
                        .build()
        );
    }
}
