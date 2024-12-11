package javawizzards.officespace.exception.Payment;

import javawizzards.officespace.enumerations.Payment.PaymentMessages;

public abstract  class PaymentCustomException extends RuntimeException {
    protected PaymentCustomException(String message) {
        super(message);
    }

    public static class PaymentNotFoundException extends PaymentCustomException {
        public PaymentNotFoundException() {
            super(PaymentMessages.PAYMENT_NOT_FOUND.getMessage());
        }
    }

    public static class PaymentProcessingException extends PaymentCustomException {
        public PaymentProcessingException() {
            super(PaymentMessages.PAYMENT_PROCESSING_ERROR.getMessage());
        }
    }

    public static class PaymentValidationException extends PaymentCustomException {
        public PaymentValidationException() {
            super(PaymentMessages.PAYMENT_VALIDATION_ERROR.getMessage());
        }
    }

    public static class PaymentRequestIsNull extends PaymentCustomException {
        public PaymentRequestIsNull() {
            super(PaymentMessages.PAYMENT_REQUEST_NULL.getMessage());
        }
    }

    public static class StripeApiException extends PaymentCustomException {
        public StripeApiException() {
            super(PaymentMessages.STRIPE_API_ERROR.getMessage());
        }
    }

    public static class InsufficientFundsException extends PaymentCustomException {
        public InsufficientFundsException() {
            super(PaymentMessages.INSUFFICIENT_FUNDS.getMessage());
        }
    }

    public static class PaymentAlreadyProcessedException extends PaymentCustomException {
        public PaymentAlreadyProcessedException() {
            super(PaymentMessages.PAYMENT_ALREADY_PROCESSED.getMessage());
        }
    }

    public static class InvalidAmountException extends PaymentCustomException {
        public InvalidAmountException() {
            super(PaymentMessages.INVALID_AMOUNT.getMessage());
        }
    }

    public static class PaymentCancelledException extends PaymentCustomException {
        public PaymentCancelledException() {
            super(PaymentMessages.PAYMENT_CANCELLED.getMessage());
        }
    }

    public static class PaymentExpiredException extends PaymentCustomException {
        public PaymentExpiredException() {
            super(PaymentMessages.PAYMENT_EXPIRED.getMessage());
        }
    }

    public static class RefundFailedException extends PaymentCustomException {
        public RefundFailedException() {
            super(PaymentMessages.REFUND_FAILED.getMessage());
        }
    }

    public static class InvalidCurrencyException extends PaymentCustomException {
        public InvalidCurrencyException() {
            super(PaymentMessages.INVALID_CURRENCY.getMessage());
        }
    }

    public static class PaymentAmountMismatch extends PaymentCustomException {
        public PaymentAmountMismatch() {
            super(PaymentMessages.PAYMENT_AMOUNT_MISMATCH.getMessage());
        }
    }
}
