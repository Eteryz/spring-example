package own.eteryz.customer.client;

import own.eteryz.customer.entity.Product;
import reactor.core.publisher.Flux;

public interface ProductsClient {
    Flux<Product> findAllProducts(String filter);
}
