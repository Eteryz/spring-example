package own.eteryz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import own.eteryz.controller.payload.NewProductPayload;
import own.eteryz.entity.Product;
import own.eteryz.service.ProductService;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix.v1}/catalog/products")
public class ProductsRestController {

    private final ProductService productService;

    @GetMapping
    public Iterable<Product> getProducts() {
        return this.productService.findAllProduct();
    }

    @PostMapping
    public ResponseEntity<?> createProduct(
            @Valid @RequestBody NewProductPayload payload,
            BindingResult bindingResult,
            UriComponentsBuilder uriBuilder
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception)
                throw exception;
            throw new BindException(bindingResult);
        } else {
            Product product = productService.createProduct(payload.title(), payload.details());
            return ResponseEntity
                    .created(uriBuilder
                            .pathSegment("/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }

}
