package javawizzards.officespace.dto.User;

import javawizzards.officespace.dto.Response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;


@Getter
@Setter
public class LoginResponse implements Serializable  {
    private String token;

    public LoginResponse() {
        this.token = "";
    }
}
