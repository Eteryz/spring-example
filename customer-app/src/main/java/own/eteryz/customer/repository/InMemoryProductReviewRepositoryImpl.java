package own.eteryz.customer.repository;

import org.springframework.stereotype.Repository;
import own.eteryz.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
public class InMemoryProductReviewRepositoryImpl implements ProductReviewRepository {

    public List<ProductReview> productReviewsList = Collections.synchronizedList(new LinkedList<>());

    @Override
    public Mono<ProductReview> save(ProductReview productReview) {
        this.productReviewsList.add(productReview);
        return Mono.just(productReview);
    }

    @Override
    public Flux<ProductReview> findAllByProductId(long id) {
        return Flux.fromIterable(this.productReviewsList)
                .filter(productReview -> productReview.getProductId() == id);
    }
}
