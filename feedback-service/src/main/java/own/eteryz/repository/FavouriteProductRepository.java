package own.eteryz.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import own.eteryz.entity.FavouriteProduct;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface FavouriteProductRepository extends ReactiveCrudRepository<FavouriteProduct, UUID> {

    Mono<Void> deleteByProductId(int productId);

    Mono<FavouriteProduct> findByProductId(int productId);

}
