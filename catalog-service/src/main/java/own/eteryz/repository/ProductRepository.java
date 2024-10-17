package own.eteryz.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import own.eteryz.entity.Product;


public interface ProductRepository extends CrudRepository<Product, Integer> {
    Iterable<Product> findAllByTitleLikeIgnoreCase(String filter);

    @Query(value = "select p from Product p where p.title ilike :filter")
    Iterable<Product> findAllByTitleQuery(String filter);

    @Query(value = "select * from catalogue.t_product where c_title ilike :filter", nativeQuery = true)
    Iterable<Product> findAllByTitleNative(String filter);

    @Query(name = "Product.findAllByTitle", nativeQuery = true)
    Iterable<Product> findAllByTitleNameQuery(String filter);
}
