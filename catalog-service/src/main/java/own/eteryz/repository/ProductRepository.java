package own.eteryz.repository;

import org.springframework.data.repository.CrudRepository;
import own.eteryz.entity.Product;


public interface ProductRepository extends CrudRepository<Product, Integer> {

}
