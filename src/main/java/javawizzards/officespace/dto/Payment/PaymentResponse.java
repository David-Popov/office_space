package javawizzards.officespace.dto.Payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class PaymentResponse {
    @Id
    @JsonProperty("id")
    private UUID id;

    @NotNull
    @JsonProperty("amount")
    private BigDecimal amount;

    @NotNull
    @JsonProperty("currency")
    private String currency;

    @NotNull
    @JsonProperty("description")
    private String description;

    @NotNull
    @JsonProperty("customerEmail")
    private String customerEmail;

    @NotNull
    @JsonProperty("customerId")
    private String customerId;

    @NotNull
    @JsonProperty("date")
    private LocalDateTime paidAt;

    @NotNull
    @JsonProperty("chargeId")
    private String chargeId;

    @NotNull
    @JsonProperty("userId")
    private UUID userId;
}
