package javawizzards.officespace.dto.Role;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class RoleDto implements Serializable {

    @NotNull(message = "Id can't be null")
    @JsonProperty("id")
    private int id;

    @NotNull(message = "Name can't be null")
    @JsonProperty("name")
    private String name;
}
