package own.eteryz.customer.client;

import own.eteryz.customer.entity.Product;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductsClient {
    Flux<Product> findAllProducts(String filter);

    Mono<Product> findProduct(int productId);
}
