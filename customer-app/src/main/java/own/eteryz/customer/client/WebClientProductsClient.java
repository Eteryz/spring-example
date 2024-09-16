package own.eteryz.customer.client;

import lombok.RequiredArgsConstructor;
import org.springframework.web.reactive.function.client.WebClient;
import own.eteryz.customer.entity.Product;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class WebClientProductsClient implements ProductsClient {

    private final WebClient webClient;

    @Override
    public Flux<Product> findAllProducts(String filter) {
        return webClient.get()
                .uri("api/v1/catalog/products?filter={filter}", filter)
                .retrieve()
                .bodyToFlux(Product.class);
    }
}
