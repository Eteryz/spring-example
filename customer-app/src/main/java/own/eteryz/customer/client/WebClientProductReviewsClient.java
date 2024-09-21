package own.eteryz.customer.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import own.eteryz.customer.client.exception.ClientBadRequestException;
import own.eteryz.customer.client.payload.NewProductReviewPayload;
import own.eteryz.customer.entity.ProductReview;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class WebClientProductReviewsClient implements ProductReviewsClient {

    private final WebClient webClient;

    @Override
    public Flux<ProductReview> findProductReviewsByProductId(Integer productId) {
        return webClient.get()
                .uri("/feedback-api/v1/product-reviews/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToFlux(ProductReview.class);
    }

    @Override
    public Mono<ProductReview> createProductReview(Integer productId, Integer rating, String review) {
        return this.webClient
                .post()
                .uri("/feedback-api/v1/product-reviews")
                .bodyValue(new NewProductReviewPayload(productId, rating, review))
                .retrieve()
                .bodyToMono(ProductReview.class)
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        ex -> new ClientBadRequestException(ex,
                                ((List<String>) ex.getResponseBodyAs(ProblemDetail.class).getProperties().get("errors"))));
    }
}
