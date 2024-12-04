package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto implements Serializable {
    @JsonProperty("Id")
    private UUID id;

    @NotNull(message = "Email can't be null")
    @Email(message = "Email is invalid")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Username can't be null")
    @JsonProperty("username")
    private String username;

//    @JsonProperty("pictureUrl")
//    private String pictureUrl;

    @NotNull(message = "FirstName can't be null")
    @JsonProperty("firstName")
    private String firstName;

    @NotNull(message = "LastName can't be null")
    @JsonProperty("lastName")
    private String lastName;

    @NotNull(message = "Phone can't be null")
    @JsonProperty("phone")
    private String phone;

//    @JsonProperty("address")
//    private String address;

    @NotNull
    @JsonProperty("roleId")
    private int roleId;

    @NotNull
    @JsonProperty("roleName")
    private String roleName;
}
