package javawizzards.officespace.service;

import javawizzards.officespace.dto.User.*;
import javawizzards.officespace.entity.Role;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.repository.UserRepository;
import javawizzards.officespace.service.JwtService.JwtService;
import javawizzards.officespace.service.Role.RoleService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.service.User.UserServiceImpl;
import javawizzards.officespace.utility.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleService roleService;

    @Mock
    private JwtService jwtService;

    @Mock
    private JwtUtility jwtUtility;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    private ModelMapper modelMapper;
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
        userService = new UserServiceImpl(
                userRepository,
                modelMapper,
                passwordEncoder,
                roleService,
                jwtUtility,
                jwtService
        );
    }

    @Test
    void findById_ExistingUser_ReturnsUserDto() {
        UUID id = UUID.randomUUID();
        User user = createTestUser();
        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDto result = userService.findById(id);

        assertNotNull(result);
        assertEquals(user.getEmail(), result.getEmail());
        assertEquals(user.getUsername(), result.getUsername());
    }

    @Test
    void findById_NonExistingUser_ThrowsException() {
        UUID id = UUID.randomUUID();
        when(userRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(UserCustomException.UserNotFoundException.class, () -> {
            try {
                userService.findById(id);
            } catch (RuntimeException e) {
                if (e.getCause() instanceof UserCustomException.UserNotFoundException) {
                    throw (UserCustomException.UserNotFoundException) e.getCause();
                }
                throw e;
            }
        });
    }

    @Test
    void findByEmail_ExistingUser_ReturnsUserDto() {
        String email = "test@test.com";
        User user = createTestUser();
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));

        UserDto result = userService.findByEmail(email);

        assertNotNull(result);
        assertEquals(email, result.getEmail());
    }

    @Test
    void findByEmail_NonExistingUser_ThrowsException() {
        String email = "nonexisting@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        assertThrows(UserCustomException.UserNotFoundException.class,
                () -> userService.findByEmail(email));
    }

    @Test
    void registerUser_ValidData_ReturnsUserDto() {
        RegisterUserDto registerDto = new RegisterUserDto();
        registerDto.setEmail("test@test.com");
        registerDto.setPassword("password");
        registerDto.setUsername("testuser");

        User savedUser = new User();
        savedUser.setId(UUID.randomUUID());
        savedUser.setEmail(registerDto.getEmail());
        savedUser.setUsername(registerDto.getUsername());

        Role userRole = new Role();
        userRole.setId(1);
        userRole.setName(RoleEnum.USER.getRoleName());

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(roleService.findRoleByName(RoleEnum.USER.getRoleName())).thenReturn(userRole);
        when(passwordEncoder.encode(any())).thenReturn("hashedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDto result = userService.registerUser(registerDto);

        assertNotNull(result);
        assertEquals(registerDto.getEmail(), result.getEmail());
        assertEquals(registerDto.getUsername(), result.getUsername());
    }

    @Test
    void loginUser_ValidCredentials_ReturnsLoginResponse() {
        LoginUserDto loginDto = new LoginUserDto();
        loginDto.setEmail("test@test.com");
        loginDto.setPassword("password");

        User user = createTestUser();
        when(userRepository.findByEmail(loginDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(loginDto.getPassword(), user.getPassword())).thenReturn(true);
        when(jwtService.generateNormalUserToken(user)).thenReturn("token");
        when(jwtService.generateRefreshToken(user)).thenReturn("refreshToken");

        LoginResponse result = userService.loginUser(loginDto);

        assertNotNull(result);
        assertNotNull(result.getToken());
        assertNotNull(result.getRefreshToken());
    }

    @Test
    void updateUser_ValidData_ReturnsUpdatedUserDto() {
        UserDto updateDto = new UserDto();
        updateDto.setEmail("test@test.com");
        updateDto.setUsername("updatedUsername");

        User existingUser = createTestUser();
        when(userRepository.findByEmail(updateDto.getEmail())).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDto result = userService.updateUser(updateDto);

        assertNotNull(result);
        assertEquals(updateDto.getUsername(), result.getUsername());
    }

    @Test
    void deleteUser_ExistingUser_ReturnsDeletedUserDto() {
        UUID userId = UUID.randomUUID();
        User user = createTestUser();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDto result = userService.deleteUser(userId);

        assertNotNull(result);
        verify(userRepository).delete(user);
    }

    @Test
    void updatePassword_ValidData_Success() {
        ChangeUserPasswordDto passwordDto = new ChangeUserPasswordDto();
        passwordDto.setEmail("test@test.com");
        passwordDto.setOldPassword("oldPassword");
        passwordDto.setNewPassword("newPassword");

        User user = createTestUser();
        String hashedNewPassword = "hashedNewPassword";

        when(userRepository.findByEmail(passwordDto.getEmail())).thenReturn(Optional.of(user));
        when(passwordEncoder.matches(passwordDto.getOldPassword(), user.getPassword())).thenReturn(true);
        when(passwordEncoder.encode(passwordDto.getNewPassword())).thenReturn(hashedNewPassword);
        when(userRepository.save(any(User.class))).thenReturn(user);

        userService.updatePassword(passwordDto);

        verify(passwordEncoder).encode(passwordDto.getNewPassword());
        verify(userRepository).save(argThat(savedUser ->
                savedUser.getPassword().equals(hashedNewPassword)
        ));
    }

    private User createTestUser() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setEmail("test@test.com");
        user.setUsername("testuser");
        user.setPassword("hashedPassword");

        Role role = new Role();
        role.setId(1);
        role.setName(RoleEnum.USER.getRoleName());
        user.setRole(role);

        return user;
    }
}