package own.eteryz.customer.controller.payload;

public record NewProductReviewPayload(
        Integer rating,
        String review
) {
}
