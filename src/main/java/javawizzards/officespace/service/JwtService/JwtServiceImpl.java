package javawizzards.officespace.service.JwtService;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.User.RoleEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class JwtServiceImpl implements JwtService {
    @Value("${security.jwt.secret-key}")
    private String SECRET_KEY;

    @Value("${security.jwt.expiration-time}")
    private long expirationTime;

    @Value("${security.jwt.refresh-token-expiration-time}")
    private long refreshTokenExpirationTime;

    private Key getSigningKey() {
        return new SecretKeySpec(SECRET_KEY.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    @Override
    public String generateNormalUserToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", user.getId());
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("phone", user.getPhone());
        claims.put("firstName", user.getFirstName());
        claims.put("lastName", user.getLastName());
        claims.put("roleId", user.getRole().getId());
        claims.put("roleName", user.getRole().getName());

        List<String> reservationIds = user.getReservations()
                .stream()
                .map(reservation -> reservation.getId().toString())
                .collect(Collectors.toList());
        claims.put("reservations", reservationIds);

        return generateToken(claims, user.getEmail(), expirationTime);
    }

    @Override
    public String generateGoogleUserToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());
        claims.put("googleId", user.getGoogleId());
        claims.put("roleId", user.getRole().getId());
        claims.put("roleName", user.getRole().getName());

        return generateToken(claims, user.getEmail(), expirationTime);
    }

    @Override
    public String generateRefreshToken(User user) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", user.getEmail());
        claims.put("username", user.getUsername());

        return generateToken(claims, user.getEmail(), refreshTokenExpirationTime);
    }

    private String generateToken(Map<String, Object> claims, String subject, long expiration) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    @Override
    public String extractEmail(String token) {
        return extractClaim(token, "email", String.class);
    }

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, "username", String.class);
    }

    @Override
    public String extractRole(String token) {
        return extractClaim(token, "roleName", String.class);
    }

    @Override
    public boolean checkForAdminRole(String token) {
        String roleName = extractRole(token);
        return roleName.equals(RoleEnum.ADMIN.getRoleName());
    }

    @Override
    public Map<String, Object> extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T extractClaim(String token, String claimName, Class<T> claimType) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get(claimName, claimType);
    }
}
