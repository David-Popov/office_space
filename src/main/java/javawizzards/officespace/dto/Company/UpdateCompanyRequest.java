package javawizzards.officespace.dto.Company;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateCompanyRequest {
    @NotNull(message = "Company name can't be null")
    @JsonProperty("name")
    private String name;

    @NotNull(message = "Company address can't be null")
    @JsonProperty("address")
    private String address;

    @NotNull(message = "Company type can't be null")
    @JsonProperty("type")
    private String type;
}
