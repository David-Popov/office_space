package javawizzards.officespace.dto.User;

import javawizzards.officespace.dto.Response.Response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LoginResponse extends Response implements Serializable  {
    private String token;
}
