package javawizzards.officespace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.*;
import javawizzards.officespace.entity.RequestAndResponse;
import javawizzards.officespace.enumerations.User.UserMessages;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    private final UserService userService;
    private final RequestAndResponseService requestAndResponseService;

    public AuthenticationController(UserService userService, RequestAndResponseService requestAndResponseService) {
        this.userService = userService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @PostMapping("/register")
    public ResponseEntity<Response<String>> registerUser(@RequestBody Request<RegisterUserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<String> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            var registeredUser = this.userService.registerUser(request.getData());

            if (registeredUser == null) {
                response = new Response<>(UserMessages.REGISTER_FAILED.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            response = new Response<>(HttpStatus.OK, UserMessages.REGISTER_SUCCESS.getMessage());

            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/google-register")
    public ResponseEntity<Response<String>> registerGoogleUser(@RequestBody Request<RegisterGoogleUserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<String> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            var registeredUser = this.userService.registerGoogleUser(request.getData());
            if (registeredUser == null) {
                response = new Response<>(UserMessages.REGISTER_FAILED.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            response = new Response<>(HttpStatus.OK, UserMessages.REGISTER_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Response<?>> loginUser(@RequestBody Request<LoginUserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<LoginResponse> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            LoginResponse loginResponse = this.userService.loginUser(request.getData());
            if (loginResponse.getToken().isEmpty() || loginResponse.getRefreshToken().isEmpty()) {
                response = new Response<>(UserMessages.USER_NOT_FOUND.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this),LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            response = new Response<>(loginResponse, HttpStatus.OK, UserMessages.REGISTER_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this),LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Response<?>> refreshToken(@RequestBody Request<RefreshTokenRequest> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try{
            LoginResponse loginResponse = this.userService.checkIfRefreshTokenIsValidAndGenerateNewTokens(request.getData().getEmail(), request.getData().getRefreshToken());

            if (loginResponse.getToken().isEmpty() || loginResponse.getRefreshToken().isEmpty()) {
                response = new Response<>(UserMessages.USER_NOT_FOUND.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this),LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            response = new Response<>(loginResponse, HttpStatus.OK, UserMessages.REFRESH_TOKEN_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/google-login")
    public ResponseEntity<Response<String>> loginGoogleUser(@RequestBody Request<LoginGoogleUserDto> loginUserDto, BindingResult bindingResult) throws JsonProcessingException {
        Response<String> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            String token = this.userService.loginGoogleUser(loginUserDto.getData());
            if (token.isEmpty()) {
                response = new Response<>(UserMessages.USER_NOT_FOUND.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(loginUserDto, response, LoggingUtils.logControllerName(this),LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            response = new Response<>(token, HttpStatus.OK, UserMessages.REGISTER_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(loginUserDto, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(loginUserDto, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }



    @GetMapping("/message")
    public ResponseEntity<String> changePassword() {
        return ResponseEntity.ok().body("hello");
    }

    @PutMapping("/change-password")
    public ResponseEntity<Response<?>> changePassword(@RequestBody Request<ChangeUserPasswordDto> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try{
            this.userService.updatePassword(request.getData());
            response = new Response<>(HttpStatus.OK, UserMessages.PASSWORD_CHANGED_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
