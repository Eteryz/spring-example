package own.eteryz.customer.client;

import own.eteryz.customer.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FavouriteProductClient {
    Mono<FavouriteProduct> findFavouriteProductByProductId(int productId);
    Mono<FavouriteProduct> addFavouriteProduct(int productId);
    Mono<Void> removeFavouriteProduct(int productId);
    Flux<FavouriteProduct> findFavouriteProducts();
}
