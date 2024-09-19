package own.eteryz.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import own.eteryz.customer.client.ProductsClient;
import own.eteryz.customer.controller.payload.NewProductReviewPayload;
import own.eteryz.customer.entity.Product;
import own.eteryz.customer.service.FavouriteProductService;
import own.eteryz.customer.service.ProductReviewsService;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products/{productId:\\d+}")
public class ProductController {
    private final ProductsClient productsClient;

    private final FavouriteProductService favouriteProductService;

    private final ProductReviewsService productReviewsService;

    @ModelAttribute(name = "product", binding = false)
    public Mono<Product> loadProduct(@PathVariable("productId") int productId) {
        return this.productsClient.findProduct(productId);
    }


    @GetMapping
    public Mono<String> getProductPage(
            @PathVariable("productId") int productId,
            Model model) {
        model.addAttribute("inFavourite", false);
        return this.productReviewsService.findProductReviewsByProductId(productId)
                .collectList()
                .doOnNext(productReviews -> model.addAttribute("reviews", productReviews))
                .then(this.favouriteProductService.findFavouriteProductByProductId(productId)
                        .doOnNext(favouriteProduct -> model.addAttribute("inFavourite", true))
                )
                .thenReturn("customer/products/product");
    }

    @PostMapping("add-to-favourites")
    public Mono<String> addProductToFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductService.addFavouriteProduct(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }

    @PostMapping("delete-from-favourites")
    public Mono<String> deleteProductFromFavourites(@ModelAttribute("product") Mono<Product> productMono) {
        return productMono
                .map(Product::id)
                .flatMap(productId -> this.favouriteProductService.removeFavouriteProduct(productId)
                        .thenReturn("redirect:/customer/products/%d".formatted(productId)));

    }

    @PostMapping("create-review")
    public Mono<String> createReview(
            @PathVariable("productId") int productId,
            NewProductReviewPayload reviewPayload
    ) {
        return this.productReviewsService.createProductReview(
                        productId, reviewPayload.rating(), reviewPayload.review()
                ).thenReturn("redirect:/customer/products/%d".formatted(productId));
    }
}
