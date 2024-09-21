package own.eteryz.repository;

import own.eteryz.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewRepository {

    Mono<ProductReview> save(ProductReview productReview);
    Flux<ProductReview> findAllByProductId(long id);
}
