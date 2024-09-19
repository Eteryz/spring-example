package own.eteryz.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import own.eteryz.customer.entity.ProductReview;
import own.eteryz.customer.repository.ProductReviewRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReviewsServiceImpl implements ProductReviewsService {

    private final ProductReviewRepository productReviewRepository;

    @Override
    public Mono<ProductReview> createProductReview(int productId, int rating, String review) {
        return this.productReviewRepository.save(
                new ProductReview(UUID.randomUUID(), productId, rating, review)
        );
    }

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(int productId) {
        return this.productReviewRepository.findAllByProductId(productId);
    }
}
