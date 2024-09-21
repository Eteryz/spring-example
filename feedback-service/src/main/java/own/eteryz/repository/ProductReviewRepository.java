package own.eteryz.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import own.eteryz.entity.ProductReview;
import reactor.core.publisher.Flux;

import java.util.UUID;

public interface ProductReviewRepository extends ReactiveCrudRepository<ProductReview, UUID> {

    Flux<ProductReview> findAllByProductId(int productId);
}
