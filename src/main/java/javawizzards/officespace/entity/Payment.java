package javawizzards.officespace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.enumerations.Payment.PaymentStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    private UUID id;

    @NotNull
    private BigDecimal amount;

    @NotNull
    private String currency;

    @NotNull
    private String description;

    @NotNull
    private String customerEmail;

    private String customerId;

    @NotNull
    private LocalDateTime createdAt;

    private LocalDateTime paidAt;

    private String paymentIntentId;  // Store the PaymentIntent ID from Stripe

    private String chargeId;  // This will be populated after successful payment

    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
