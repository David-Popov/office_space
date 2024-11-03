package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.enumerations.User.UserMessages;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.JwtUtility;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
