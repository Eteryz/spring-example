package own.eteryz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
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
@Slf4j
public class ProductReviewsRestController {

    private final ProductReviewsService productReviewsService;

    private final ReactiveMongoTemplate reactiveMongoTemplate;

    @GetMapping("by-product-id/{productId:\\d+}")
    @Operation(
            security = @SecurityRequirement(name = "keycloak"),
            description = "Получение списка отзывов о продукте"
    )
    public Flux<ProductReview> findProductReviewsByProductId(
            @PathVariable("productId") int productId
    ) {
        var query = query(where("productId").is(productId));
        return this.reactiveMongoTemplate.find(query, ProductReview.class);
    }

    @PostMapping
    public Mono<ResponseEntity<ProductReview>> createProductReview(
            @Valid @RequestBody Mono<NewProductReviewPayload> newProductReviewPayloadMono,
            UriComponentsBuilder uriComponentsBuilder,
            Mono<JwtAuthenticationToken> authenticationTokenMono
    ) {
        return authenticationTokenMono.flatMap(token -> newProductReviewPayloadMono
                .flatMap(newProductReviewPayload -> this.productReviewsService.createProductReview(
                        newProductReviewPayload.productId(),
                        newProductReviewPayload.rating(),
                        newProductReviewPayload.review(),
                        token.getToken().getSubject()
                ))
                .map(productReview -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("/feedback-api/v1/product-reviews/{id}")
                                .build(productReview.getId()))
                        .body(productReview)));
    }
}
