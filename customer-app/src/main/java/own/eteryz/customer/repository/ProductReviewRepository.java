package own.eteryz.customer.repository;

import own.eteryz.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewRepository {

    Mono<ProductReview> save(ProductReview productReview);
    Flux<ProductReview> findAllByProductId(long id);
}
