package javawizzards.officespace.service.Stripe;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Refund;
import com.stripe.model.checkout.Session;
import com.stripe.param.RefundCreateParams;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.PersistenceException;
import javawizzards.officespace.dto.Payment.*;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.entity.Payment;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.Payment.PaymentMessages;
import javawizzards.officespace.enumerations.Payment.PaymentStatus;
import javawizzards.officespace.enumerations.Util.Urls;
import javawizzards.officespace.exception.Payment.PaymentCustomException;
import javawizzards.officespace.repository.PaymentRepository;
import javawizzards.officespace.service.Email.EmailService;
import javawizzards.officespace.service.User.UserService;
import org.hibernate.HibernateException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.TransactionSystemException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class PaymentServiceImpl implements PaymentService {
    private Stripe stripe;

    @Value("${STRIPE_SECRET_KEY}")
    private String secretKey;

    private final UserService userService;
    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;
    private final EmailService emailService;

    public PaymentServiceImpl(UserService userService, ModelMapper modelMapper,
                          PaymentRepository paymentRepository, EmailService emailService) {
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.paymentRepository = paymentRepository;
        this.emailService = emailService;
    }

    @PostConstruct
    public void init() {
        stripe.apiKey = secretKey;
    }

    public PaymentResponse confirmPayment(ConfirmPaymentRequest paymentRequest) {
        try {
            if (paymentRequest == null) {
                throw new PaymentCustomException.PaymentValidationException();
            }

            Session session = Session.retrieve(paymentRequest.getSessionId());
            PaymentIntent paymentIntent = PaymentIntent.retrieve(session.getPaymentIntent());
            String chargeId = paymentIntent.getLatestCharge();
            PaymentStatus status = PaymentStatus.SUCCEEDED;

            if ("expired".equals(session.getStatus()) ||
                    "canceled".equals(session.getStatus())) {
                status = PaymentStatus.CANCELLED;
            }

            if (!"succeeded".equals(paymentIntent.getStatus())) {
                status = PaymentStatus.FAILED;
            }

            if (paymentRepository.findByChargeId(chargeId).isPresent()) {
                throw new PaymentCustomException.PaymentAlreadyProcessedException();
            }

            Long stripeAmount = paymentIntent.getAmount();
            Long requestAmount = paymentRequest.getAmount().multiply(new BigDecimal("100")).longValue();

            if (!stripeAmount.equals(requestAmount)) {
                throw new PaymentCustomException.PaymentAmountMismatch();
            }

            User user = userService.getUserEntityByEmail(session.getCustomerEmail());

            Payment payment = new Payment();
            payment.setId(UUID.randomUUID());
            payment.setAmount(paymentRequest.getAmount());
            payment.setCurrency(paymentRequest.getCurrency().toLowerCase());
            payment.setDescription(paymentRequest.getDescription());
            payment.setCreatedAt(LocalDateTime.now());
            payment.setPaidAt(LocalDateTime.now());
            payment.setCustomerEmail(user.getEmail());
            payment.setUser(user);
            payment.setCustomerId(session.getCustomer());
            payment.setPaymentIntentId(paymentIntent.getId());
            payment.setChargeId(chargeId);
            payment.setStatus(status);

            payment = paymentRepository.save(payment);

            return modelMapper.map(payment, PaymentResponse.class);
        }
        catch (PaymentCustomException e) {
            throw e;
        }
        catch (Exception e) {
            throw new PaymentCustomException.PaymentProcessingException();
        }
    }

    public SessionResponse createCheckoutSession(PaymentRequest paymentRequest) {
        Stripe.apiKey = secretKey;

        try {
            if (paymentRequest == null) {
                throw new PaymentCustomException.PaymentValidationException();
            }

            if (paymentRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
                throw new PaymentCustomException.InvalidAmountException();
            }

            User user = this.userService.getUserEntityById(paymentRequest.getUserId());

            Long amountInCents = paymentRequest.getAmount().multiply(new BigDecimal("100")).longValue();

            SessionCreateParams params = SessionCreateParams.builder()
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setCustomerEmail(user.getEmail())
                    .setSuccessUrl(Urls.PAYMENT_SUCCESS_REDIRECT.getUrl())
                    .setCancelUrl(Urls.PAYMENT_FAILURE_REDIRECT.getUrl())
                    .addLineItem(SessionCreateParams.LineItem.builder()
                            .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                    .setCurrency(paymentRequest.getCurrency())
                                    .setProductData(SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                            .setName("Reservation Payment")
                                            .setDescription(paymentRequest.getDescription())
                                            .build())
                                    .setUnitAmount(amountInCents)
                                    .build())
                            .setQuantity(paymentRequest.getQuantity())
                            .build())
                    .build();

            Session session = Session.create(params);
            return new SessionResponse(session.getUrl(), session.getSuccessUrl(), session.getCancelUrl(), session.getId());

        } catch (StripeException e) {
            throw new PaymentCustomException.StripeApiException();
        } catch (PaymentCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new PaymentCustomException.PaymentProcessingException();
        }
    }

    public Response makeRefund(String chargeId) {
        Stripe.apiKey = secretKey;

        try {
            if (chargeId == null || chargeId.trim().isEmpty()) {
                throw new PaymentCustomException.PaymentValidationException();
            }

            RefundCreateParams params = RefundCreateParams.builder()
                    .setCharge(chargeId)
                    .build();

            Payment payment = this.paymentRepository.findByChargeId(chargeId).orElseThrow(() -> new PaymentCustomException.PaymentNotFoundException());
            payment.setStatus(PaymentStatus.REFUNDED);

            Refund refund = Refund.create(params);
            return new Response<>(refund.getStatus(), HttpStatus.OK, PaymentMessages.PAYMENT_REFUNDED_SUCCESSFULLY.getMessage());

        }
        catch (PaymentCustomException e) {
            throw e;
        }
        catch (StripeException e) {
            throw new PaymentCustomException.RefundFailedException();
        }
    }

    public void resendStripeReceipt(String chargeId) {
        try {
            Stripe.apiKey = secretKey;

            Charge charge = Charge.retrieve(chargeId);

            if (charge == null || charge.getReceiptUrl() == null) {
                throw new PaymentCustomException.PaymentNotFoundException();
            }

            Payment payment = paymentRepository.findByChargeId(chargeId)
                    .orElseThrow(PaymentCustomException.PaymentNotFoundException::new);

            if (!"succeeded".equals(charge.getStatus())) {
                throw new PaymentCustomException.PaymentValidationException();
            }

            emailService.sendPaymentReceipt(
                    payment.getCustomerEmail(),
                    payment.getUser().getUsername(),
                    payment.getAmount(),
                    payment.getCurrency(),
                    payment.getDescription(),
                    charge.getReceiptUrl(),
                    payment.getPaidAt().format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
                    chargeId
            );
        } catch (StripeException e) {
            throw new PaymentCustomException.StripeApiException();
        } catch (Exception e) {
            throw new PaymentCustomException.PaymentProcessingException();
        }
    }

    public PaymentResponse findById(UUID id) {
        try {
            Payment payment = paymentRepository.findById(id).orElseThrow(PaymentCustomException.PaymentNotFoundException::new);

            return modelMapper.map(payment, PaymentResponse.class);
        } catch (Exception e) {
            throw new PaymentCustomException.PaymentProcessingException();
        }
    }

    public PaymentResponse findByCustomerEmail(String email) {
        try {
            Payment payment = paymentRepository.findByCustomerEmail(email).orElseThrow(PaymentCustomException.PaymentNotFoundException::new);

            return modelMapper.map(payment, PaymentResponse.class);
        }
        catch (PaymentCustomException e) {
            throw e;
        }
        catch (Exception e) {
            throw new PaymentCustomException.PaymentProcessingException();
        }
    }
}