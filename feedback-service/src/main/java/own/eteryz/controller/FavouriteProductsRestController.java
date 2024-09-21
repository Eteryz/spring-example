package own.eteryz.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public Flux<FavouriteProduct> getFavouriteProducts() {
        return this.favouriteProductService.findFavouriteProducts();
    }

    @GetMapping("by-product-id/{productId:\\d+}")
    public Mono<FavouriteProduct> findFavouriteProduct(
            @PathVariable("productId") int productId
    ) {
        return this.favouriteProductService.findFavouriteProductByProductId(productId);
    }

    @PostMapping
    public Mono<ResponseEntity<FavouriteProduct>> createFavouriteProduct(
            @Valid @RequestBody Mono<NewFavouriteProductPayload> payloadMono,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        return payloadMono
                .flatMap(payload -> this.favouriteProductService.addFavouriteProduct(payload.productId()))
                .map(favouriteProduct -> ResponseEntity
                        .created(uriComponentsBuilder.replacePath("feedback-api/v1/favourite-products/{id}")
                                .build(favouriteProduct.getId()))
                        .body(favouriteProduct));
    }

    @DeleteMapping("by-product-id/{productId:\\d+}")
    public Mono<ResponseEntity<Void>> deleteFavouriteProduct(
            @PathVariable("productId") int productId
    ){
        return this.favouriteProductService.removeFavouriteProduct(productId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
