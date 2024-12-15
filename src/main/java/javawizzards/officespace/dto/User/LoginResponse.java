package javawizzards.officespace.dto.User;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class LoginResponse implements Serializable  {
    private UserDto user;
    private String token;
    private String refreshToken;

    public LoginResponse() {
        this.token = "";
        this.refreshToken = "";
    }
}
