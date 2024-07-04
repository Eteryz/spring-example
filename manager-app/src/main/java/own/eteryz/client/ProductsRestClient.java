package own.eteryz.client;

import own.eteryz.model.Product;

import java.util.List;
import java.util.Optional;

public interface ProductsRestClient {

    List<Product> findAllProduct();

    Product createProduct(String title, String details);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String title, String details);

    void deleteProduct(Integer id);
}
