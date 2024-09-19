package own.eteryz.customer.service;

import own.eteryz.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductReviewsService {

    Mono<ProductReview> createProductReview(int productId, int rating, String review);
    Flux<ProductReview> findProductReviewsByProductId(int productId);

}
