package own.eteryz.customer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.server.csrf.CsrfToken;
import org.springframework.security.web.reactive.result.view.CsrfRequestDataValueProcessor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import own.eteryz.customer.client.FavouriteProductClient;
import own.eteryz.customer.client.ProductReviewsClient;
import own.eteryz.customer.client.ProductsClient;
import own.eteryz.customer.client.exception.ClientBadRequestException;
import own.eteryz.customer.controller.payload.NewProductReviewPayload;
import own.eteryz.customer.entity.Product;
import reactor.core.publisher.Mono;

import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
@Slf4j
public class ProductController {
    private final ProductsClient productsClient;

    private final FavouriteProductClient favouriteProductClient;

    private final ProductReviewsClient productReviewsClient;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int productId) {
        return this.productsClient.findProduct(productId)
                .switchIfEmpty(Mono.error(new NoSuchElementException("customer.products.error.not_found")));
    }


    @GetMapping
    public Mono<String> getProductPage(
            @PathVariable("productId") int productId,
            Model model) {
        model.addAttribute("inFavourite", false);
        return this.productReviewsClient.findProductReviewsByProductId(productId)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favouriteProductClient.findFavouriteProductByProductId(productId)
                        .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true))
                )
                .thenReturn("customer/products/product");
    }

    @PostMapping("add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductClient.addFavouriteProduct(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId))
                        .onErrorResume(ex -> {
                            log.error(ex.getMessage(), ex);
                            return Mono.just("redirect:/customer/products/%d".formatted(productId));
                        }));
    }

    @PostMapping("delete-from-favourites")
    public Mono<String> deleteProductFromFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductClient.removeFavouriteProduct(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }

    @PostMapping("create-review")
    public Mono<String> createReview(
            @PathVariable("productId") int productId,
            NewProductReviewPayload reviewPayload,
            Model model
    ) {
        return this.productReviewsClient.createProductReview(
                        productId, reviewPayload.rating(), reviewPayload.review()
                ).thenReturn("redirect:/customer/products/%d".formatted(productId))
                .onErrorResume(ClientBadRequestException.class, e -> {
                    model.addAttribute("inFavourite", false);
                    model.addAttribute("payload", reviewPayload);
                    model.addAttribute("errors", e.getErrors());
                    return this.favouriteProductClient.findFavouriteProductByProductId(productId)
                            .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true))
                            .thenReturn("customer/products/product");
                });

    }

    @ExceptionHandler(NoSuchElementException.class)
    public String handleNoSuchElementException(NoSuchElementException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "errors/404";
    }

    @ModelAttribute
    public Mono<CsrfToken> loadCsrfToken(ServerWebExchange exchange) {
        return Objects.requireNonNull(
                        exchange.<Mono<CsrfToken>>getAttribute(CsrfToken.class.getName())
                )
                .doOnSuccess(csrfToken -> exchange.getAttributes()
                        .put(CsrfRequestDataValueProcessor.DEFAULT_CSRF_ATTR_NAME, csrfToken));
    }
}
