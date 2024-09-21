package own.eteryz.customer.client;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import own.eteryz.customer.client.exception.ClientBadRequestException;
import own.eteryz.customer.client.payload.NewFavouriteProductPayload;
import own.eteryz.customer.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
public class WebClientFavouriteProductClient implements FavouriteProductClient {

    private final WebClient webClient;

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(int productId) {
        return webClient
                .get()
                .uri("/feedback-api/v1/favourite-products/by-product-id/{productId}", productId)
                .retrieve()
                .bodyToMono(FavouriteProduct.class)
                .onErrorComplete(WebClientResponseException.NotFound.class);
    }

    @Override
    public Mono<FavouriteProduct> addFavouriteProduct(int productId) {
        return webClient
                .post()
                .uri("/feedback-api/v1/favourite-products")
                .bodyValue(new NewFavouriteProductPayload(productId))
                .retrieve()
                .bodyToMono(FavouriteProduct.class)
                .onErrorMap(WebClientResponseException.BadRequest.class,
                        ex -> new ClientBadRequestException(ex,
                                ((List<String>) ex.getResponseBodyAs(ProblemDetail.class)
                                        .getProperties().get("errors"))));
    }

    @Override
    public Mono<Void> removeFavouriteProduct(int productId) {
        return webClient
                .delete()
                .uri("/feedback-api/v1/favourite-products/by-product-id/{productId}", productId)
                .retrieve()
                .toBodilessEntity()
                .then();
    }

    @Override
    public Flux<FavouriteProduct> findFavouriteProducts() {
        return webClient
                .get()
                .uri("/feedback-api/v1/favourite-products")
                .retrieve()
                .bodyToFlux(FavouriteProduct.class);
    }
}
