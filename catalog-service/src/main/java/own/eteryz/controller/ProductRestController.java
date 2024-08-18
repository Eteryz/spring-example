package own.eteryz.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import own.eteryz.controller.payload.UpdateProductPayload;
import own.eteryz.entity.Product;
import own.eteryz.service.ProductService;

import java.util.Locale;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping("${apiPrefix.v1}/catalog/products/{productId:\\d+}")
public class ProductRestController {

    private final ProductService productService;
    private final MessageSource messageSource;

    @ModelAttribute("product")
    private Product getProduct(@PathVariable("productId") int productId) {
        return this.productService.findProduct(productId)
                .orElseThrow(() -> new NoSuchElementException("catalogue.errors.product.not_found"));
    }

    @GetMapping
    public Product findProduct(@ModelAttribute("product") Product product) {
        return product;
    }

    @PatchMapping
    public ResponseEntity<Void> updateProduct(
            @PathVariable("productId") int productId,
            @Valid @RequestBody UpdateProductPayload payload,
            BindingResult bindingResult
    ) throws BindException {
        if (bindingResult.hasErrors()) {
            if (bindingResult instanceof BindException exception)
                throw exception;
            throw new BindException(bindingResult);
        } else {
            this.productService.updateProduct(productId, payload.title(), payload.details());
            return ResponseEntity.noContent().build();
        }
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(@PathVariable("productId") int productId) {
        this.productService.deleteProduct(productId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ProblemDetail> handleNoSuchElementException(
            NoSuchElementException e,
            Locale locale
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ProblemDetail.forStatusAndDetail(
                        HttpStatus.NOT_FOUND,
                        this.messageSource.getMessage(e.getMessage(), new Object[0],
                                e.getMessage(), locale)
                ));
    }
}
