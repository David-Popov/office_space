package javawizzards.officespace.controller;

import jakarta.validation.Valid;
import javawizzards.officespace.dto.Payment.ConfirmPaymentRequest;
import javawizzards.officespace.dto.Payment.PaymentResponse;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.Payment.PaymentRequest;
import javawizzards.officespace.dto.Payment.SessionResponse;
import javawizzards.officespace.enumerations.Payment.PaymentMessages;
import javawizzards.officespace.exception.Payment.PaymentCustomException;
import javawizzards.officespace.service.Stripe.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    private PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-session")
    public ResponseEntity<Response<?>> createSession(
            @Valid @RequestBody Request<PaymentRequest> request,
            BindingResult bindingResult) {

        Response<?> response = null;

        try {
            if (bindingResult.hasErrors()) {
                String errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining(", "));

                return ResponseEntity.badRequest().body(new Response<>(errorMessages));
            }

            if (request == null || request.getData() == null) {
                throw new PaymentCustomException.PaymentRequestIsNull();
            }

            SessionResponse sessionResponse = paymentService.createCheckoutSession(request.getData());

            return ResponseEntity.ok(new Response<>(sessionResponse, HttpStatus.CREATED, PaymentMessages.PAYMENT_SUCCESSFULLY_CREATED.getMessage()));

        }
        catch (PaymentCustomException e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.badRequest().body(response);}
        catch (Exception e) {
            response =new Response<>(null, HttpStatus.INTERNAL_SERVER_ERROR, PaymentMessages.UNEXPECTED_ERROR.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/confirm")
    public ResponseEntity<Response<?>> confirmPayment(@Valid @RequestBody Request<ConfirmPaymentRequest> request, BindingResult bindingResult) {
        Response<?> response = null;

        try{
            if (bindingResult.hasErrors()) {
                String errorMessages = bindingResult.getFieldErrors()
                        .stream()
                        .map(error -> error.getField() + ": " + error.getDefaultMessage())
                        .collect(Collectors.joining(", "));

                return ResponseEntity.badRequest().body(new Response<>(errorMessages));
            }

            PaymentResponse paymentResponse = paymentService.confirmPayment(request.getData());

            response = new Response<>(paymentResponse, HttpStatus.OK, PaymentMessages.PAYMENT_CONFIRMED_SUCCESSFULLY.getMessage());
            return ResponseEntity.ok(response);
        }
        catch (PaymentCustomException e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.badRequest().body(response);}
        catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/refund/{chargeId}")
    public ResponseEntity<Response<?>> refundPayment(@PathVariable String chargeId) {
        Response<?> response = null;

        try {
            Response refundResponse = paymentService.makeRefund(chargeId);

            response = new Response<>(refundResponse, HttpStatus.OK,
                    PaymentMessages.REFUND_PROCESSED_SUCCESSFULLY.getMessage());
            return ResponseEntity.ok(response);
        }
        catch (PaymentCustomException e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.badRequest().body(response);}
        catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/resend-receipt/{chargeId}")
    public ResponseEntity<Response<?>> resendReceipt(@PathVariable String chargeId) {
        Response<?> response = null;

        try {
            paymentService.resendStripeReceipt(chargeId);

            response = new Response<>(null, HttpStatus.OK,
                    PaymentMessages.RECEIPT_RESENT_SUCCESSFULLY.getMessage());
            return ResponseEntity.ok(response);
        }
        catch (PaymentCustomException e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.badRequest().body(response);}
        catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<?>> getPaymentById(@PathVariable UUID id) {
        Response<?> response = null;

        try {
            PaymentResponse paymentResponse = paymentService.findById(id);

            response = new Response<>(paymentResponse, HttpStatus.OK,
                    PaymentMessages.PAYMENT_RETRIEVED_SUCCESSFULLY.getMessage());
            return ResponseEntity.ok(response);
        }
        catch (PaymentCustomException e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.badRequest().body(response);}
        catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Response<?>> getPaymentByCustomerEmail(@PathVariable String email) {
        Response<?> response = null;

        try {
            PaymentResponse paymentResponse = paymentService.findByCustomerEmail(email);

            response = new Response<>(paymentResponse, HttpStatus.OK,
                    PaymentMessages.PAYMENT_RETRIEVED_SUCCESSFULLY.getMessage());
            return ResponseEntity.ok(response);
        }
        catch (PaymentCustomException e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.badRequest().body(response);}
        catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
