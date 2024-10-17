package own.eteryz.service;

import own.eteryz.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface  FavouriteProductService {
    Mono<FavouriteProduct> addFavouriteProduct(int productId, String userId);

    Mono<Void> removeFavouriteProduct(int productId, String userId);

    Mono<FavouriteProduct> findFavouriteProductByProductId(int productId, String userId);

    Flux<FavouriteProduct> findFavouriteProducts(String userId);
}
