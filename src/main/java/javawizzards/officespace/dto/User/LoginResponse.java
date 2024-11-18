package javawizzards.officespace.dto.User;

import javawizzards.officespace.dto.Response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
@AllArgsConstructor
public class LoginResponse implements Serializable  {
    private String token;
    private String refreshToken;

    public LoginResponse() {

        this.token = "";
        this.refreshToken = "";
    }
}
