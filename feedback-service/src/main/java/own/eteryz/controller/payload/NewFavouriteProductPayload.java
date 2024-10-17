package own.eteryz.controller.payload;

import jakarta.validation.constraints.NotNull;

public record NewFavouriteProductPayload(
        @NotNull(message = "{feedback.products.favourites.create.errors.product_id_is_null}")
        int productId
) {
}
