package own.eteryz.client;

import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
import own.eteryz.client.exception.BadRequestException;
import own.eteryz.controller.payload.NewProductPayload;
import own.eteryz.controller.payload.UpdateProductPayload;
import own.eteryz.model.Product;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RequiredArgsConstructor
public class RestClientProductsRestClientImpl implements ProductsRestClient {

    private final RestClient restClient;

    private static final ParameterizedTypeReference<List<Product>> PRODUCTS_TYPE_REFERENCE =
            new ParameterizedTypeReference<>() {
            };

    @Override
    public List<Product> findAllProduct() {
        return this.restClient
                .get()
                .uri("/catalog/products")
                .retrieve()
                .body(PRODUCTS_TYPE_REFERENCE);
    }

    @Override
    public Product createProduct(String title, String details) throws BadRequestException {
        try {
            return this.restClient
                    .post()
                    .uri("/catalog/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new NewProductPayload(title, details))
                    .retrieve()
                    .body(Product.class);
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public Optional<Product> findProduct(int productId) {
        try {
            return Optional.ofNullable(this.restClient
                    .get()
                    .uri("/products/{productId}", productId)
                    .retrieve()
                    .body(Product.class));
        } catch (HttpClientErrorException.NotFound exception) {
            return Optional.empty();
        }
    }

    @Override
    public void updateProduct(Integer id, String title, String details) throws BadRequestException {
        try {
            this.restClient
                    .patch()
                    .uri("/products/{productId}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(new UpdateProductPayload(title, details))
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.BadRequest exception) {
            ProblemDetail problemDetail = exception.getResponseBodyAs(ProblemDetail.class);
            throw new BadRequestException((List<String>) problemDetail.getProperties().get("errors"));
        }
    }

    @Override
    public void deleteProduct(Integer id) throws NoSuchElementException {
        try {
            this.restClient
                    .delete()
                    .uri("/products/{productId}", id)
                    .retrieve()
                    .toBodilessEntity();
        } catch (HttpClientErrorException.NotFound exception) {
            throw new NoSuchElementException(exception);
        }
    }
}
