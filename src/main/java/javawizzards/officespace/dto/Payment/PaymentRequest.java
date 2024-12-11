package javawizzards.officespace.dto.Payment;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
public class PaymentRequest {

    @NotNull(message = "Source cannot be null")
    @JsonProperty("source")
    public String source;

    @NotNull(message = "Amount cannot be null")
    @JsonProperty("amount")
    public BigDecimal amount;

    @NotNull(message = "Currency cannot be null")
    @JsonProperty("currency")
    public String currency;

    @NotNull(message = "Description cannot be null")
    @JsonProperty("description")
    public String description;

    @NotNull(message = "Count cannot be null")
    @JsonProperty("count")
    public String count;

    @NotNull(message = "Quantity cannot be null")
    @JsonProperty("quantity")
    public Long quantity;

    @NotNull(message = "User ID cannot be null")
    @JsonProperty("userId")
    public UUID userId;
}
