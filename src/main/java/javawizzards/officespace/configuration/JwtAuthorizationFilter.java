package javawizzards.officespace.configuration;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javawizzards.officespace.utility.JwtUtility;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtility jwtUtility;

    private final UserDetailsService userDetailsService;

    public JwtAuthorizationFilter(JwtUtility jwtUtility, @Lazy UserDetailsService userDetailsService) {
        this.jwtUtility = jwtUtility;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            System.out.println("Authorization header is missing or malformed: " + authorizationHeader);
            chain.doFilter(request, response);
            return;
        }

        String jwt = authorizationHeader.substring(7);

        try {
            if (jwtUtility.validateToken(jwt)) {
                String email = jwtUtility.extractEmail(jwt);
                String roleName = jwtUtility.extractRole(jwt);

                System.out.println("JWT Token validated. Email: " + email + ", Role: " + roleName);

                if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
                        System.out.println("User details loaded successfully for email: " + email);

                        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + roleName);
                        List<GrantedAuthority> combinedAuthorities = new ArrayList<>(userDetails.getAuthorities());
                        combinedAuthorities.add(authority);

                        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                                userDetails, null, combinedAuthorities);
                        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        SecurityContextHolder.getContext().setAuthentication(authentication);

                        System.out.println("Authentication set in SecurityContext. Authorities: " + combinedAuthorities);
                    } catch (Exception e) {
                        System.err.println("Error loading user details: " + e.getMessage());
                        e.printStackTrace();
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }
                } else {
                    System.out.println("Email is null or authentication already exists in context");
                }
            } else {
                System.err.println("JWT Token validation failed");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        } catch (Exception e) {
            System.err.println("Error processing JWT token: " + e.getMessage());
            System.err.println("Token that caused error: " + jwt);
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        try {
            chain.doFilter(request, response);
        } catch (Exception e) {
            System.err.println("Error in filter chain: " + e.getMessage());
            e.printStackTrace();
            throw e;
        }
    }
}
