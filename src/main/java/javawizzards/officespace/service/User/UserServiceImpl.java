package javawizzards.officespace.service.User;

import javawizzards.officespace.dto.User.*;
import javawizzards.officespace.entity.Role;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.repository.UserRepository;
import javawizzards.officespace.service.JwtService.JwtService;
import javawizzards.officespace.service.Role.RoleService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtService jwtService;
    private final Logger logger;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder, RoleService roleService, JwtService jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtService = jwtService;
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    public UserDto getUser(UUID id) {
        try {
            User user = this.userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            UserDto userDto = this.modelMapper.map(user, UserDto.class);
            this.setUserDtoRoleName(userDto, user.getRole());
            return userDto;
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserEntityById(UUID id) {
        try {
            User user = this.userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return user;
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserEntityByEmail(String email) {
        try {
            User user = this.userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return user;
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDto> getUsers() {
        try {
            List<User> users = this.userRepository.findAll();

            return users.stream()
                    .map(user -> {
                        UserDto dto = modelMapper.map(user, UserDto.class);
                        this.setUserDtoRoleName(dto, user.getRole());
                        return dto;
                    })
                    .collect(Collectors.toList());
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto findById(UUID id) {
        try {
            User user = userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto findByEmail(String email) {
        try {
            if (email.isEmpty()) {
                throw new RuntimeException("Email cannot be empty");
            }

            User user = userRepository.findByEmail(email).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            UserDto userDto = this.MapUserToDto(user);
            setUserDtoRoleName(userDto, user.getRole());
            return userDto;
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDto findByUsername(String username) {
        try {
            if (username.isEmpty()) {
                throw new RuntimeException("Username cannot be empty");
            }

            User user = userRepository.findByUsername(username).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDto registerUser(RegisterUserDto userDto) {
        try {
            if (userDto == null) {
                throw new RuntimeException("UserDto cannot be null");
            }

            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserCustomException.UserAlreadyExistsException();
            }

            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setPassword(hashPassword(userDto.getPassword()));
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhone(userDto.getPhone());

            Role role = this.roleService.findRoleByName(RoleEnum.USER.getRoleName());
            user.setRole(role);

            userRepository.save(user);

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDto registerGoogleUser(RegisterGoogleUserDto userDto) {
        try {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserCustomException.UserAlreadyExistsException();
            }

            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setGoogleId(userDto.getGoogleId());
            user.setPictureUrl(userDto.getPictureUrl());

            Role role = this.roleService.findRoleByName(RoleEnum.USER.getRoleName());
            user.setRole(role);

            userRepository.save(user);

            return this.MapUserToDto(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public LoginResponse loginUser(LoginUserDto loginUserDto) {
        try {
            if (loginUserDto == null) {
                throw new RuntimeException("LoginUserDto cannot be null");
            }

            User user = this.userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
                throw new UserCustomException.PasswordMismatchException();
            }

            String token = this.jwtService.generateNormalUserToken(user);
            String refreshToken = this.jwtService.generateRefreshToken(user);
            UserDto userDto = this.MapUserToDto(user);
            this.setUserDtoRoleName(userDto, user.getRole());

            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);

            userDto.setRoleId(user.getRole().getId());
            userDto.setRoleName(user.getRole().getName());
            return new LoginResponse(userDto, token, refreshToken);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    public UserDto getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated() ||
                authentication instanceof AnonymousAuthenticationToken) {
            throw new RuntimeException("No authenticated user found");
        }

        String email = authentication.getName(); // Gets username/email used during authentication
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserCustomException.UserNotFoundException());

        return this.modelMapper.map(user, UserDto.class);
    }

    @Override
    public LoginResponse checkIfRefreshTokenIsValidAndGenerateNewTokens(String email, String refreshToken) {
        try {
            if (email.isEmpty() || refreshToken.isEmpty()) {
                throw new RuntimeException("Email and refresh token cannot be empty");
            }

            User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            if (refreshToken.equals(user.getRefreshToken())) {
                String newToken = this.jwtService.generateNormalUserToken(user);
                String newRefreshToken = this.jwtService.generateRefreshToken(user);
                UserDto userDto = this.MapUserToDto(user);
                this.setUserDtoRoleName(userDto, user.getRole());

                user.setRefreshToken(refreshToken);
                this.userRepository.save(user);

                return new LoginResponse(userDto, newToken, newRefreshToken);
            }

            return new LoginResponse();
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String loginGoogleUser(LoginGoogleUserDto userDto) {
        try {
            if (userDto == null) {
                throw new RuntimeException("LoginGoogleUserDto cannot be null");
            }

            User user = this.userRepository.findByGoogleId(userDto.getGoogleId()).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            return this.jwtService.generateGoogleUserToken(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDto updateUser(UUID userId, UpdateUserRequest userRequest) {
        try {
            if (userRequest == null) {
                throw new RuntimeException("UserDto cannot be null");
            }

            User userForUpdate = this.userRepository.findById(userId).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            Role role = this.roleService.findRoleById(userRequest.getRoleId());

            userForUpdate.setFirstName(userRequest.getFirstName());
            userForUpdate.setLastName(userRequest.getLastName());
            userForUpdate.setPhone(userRequest.getPhone());
            userForUpdate.setUsername(userRequest.getUsername());
            userForUpdate.setRole(role);
            userForUpdate.setEmail(userRequest.getEmail());

            this.userRepository.save(userForUpdate);

            return this.MapUserToDto(userForUpdate);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public GoogleUserDto updateGoogleUser(GoogleUserDto userDto) {
        try {
            if (userDto == null) {
                throw new RuntimeException("UserDto cannot be null");
            }

            User userForUpdate = this.userRepository.findByGoogleId(userDto.getGoogleId()).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            userForUpdate.setUsername(userDto.getUsername());
            userForUpdate.setPictureUrl(userDto.getPictureUrl());
            userForUpdate.setEmail(userDto.getEmail());

            this.userRepository.save(userForUpdate);

            return MapUserToGoogleUserDto(userForUpdate);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void updatePassword(ChangeUserPasswordDto userDto) {
        try {
            if (userDto == null) {
                throw new RuntimeException("UserDto cannot be null");
            }

            User user = this.userRepository.findByEmail(userDto.getEmail()).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            if (!passwordEncoder.matches(userDto.getOldPassword(), user.getPassword())) {
                throw new UserCustomException.PasswordMismatchException();
            }

            user.setPassword(hashPassword(userDto.getNewPassword()));
            this.userRepository.save(user);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDto deleteUser(UUID id) {
        try {
            if (id.toString().isEmpty()) {
                throw new UserCustomException.InvalidId();
            }

            User userForDelete = this.userRepository.findById(id).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            this.userRepository.delete(userForDelete);

            return this.MapUserToDto(userForDelete);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        try {
            User user = this.userRepository.findByEmail(email).orElseThrow(() -> new UserCustomException.UserNotFoundException());

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    new ArrayList<>()
            );
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private UserDto MapUserToDto(User user) {
        try {
            UserDto dto = modelMapper.map(user, UserDto.class);

//            dto.setReservations(user.getReservations().stream()
//                    .map(reservation -> modelMapper.map(reservation, ReservationDto.class))
//                    .collect(Collectors.toList()));
//
//            dto.setPayments(user.getPayments().stream()
//                    .map(payment -> modelMapper.map(payment, PaymentResponse.class))
//                    .collect(Collectors.toList()));

            return dto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GoogleUserDto MapUserToGoogleUserDto(User user) {
        try {
            return this.modelMapper.map(user, GoogleUserDto.class);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserDto MapUserDtoToUserEntity(User user) {
        try {
            return this.modelMapper.map(user, UserDto.class);
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setUserDtoRoleName(UserDto userDto, Role role) {
        try {
            userDto.setRoleName(role.getName());
            userDto.setRoleId(role.getId());
        } catch (UserCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}