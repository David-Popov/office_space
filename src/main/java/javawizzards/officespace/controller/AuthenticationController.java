package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.*;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.service.User.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response> registerUser(@RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult) {
        try{
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(new Response(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()));
            }

            var registeredUser = this.userService.registerUser(registerUserDto);

            if (registeredUser == null) {
                return ResponseEntity.badRequest().body(new Response("User registration failed"));
            }

            return ResponseEntity.ok(new Response("User registration successful"));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new Response(e.getMessage()));
        }
    }

    @PostMapping("/google-register")
    public ResponseEntity<Response> registerGoogleUser(@RequestBody RegisterGoogleUserDto registerUserDto, BindingResult bindingResult) {
        try{
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(new Response(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()));
            }

            var registeredUser = this.userService.registerGoogleUser(registerUserDto);

            if (registeredUser == null) {
                return ResponseEntity.badRequest().body(new Response("User registration failed"));
            }

            return ResponseEntity.ok(new Response("User registration successful"));
        }
        catch(Exception e){
            return ResponseEntity.internalServerError().body(new Response(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody LoginUserDto loginUserDto, BindingResult bindingResult) {
        try{
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(new LoginResponse(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()));
            }

            String token = this.userService.loginUser(loginUserDto);

            if (token.isEmpty()){
                return ResponseEntity.badRequest().body(new LoginResponse("User login failed"));
            }

            return ResponseEntity.ok(new LoginResponse(token));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(new LoginResponse(e.getMessage()));
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<LoginResponse> loginGoogleUser(@RequestBody LoginGoogleUserDto loginUserDto, BindingResult bindingResult) {
        try{
            if (bindingResult.hasErrors()) {
                return ResponseEntity.badRequest().body(new LoginResponse(Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage()));
            }

            String token = this.userService.loginGoogleUser(loginUserDto);

            if (token.isEmpty()){
                return ResponseEntity.badRequest().body(new LoginResponse("User login failed"));
            }

            return ResponseEntity.ok(new LoginResponse(token));
        }
        catch (Exception e){
            return ResponseEntity.internalServerError().body(new LoginResponse(e.getMessage()));
        }
    }
}
