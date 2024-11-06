package javawizzards.officespace.service.User;

import javawizzards.officespace.dto.User.*;

import java.util.UUID;

public interface UserService {
    UserDto findById(UUID id);
    UserDto findByEmail(String email);
    UserDto findByUsername(String username);
    UserDto registerUser(RegisterUserDto userDto);
    UserDto registerGoogleUser(RegisterGoogleUserDto userDto);
    LoginResponse loginUser(LoginUserDto userDto);
    LoginResponse checkIfRefreshTokenIsValidAndGenerateNewTokens(String email, String refreshToken);
    String loginGoogleUser(LoginGoogleUserDto userDto);
    UserDto updateUser(UserDto userDto);
    GoogleUserDto updateGoogleUser(GoogleUserDto userDto);
    void updatePassword(ChangeUserPasswordDto userDto);
    UserDto deleteUser(UUID id);
}
