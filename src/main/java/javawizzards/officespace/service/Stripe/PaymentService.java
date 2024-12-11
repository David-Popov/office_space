package javawizzards.officespace.service.Stripe;

import javawizzards.officespace.dto.Payment.ConfirmPaymentRequest;
import javawizzards.officespace.dto.Payment.PaymentRequest;
import javawizzards.officespace.dto.Payment.PaymentResponse;
import javawizzards.officespace.dto.Payment.SessionResponse;
import javawizzards.officespace.dto.Response.Response;

import java.util.UUID;

public interface PaymentService {
    PaymentResponse confirmPayment(ConfirmPaymentRequest paymentRequest);
    SessionResponse createCheckoutSession(PaymentRequest paymentRequest);
    Response makeRefund(String chargeId);
    void resendStripeReceipt(String chargeId);
    PaymentResponse findById(UUID id);
    PaymentResponse findByCustomerEmail(String email);
}
