package javawizzards.officespace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.*;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.User.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AuthenticationControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private RequestAndResponseService requestAndResponseService;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private AuthenticationController authenticationController;

    private ObjectMapper objectMapper;
    private RegisterUserDto registerUserDto;
    private UserDto userDto;
    private UUID testUuid;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        testUuid = UUID.randomUUID();
        registerUserDto = new RegisterUserDto();
        registerUserDto.setEmail("test@example.com");
        registerUserDto.setPassword("password123");
        registerUserDto.setUsername("testuser");
        registerUserDto.setFirstName("John");
        registerUserDto.setLastName("Doe");
        registerUserDto.setPhone("+1234567890");

        userDto = new UserDto(
                UUID.randomUUID(),
                "test@example.com",
                "testuser",
                "John",
                "Doe",
                "+1234567890",
                1,
                "USER"
        );
    }

    @Test
    void registerUser_Success() throws Exception {
        Request<RegisterUserDto> request = new Request<>();
        request.setData(registerUserDto);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any(RegisterUserDto.class))).thenReturn(userDto);

        ResponseEntity<Response<String>> response = authenticationController.registerUser(request, bindingResult);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
        verify(userService).registerUser(any(RegisterUserDto.class));
    }

    @Test
    void registerUser_ValidationError() throws Exception {
        Request<RegisterUserDto> request = new Request<>();
        request.setData(new RegisterUserDto()); // Empty DTO to trigger validation

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("userDto", "email", "Email can't be null")
        );

        ResponseEntity<Response<String>> response = authenticationController.registerUser(request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        verify(userService, never()).registerUser(any());
    }

    @Test
    void changePassword_Success() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto();
        passwordDto.setOldPassword("oldPass123");
        passwordDto.setNewPassword("newPass123");

        Request<ChangeUserPasswordDto> request = new Request<>();
        request.setData(passwordDto);

        when(bindingResult.hasErrors()).thenReturn(false);
        doNothing().when(userService).updatePassword(any());

        ResponseEntity<Response<?>> response = authenticationController.changePassword(request, bindingResult);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }

    @Test
    void handleServiceException() throws Exception {
        Request<RegisterUserDto> request = new Request<>();
        request.setData(registerUserDto);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any())).thenThrow(new RuntimeException("Service error"));

        ResponseEntity<Response<String>> response = authenticationController.registerUser(request, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        verify(requestAndResponseService).CreateRequestAndResponse(any(), any(), any(), any());
    }

    @Test
    void refreshToken_Success() throws Exception {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setEmail("test@example.com");
        refreshRequest.setRefreshToken("valid-refresh-token");

        Request<RefreshTokenRequest> request = new Request<>();
        request.setData(refreshRequest);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("new-token");
        loginResponse.setRefreshToken("new-refresh-token");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.checkIfRefreshTokenIsValidAndGenerateNewTokens(any(), any())).thenReturn(loginResponse);

        ResponseEntity<Response<?>> response = authenticationController.refreshToken(request, bindingResult);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void refreshToken_InvalidToken() throws Exception {
        RefreshTokenRequest refreshRequest = new RefreshTokenRequest();
        refreshRequest.setEmail("test@example.com");
        refreshRequest.setRefreshToken("invalid-token");

        Request<RefreshTokenRequest> request = new Request<>();
        request.setData(refreshRequest);

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setToken("");
        loginResponse.setRefreshToken("");

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.checkIfRefreshTokenIsValidAndGenerateNewTokens(any(), any())).thenReturn(loginResponse);

        ResponseEntity<Response<?>> response = authenticationController.refreshToken(request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

//    @Test
//    void updateUser_Success() throws Exception {
//        UserDto updatedUserDto = new UserDto(
//                testUuid,
//                "updated@example.com",
//                "updateduser",
//                "UpdatedFirst",
//                "UpdatedLast",
//                "+9876543210",
//                1,
//                "USER"
//        );
//
//        Request<UserDto> request = new Request<>();
//        request.setData(updatedUserDto);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(userService.updateUser(any(UserDto.class))).thenReturn(updatedUserDto);
//
//        ResponseEntity<Response<?>> response = authenticationController.updateUser(request, bindingResult);
//
//        assertNotNull(response);
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//    }

//    @Test
//    void updateUser_ValidationErrors() throws Exception {
//        UserDto invalidUserDto = new UserDto(); // Empty DTO
//        Request<UserDto> request = new Request<>();
//        request.setData(invalidUserDto);
//
//        when(bindingResult.hasErrors()).thenReturn(true);
//        when(bindingResult.getFieldError()).thenReturn(
//                new FieldError("userDto", "email", "Email cannot be null")
//        );
//
//        ResponseEntity<Response<?>> response = authenticationController.updateUser(request, bindingResult);
//
//        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
//    }

    @Test
    void changePassword_ValidationError() throws Exception {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto();
        Request<ChangeUserPasswordDto> request = new Request<>();
        request.setData(passwordDto);

        when(bindingResult.hasErrors()).thenReturn(true);
        when(bindingResult.getFieldError()).thenReturn(
                new FieldError("passwordDto", "oldPassword", "Old password cannot be null")
        );

        ResponseEntity<Response<?>> response = authenticationController.changePassword(request, bindingResult);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void handleServiceException_UserRegistration() throws Exception {
        Request<RegisterUserDto> request = new Request<>();
        request.setData(registerUserDto);

        when(bindingResult.hasErrors()).thenReturn(false);
        when(userService.registerUser(any())).thenThrow(new RuntimeException("Database error"));

        ResponseEntity<Response<String>> response = authenticationController.registerUser(request, bindingResult);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }

//    @Test
//    void handleServiceException_UserUpdate() throws Exception {
//        Request<UserDto> request = new Request<>();
//        request.setData(userDto);
//
//        when(bindingResult.hasErrors()).thenReturn(false);
//        when(userService.updateUser(any())).thenThrow(new RuntimeException("Update failed"));
//
//        ResponseEntity<Response<?>> response = authenticationController.updateUser(request, bindingResult);
//
//        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
//    }
}