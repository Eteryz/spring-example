package own.eteryz.controller;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import own.eteryz.client.ProductsRestClient;
import own.eteryz.client.exception.BadRequestException;
import own.eteryz.model.Product;
import own.eteryz.controller.payload.UpdateProductPayload;

import java.util.Locale;
import java.util.NoSuchElementException;

@Controller
@RequiredArgsConstructor
@RequestMapping("catalogue/products/{productId:\\d+}")
public class ProductController {

    private final ProductsRestClient productsRestClient;

    private final MessageSource messageSource;

    @ModelAttribute("product")
    public Product product(@PathVariable("productId") int productId) {
        return this.productsRestClient.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public String getProduct() {
        return "catalogue/products/product";
    }

    @GetMapping("edit")
    public String getProductEditPage() {
        return "catalogue/products/edit";
    }

    @PostMapping("edit")
    public String updateProduct(
            @ModelAttribute(value = "product", binding = false) Product product,
            UpdateProductPayload payload,
            Model model
    ) {
        try {
            this.productsRestClient.updateProduct(product.id(), payload.title(), payload.details());
            return "redirect:/catalogue/products/%d".formatted(product.id());
        } catch (BadRequestException e) {
            model.addAttribute("payload", payload);
            model.addAttribute("errors", e.getErrors());
            return "catalogue/products/edit";
        }
    }

    @PostMapping("delete")
    public String deleteProduct(@ModelAttribute("product") Product product) {
        this.productsRestClient.deleteProduct(product.id());
        return "redirect:/catalogue/products/list";
    }

    @ExceptionHandler
    public String handleNoSuchElementException(
            NoSuchElementException e,
            Model model,
            HttpServletResponse response,
            Locale locale
    ) {
        response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        model.addAttribute("error",
                this.messageSource.getMessage(
                        e.getMessage(), new Object[0], e.getMessage(), locale
                )
        );
        return "errors/404";
    }
}
