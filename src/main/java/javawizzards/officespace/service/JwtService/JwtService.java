package javawizzards.officespace.service.JwtService;

import javawizzards.officespace.entity.User;

import java.util.Map;

public interface JwtService {
    String generateNormalUserToken(User user);
    String generateGoogleUserToken(User user);
    String generateRefreshToken(User user);
    boolean validateToken(String token);
    String extractEmail(String token);
    String extractUsername(String token);
    String extractRole(String token);
    boolean checkForAdminRole(String token);
    Map<String, Object> extractAllClaims(String token);
}
