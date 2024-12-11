package javawizzards.officespace.dto.Payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class ConfirmPaymentRequest {

    @JsonProperty("amount")
    @NotNull(message = "Amount cannot be null")
    private BigDecimal amount;

    @JsonProperty("currency")
    @NotNull(message = "Currency cannot be null")
    private String currency;

    @JsonProperty("description")
    @NotNull(message = "Description cannot be null")
    private String description;

    @JsonProperty("user_id")
    @NotNull(message = "User ID cannot be null")
    private UUID userId;

    @JsonProperty("session_id")
    @NotNull(message = "Session ID cannot be null")
    private String sessionId;
}
