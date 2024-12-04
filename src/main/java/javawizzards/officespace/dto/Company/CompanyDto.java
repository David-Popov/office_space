package javawizzards.officespace.dto.Company;

import lombok.Getter;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
public class CompanyDto implements Serializable{
    @NotNull(message = "Id can't be null")
    @JsonProperty("id")
    private UUID id;

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
