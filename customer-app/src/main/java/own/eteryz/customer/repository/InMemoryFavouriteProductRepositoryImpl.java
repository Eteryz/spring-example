package own.eteryz.customer.repository;

import org.springframework.stereotype.Repository;
import own.eteryz.customer.entity.FavouriteProduct;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

@Repository
public class InMemoryFavouriteProductRepositoryImpl implements FavouriteProductRepository {

    private final List<FavouriteProduct> favouriteProductList = Collections.synchronizedList(new LinkedList<>());

    @Override
    public Mono<FavouriteProduct> save(FavouriteProduct favouriteProduct) {
        this.favouriteProductList.add(favouriteProduct);
        return Mono.just(favouriteProduct);
    }

    @Override
    public Mono<Void> deleteByProductId(int productId) {
        this.favouriteProductList.removeIf(product -> product.getProductId() == productId);
        return Mono.empty();
    }

    @Override
    public Mono<FavouriteProduct> findByProductId(int productId) {
        return Flux.fromIterable(this.favouriteProductList)
                .filter(favouriteProduct -> favouriteProduct.getProductId() == productId)
                .singleOrEmpty();
    }

    @Override
    public Flux<FavouriteProduct> findAll() {
        return Flux.fromIterable(this.favouriteProductList);
    }
}
