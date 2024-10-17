package own.eteryz.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import own.eteryz.client.ProductsRestClient;
import own.eteryz.client.exception.BadRequestException;
import own.eteryz.controller.payload.NewProductPayload;
import own.eteryz.model.Product;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products")
public class ProductsController {

    private final ProductsRestClient productsRestClient;

    @GetMapping("list")
    public String getProductList(Model model, @RequestParam(name = "filter", required = false) String filter) {
        model.addAttribute("products", this.productsRestClient.findAllProduct(filter));
        model.addAttribute("filter", filter);
        return "catalogue/products/list";
    }

    @GetMapping("create")
    public String getCreateProductPage() {
        return "catalogue/products/create";
    }

    @PostMapping("create")
    public String createProduct(
            NewProductPayload payload,
            Model model
    ) {
        try {
            Product product = this.productsRestClient.createProduct(payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", e.getErrors());
            return "catalogue/products/create";
        }
    }

}
