package javawizzards.officespace.controller;

import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.UserDto;
import javawizzards.officespace.enumerations.OfficeRoom.OfficeRoomMessages;
import javawizzards.officespace.enumerations.User.UserMessages;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.time.LocalDateTime;
import java.util.List;
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
            response = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse("Id:" + id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Response<UserDto>> getUserByEmail(
            @PathVariable(name = "email") String email) throws JsonProcessingException {
    
        Response<UserDto> response;
    
        try {
            var userDto = this.userService.findByEmail(email);
            response = new Response<>(userDto, HttpStatus.OK, UserMessages.USER_FETCH_SUCCESS.getMessage());
    
            return ResponseEntity.ok(response);
    
        } catch (UserCustomException e) {
            response = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
        
            return ResponseEntity.badRequest().body(response);
    
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
    
}
