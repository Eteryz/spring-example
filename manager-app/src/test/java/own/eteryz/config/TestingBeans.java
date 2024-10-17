package own.eteryz.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
import org.springframework.web.client.RestClient;
import own.eteryz.client.RestClientProductsRestClientImpl;

import static org.mockito.Mockito.mock;

@Configuration
public class TestingBeans {

    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return mock(ClientRegistrationRepository.class);
    }

    @Bean
    public OAuth2AuthorizedClientRepository oAuth2AuthorizedClientRepository(){
        return mock(OAuth2AuthorizedClientRepository.class);
    }

    @Bean
    @Primary
    // данный компонент является главным кандидатом на внедрение, а не тот, который указан в конфиге manager
    public RestClientProductsRestClientImpl testRestClientProductsRestClientImpl(
            @Value("${eteryz.services.manager.url}") String baseUrl
    ){
        return new RestClientProductsRestClientImpl(RestClient.builder()
                .baseUrl(baseUrl)
                .build());
    }
}
