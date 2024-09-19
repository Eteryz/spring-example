package own.eteryz.customer.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import own.eteryz.customer.client.ProductsClient;
import own.eteryz.customer.entity.FavouriteProduct;
import own.eteryz.customer.service.FavouriteProductService;
import reactor.core.publisher.Mono;

@Controller
@RequiredArgsConstructor
@RequestMapping("customer/products")
public class ProductsController {

    private final ProductsClient productsClient;
    private final FavouriteProductService favouriteProductService;

    @GetMapping("list")
    public Mono<String> getProductsListPage(
            Model model,
            @RequestParam(name = "filter", required = false) String filter
    ) {
        model.addAttribute("filter", filter);
        return this.productsClient.findAllProducts(filter)
                .collectList()
                .doOnNext(products -> model.addAttribute("products", products))
                .thenReturn("customer/products/list");
    }

    @GetMapping("favourites")
    public Mono<String> getFavouritesPage(
            Model model,
            @RequestParam(name = "filter", required = false) String filter
    ) {
        model.addAttribute("filter", filter);
        return this.favouriteProductService.findFavouriteProducts()
                .map(FavouriteProduct::getProductId)
                .collectList()
                .flatMap(favouriteProducts -> this.productsClient.findAllProducts(filter)
                        .filter(product -> favouriteProducts.contains(product.id()))
                        .collectList()
                        .doOnNext(products -> model.addAttribute("products", products))
                ).thenReturn("customer/products/favourites");
    }
}
