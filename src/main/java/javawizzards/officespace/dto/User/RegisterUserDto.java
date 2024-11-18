package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.dto.Request.Request;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class RegisterUserDto implements Serializable {

    @NotNull(message = "Email can't be null")
    @Email(message = "Email is invalid")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Username can't be null")
    @JsonProperty("username")
    private String username;

    @NotNull(message = "Password can't be null")
    @JsonProperty("password")
    private String password;

    @JsonProperty("pictureUrl")
    private String pictureUrl;

    @NotNull(message = "FirstName can't be null")
    @JsonProperty("firstName")
    private String firstName;

    @NotNull(message = "LastName can't be null")
    @JsonProperty("lastName")
    private String lastName;

    @NotNull(message = "Phone can't be null")
    @JsonProperty("phone")
    private String phone;

    @JsonProperty("address")
    private String address;
}
