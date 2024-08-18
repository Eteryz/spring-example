package own.eteryz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import own.eteryz.client.RestClientProductsRestClientImpl;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClientImpl productsRestClient(
            @Value("${catalog.service.baseUrl:http://localhost:8080/api/v1}") String catalogBaseUrl,
            @Value("${catalog.service.username:}") String usernameService,
            @Value("${catalog.service.password:}") String passwordService
    ) {
        return new RestClientProductsRestClientImpl(
                RestClient.builder()
                        .baseUrl(catalogBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(usernameService, passwordService)
                        )
                        .build()
        );
    }
}
