package own.eteryz.customer.client.payload;

public record NewProductReviewPayload(Integer productId, Integer rating, String review) {
}
