package own.eteryz.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import own.eteryz.controller.payload.NewFavouriteProductPayload;
import own.eteryz.entity.FavouriteProduct;
import own.eteryz.service.FavouriteProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


@RestController
@RequestMapping("feedback-api/v1/favourite-products")
@RequiredArgsConstructor
public class FavouriteProductsRestController {

    private final FavouriteProductService favouriteProductService;

    @GetMapping
    public Flux<FavouriteProduct> getFavouriteProducts(
            Mono<JwtAuthenticationToken> authenticationTokenMono
    ) {
        return authenticationTokenMono.flatMapMany(token ->
                this.favouriteProductService.findFavouriteProducts(token.getToken().getSubject()));
    }

    @GetMapping("by-product-id/{productId:\\d+}")
    public Mono<FavouriteProduct> findFavouriteProduct(
            @PathVariable("productId") int productId,
            Mono<JwtAuthenticationToken> authenticationTokenMono
    ) {
        return authenticationTokenMono.flatMap(token ->
                this.favouriteProductService.findFavouriteProductByProductId(
                        productId, token.getToken().getSubject()));
    }

    @PostMapping
    public Mono<ResponseEntity<FavouriteProduct>> createFavouriteProduct(
            @Valid @RequestBody Mono<NewFavouriteProductPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder,
            Mono<JwtAuthenticationToken> authenticationTokenMono
    ) {
        return Mono.zip(authenticationTokenMono, payloadMono)
                .flatMap(tuple -> this.favouriteProductService.addFavouriteProduct(
                        tuple.getT2().productId(), tuple.getT1().getToken().getSubject()))
                .map(favouriteProduct -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("feedback-api/v1/favourite-products/{id}")
                                .build(favouriteProduct.getId()))
                        .body(favouriteProduct));
    }

    @DeleteMapping("by-product-id/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> deleteFavouriteProduct(
            @PathVariable("productId") int productId,
            Mono<JwtAuthenticationToken> authenticationTokenMono
    ) {
        return authenticationTokenMono.flatMap(token ->
                this.favouriteProductService.removeFavouriteProduct(productId, token.getToken().getSubject())
                        .then(Mono.just(ResponseEntity.noContent().build())));
    }
}
