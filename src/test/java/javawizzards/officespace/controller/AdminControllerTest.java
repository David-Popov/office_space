package javawizzards.officespace.controller;

import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.UserDto;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.JwtUtility;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RequestAndResponseService requestAndResponseService;

    @Mock
    private JwtUtility jwtUtility;

    @InjectMocks
    private AdminController adminController;

    private UUID validUserId;
    private UUID nonExistentUserId;
    private UserDto deletedUserDto;

    void setUp() {
        validUserId = UUID.randomUUID();
        nonExistentUserId = UUID.randomUUID();

        deletedUserDto = new UserDto(
                validUserId,
                "test@example.com",
                "testuser",
                "password123",
                "http://example.com/picture.jpg",
                "John",
                "Doe",
                "+1234567890",
                "123 Test Street",
                1
        );
    }

    @Test
    void deleteUser_Success() {
        when(userService.deleteUser(validUserId)).thenReturn(deletedUserDto);

        ResponseEntity<Response> response = adminController.deleteUser(validUserId);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.OK, response.getBody().getStatus());

        verify(userService).deleteUser(validUserId);
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }

    @Test
    void deleteUser_NullId() {
        ResponseEntity<Response> response = adminController.deleteUser(null);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());

        verify(userService, never()).deleteUser(any());
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }

    @Test
    void deleteUser_UserNotFound() {
        when(userService.deleteUser(nonExistentUserId)).thenReturn(null);

        ResponseEntity<Response> response = adminController.deleteUser(nonExistentUserId);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());

        verify(userService).deleteUser(nonExistentUserId);
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }

    @Test
    void deleteUser_ServiceThrowsException() {
        String errorMessage = "Database connection error";
        when(userService.deleteUser(validUserId)).thenThrow(new RuntimeException(errorMessage));

        ResponseEntity<Response> response = adminController.deleteUser(validUserId);

        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getBody().getStatus());

        verify(userService).deleteUser(validUserId);
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }

    @Test
    void deleteUser_EmptyUUID() {
        UUID emptyUUID = UUID.fromString("00000000-0000-0000-0000-000000000000");

        ResponseEntity<Response> response = adminController.deleteUser(emptyUUID);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());

        verify(userService).deleteUser(emptyUUID);
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }

    @Test
    void deleteUser_ServiceReturnsNull() {
        when(userService.deleteUser(any())).thenReturn(null);

        ResponseEntity<Response> response = adminController.deleteUser(validUserId);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());

        verify(userService).deleteUser(validUserId);
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }
}