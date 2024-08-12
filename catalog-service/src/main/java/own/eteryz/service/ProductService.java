package own.eteryz.service;


import own.eteryz.entity.Product;

import java.util.Optional;

public interface ProductService {

    Iterable<Product> findAllProduct(String filter);

    Product createProduct(String title, String details);

    Optional<Product> findProduct(int productId);

    void updateProduct(Integer id, String title, String details);

    void deleteProduct(Integer id);
}
