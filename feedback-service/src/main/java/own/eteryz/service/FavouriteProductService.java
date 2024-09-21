package own.eteryz.service;

import own.eteryz.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  FavouriteProductService {
    Mono<FavouriteProduct> addFavouriteProduct(int productId);

    Mono<Void> removeFavouriteProduct(int productId);

    Mono<FavouriteProduct> findFavouriteProductByProductId(int productId);

    Flux<FavouriteProduct> findFavouriteProducts();
}
