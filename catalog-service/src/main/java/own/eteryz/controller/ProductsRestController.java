package own.eteryz.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.StringToClassMapItem;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
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
    @Operation(responses = {
            @ApiResponse(
                    responseCode = "200",
                    headers = @Header(name = "Content-Type", description = "Тип данных"),
                    content = @Content(schema = @Schema(implementation = Product.class))
            )
    })
    public Iterable<Product> getProducts(@RequestParam(name = "filter", required = false) String filter) {
        return this.productService.findAllProduct(filter);
    }

    @PostMapping
    @Operation(
            security = @SecurityRequirement(name = "keycloak", scopes = {"edit_catalogue"}),
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(
                                    type = "object",
                                    properties = {
                                            @StringToClassMapItem(key = "title", value = String.class),
                                            @StringToClassMapItem(key = "details", value = String.class)
                                    }
                            ))
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            headers = @Header(name = "Content-Type", description = "Тип данных"),
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(
                                            type = "object",
                                            properties = {
                                                    @StringToClassMapItem(key = "id", value = Integer.class),
                                                    @StringToClassMapItem(key = "title", value = String.class),
                                                    @StringToClassMapItem(key = "details", value = String.class)
                                            }
                                    ))
                    )
            }
    )
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
                            .replacePath("/api/v1/catalog/products/{productId}")
                            .build(Map.of("productId", product.getId())))
                    .body(product);
        }
    }

}
