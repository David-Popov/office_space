package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.enumerations.User.UserMessages;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final RequestAndResponseService requestAndResponseService;

    public UserController(UserService userService, RequestAndResponseService requestAndResponseService) {
        this.userService = userService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @GetMapping("/get-data/{id}")
    public ResponseEntity<Response<?>> getUserData(@PathVariable UUID id) {
        Response<?> response;

        if (id == null || id.toString().isEmpty()) {
            return ResponseEntity.badRequest().body(new Response<>(UserMessages.INVALID_ID.getMessage()));
        }

        try {
            var data = this.userService.getUser(id);
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (UserCustomException e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
