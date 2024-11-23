package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.UserDto;
import javawizzards.officespace.enumerations.User.UserMessages;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RequestAndResponseService requestAndResponseService;

    @Mock
    private JwtUtility jwtUtility;

    private AdminController adminController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        adminController = new AdminController(userService, requestAndResponseService, jwtUtility);
    }

    @Test
    void deleteUser_NullId_ReturnsBadRequest() {
        var response = adminController.deleteUser(null);
        Response responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid Id", responseBody.getDescription());
    }

    @Test
    void deleteUser_UserNotFound_ReturnsBadRequest() {
        UUID invalidId = UUID.randomUUID();
        when(userService.deleteUser(invalidId)).thenReturn(null);

        var response = adminController.deleteUser(invalidId);
        Response responseBody = response.getBody();

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals(UserMessages.USER_NOT_FOUND.getMessage(), responseBody.getDescription());
        verify(userService).deleteUser(invalidId);
    }

    @Test
    void deleteUser_ValidId_Success() {
        UUID validId = UUID.randomUUID();
        UserDto userDto = new UserDto();
        when(userService.deleteUser(validId)).thenReturn(userDto);

        var response = adminController.deleteUser(validId);
        Response responseBody = response.getBody();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(UserMessages.USER_DELETED.getMessage(), responseBody.getDescription());
        verify(userService).deleteUser(validId);
    }

    @Test
    void deleteUser_ServiceThrowsException_ReturnsInternalServerError() {
        UUID validId = UUID.randomUUID();
        String errorMessage = "Database error";
        when(userService.deleteUser(validId)).thenThrow(new RuntimeException(errorMessage));

        var response = adminController.deleteUser(validId);
        Response responseBody = response.getBody();

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals(errorMessage, responseBody.getErrorDescription());
        verify(userService).deleteUser(validId);
    }
}