package own.eteryz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import own.eteryz.client.RestClientProductsRestClientImpl;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientProductsRestClientImpl productsRestClient(
            @Value("${catalog.service.baseUrl:http://localhost:8080/api/v1}") String catalogBaseUrl
    ) {
        return new RestClientProductsRestClientImpl(
                RestClient.builder()
                        .baseUrl(catalogBaseUrl)
                        .build()
        );
    }
}
