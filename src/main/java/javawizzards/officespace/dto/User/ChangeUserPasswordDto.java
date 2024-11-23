package javawizzards.officespace.dto.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;

@Data
public class ChangeUserPasswordDto implements Serializable {
    @NotNull(message = "Email can't be null")
    @JsonProperty("email")
    private String email;

    @NotNull(message = "Old password can't be null")
    @JsonProperty("oldPassword")
    private String oldPassword;

    @NotNull(message = "New Password can't be null")
    @JsonProperty("newPassword")
    private String newPassword;
}
