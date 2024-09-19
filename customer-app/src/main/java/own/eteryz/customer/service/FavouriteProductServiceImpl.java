package own.eteryz.customer.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import own.eteryz.customer.entity.FavouriteProduct;
import own.eteryz.customer.repository.FavouriteProductRepository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FavouriteProductServiceImpl implements FavouriteProductService {

    private final FavouriteProductRepository favouriteProductRepository;

    @Override
    public Mono<FavouriteProduct> addFavouriteProduct(int productId) {
        return this.favouriteProductRepository.save(new FavouriteProduct(UUID.randomUUID(), productId));
    }

    @Override
    public Mono<Void> removeFavouriteProduct(int productId) {
        return this.favouriteProductRepository.deleteByProductId(productId);
    }

    @Override
    public Mono<FavouriteProduct> findFavouriteProductByProductId(int productId) {
        return this.favouriteProductRepository.findByProductId(productId);
    }
}
