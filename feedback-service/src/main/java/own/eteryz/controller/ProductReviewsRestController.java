package own.eteryz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import own.eteryz.controller.payload.NewProductReviewPayload;
import own.eteryz.entity.ProductReview;
import own.eteryz.service.ProductReviewsService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.data.mongodb.core.query.Query.query;
import static org.springframework.data.mongodb.core.query.Criteria.where;

@RestController
@RequestMapping("feedback-api/v1/product-reviews")
@RequiredArgsConstructor
public class ProductReviewsRestController {

    private final ProductReviewsService productReviewsService;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @GetMapping("by-product-id/{productId:\\d+}")
    public Flux<ProductReview> findProductReviewsByProductId(
            @PathVariable("productId") int productId
    ) {
        var query = query(where("productId").is(productId));
        return this.reactiveMongoTemplate.find(query, ProductReview.class);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductReview>> createProductReview(
            @Valid @RequestBody Mono<NewProductReviewPayload> newProductReviewPayloadMono,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        return newProductReviewPayloadMono
                .flatMap(newProductReviewPayload -> this.productReviewsService.createProductReview(
                        newProductReviewPayload.productId(),
                        newProductReviewPayload.rating(),
                        newProductReviewPayload.review()
                ))
                .map(productReview -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/feedback-api/v1/product-reviews/{id}")
                                .build(productReview.getId()))
                        .body(productReview));
    }
}
