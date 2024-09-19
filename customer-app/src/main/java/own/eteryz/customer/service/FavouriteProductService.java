package own.eteryz.customer.service;

import own.eteryz.customer.entity.FavouriteProduct;
import reactor.core.publisher.Mono;

public interface FavouriteProductService {
    Mono<FavouriteProduct> addFavouriteProduct(int productId);

    Mono<Void> removeFavouriteProduct(int productId);

    Mono<FavouriteProduct> findFavouriteProductByProductId(int productId);
}
