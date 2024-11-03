package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoogleUserDto implements Serializable {
    @NotNull(message = "GoogleId can't be null")
    @JsonProperty("googleId")
    private String googleId;

    @NotNull(message = "Email can't be null")
    @Email(message = "Email is invalid")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Username can't be null")
    @JsonProperty("username")
    private String username;

    @JsonProperty("pictureUrl")
    private String pictureUrl;
}
