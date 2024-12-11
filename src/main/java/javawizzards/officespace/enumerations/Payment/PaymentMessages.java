package javawizzards.officespace.enumerations.Payment;

public enum PaymentMessages {
    PAYMENT_NOT_FOUND("Payment not found"),
    PAYMENT_PROCESSING_ERROR("Error processing payment"),
    PAYMENT_VALIDATION_ERROR("Payment validation failed"),
    PAYMENT_REQUEST_NULL("Payment request is null"),
    STRIPE_API_ERROR("Error communicating with Stripe API"),
    INSUFFICIENT_FUNDS("Insufficient funds for this transaction"),
    PAYMENT_ALREADY_PROCESSED("Payment has already been processed"),
    INVALID_AMOUNT("Invalid payment amount"),
    PAYMENT_CANCELLED("Payment was cancelled"),
    PAYMENT_EXPIRED("Payment session has expired"),
    REFUND_FAILED("Failed to process refund"),
    INVALID_CURRENCY("Invalid currency specified"),
    UNEXPECTED_ERROR("An unexpected error occurred while processing your payment"),
    PAYMENT_AMOUNT_MISMATCH("Amount mismatch"),
    PAYMENT_CONFIRMED_SUCCESSFULLY("Payment confirmed successfully"),
    PAYMENT_REFUNDED_SUCCESSFULLY("Refund processed successfully"),
    PAYMENT_RETRIEVED_SUCCESSFULLY("Payment retrieved successfully"),
    PAYMENT_RECEIPT_SENT_SUCCESSFULLY("Receipt resent successfully"),
    PAYMENT_SESSION_CREATED_SUCCESSFULLY("Payment session was created successfully"),
    REFUND_PROCESSED_SUCCESSFULLY("Refund was processed successfully"),
    RECEIPT_RESENT_SUCCESSFULLY("Receipt was resent successfully"),
    PAYMENT_SUCCESSFULLY_CREATED("Payment session was created successfully");

    private final String message;

    PaymentMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
