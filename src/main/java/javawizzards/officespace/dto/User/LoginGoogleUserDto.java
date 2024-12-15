package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class LoginGoogleUserDto implements Serializable {
    @NotNull(message = "GoogleId can't be null")
    @JsonProperty("googleId")
    private String googleId;
}
