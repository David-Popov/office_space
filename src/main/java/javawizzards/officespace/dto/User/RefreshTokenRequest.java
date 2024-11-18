package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest implements Serializable {
    @NotNull
    @Email(message = "Email is invalid")
    @JsonProperty("email")
    String email;

    @NotNull
    @JsonProperty("refreshToken")
    String refreshToken;
}
