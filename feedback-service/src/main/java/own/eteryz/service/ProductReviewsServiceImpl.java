package own.eteryz.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import own.eteryz.entity.ProductReview;
import own.eteryz.repository.ProductReviewRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductReviewsServiceImpl implements ProductReviewsService {

    private final ProductReviewRepository productReviewRepository;

    @Override
    public Mono<ProductReview> createProductReview(int productId, int rating, String review, String userId) {
        return this.productReviewRepository.save(
                new ProductReview(UUID.randomUUID(), productId, rating, review, userId)
        );
    }

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(int productId) {
        return this.productReviewRepository.findAllByProductId(productId);
    }
}
