package own.eteryz.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import own.eteryz.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FavouriteProductRepository extends ReactiveCrudRepository<FavouriteProduct, UUID> {

    Mono<Void> deleteByProductIdAndUserId(int productId, String userId);

    Mono<FavouriteProduct> findByProductIdAndUserId(int productId, String userId);

    Flux<FavouriteProduct> findAllByUserId(String userId);
}
