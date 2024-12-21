package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @NotNull(message = "Email can't be null")
    @Email(message = "Email is invalid")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Username can't be null")
    @JsonProperty("username")
    private String username;

    @NotNull(message = "FirstName can't be null")
    @JsonProperty("firstName")
    private String firstName;

    @NotNull(message = "LastName can't be null")
    @JsonProperty("lastName")
    private String lastName;

    @NotNull(message = "Phone can't be null")
    @JsonProperty("phone")
    private String phone;

    @NotNull
    @JsonProperty("roleId")
    private int roleId;
}
