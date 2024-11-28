package javawizzards.officespace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.GoogleUserDto;
import javawizzards.officespace.dto.User.UserDto;
import javawizzards.officespace.enumerations.User.UserMessages;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.JwtUtility;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RequestAndResponseService requestAndResponseService;
    private final JwtUtility jwtUtility;

    public AdminController(UserService userService, RequestAndResponseService requestAndResponseService, JwtUtility jwtUtility) {
        this.userService = userService;
        this.requestAndResponseService = requestAndResponseService;
        this.jwtUtility = jwtUtility;
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable UUID id) {
        Response response = null;
        try{
            if (id == null || id.toString().isEmpty()){
                response = new Response(HttpStatus.BAD_REQUEST, "Invalid Id");
                this.requestAndResponseService.CreateRequestAndResponse(id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            var data = this.userService.deleteUser(id);

            if (data == null){
                response = new Response(HttpStatus.BAD_REQUEST, UserMessages.USER_NOT_FOUND.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            response = new Response(HttpStatus.OK, UserMessages.USER_DELETED.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            var errorResponse = new Response<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e.getStackTrace().toString());
            this.requestAndResponseService.CreateRequestAndResponse(id, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<Response<?>> updateUser(@RequestBody Request<UserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try{
            var data = this.userService.updateUser(request.getData());
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/update-google-user")
    public ResponseEntity<Response<?>> updateGoogleUser(@RequestBody Request<GoogleUserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try{
            var data = this.userService.updateGoogleUser(request.getData());
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping()
    public ResponseEntity<Response<?>> getAllUsers() {
        Response<?> response;
        String requestId = UUID.randomUUID().toString();

        try{
            var data = this.userService.getUsers();
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(requestId, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(requestId, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }


}
