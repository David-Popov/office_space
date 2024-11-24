package javawizzards.officespace.service.User;

import javawizzards.officespace.dto.User.*;
import javawizzards.officespace.entity.Role;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.repository.UserRepository;
import javawizzards.officespace.service.JwtService.JwtService;
import javawizzards.officespace.service.Role.RoleService;
import javawizzards.officespace.utility.JwtUtility;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtUtility jwtUtility;
    private final JwtService jwtService;
    private final Logger logger;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder, RoleService roleService, JwtUtility jwtUtility, JwtService jwtService) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtUtility = jwtUtility;
        this.jwtService = jwtService;
        this.logger = Logger.getLogger(this.getClass().getName());
    }

    @Override
    public UserDto getUser(UUID id) {
        try{
            User user = this.userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UsernameNotFoundException("User not found");
            }

            UserDto userDto = this.modelMapper.map(user, UserDto.class);
//            userDto.setRoleName(this.setUserDtoRoleName(userDto));
            this.setUserDtoRoleName(userDto);
            return userDto;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UserDto> getUsers() {
        try{
            List<User> users = this.userRepository.findAll();

            List<UserDto> userDtos = this.modelMapper.map(users, List.class);

            userDtos.stream()
                    .peek(user -> user.setRoleName(this.roleService.findRoleById(user.getRoleId()).getName()))
                    .toList();

            return userDtos;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto findById(UUID id) {
        try{
            User user = userRepository.findById(id).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return this.MapUserToDto(user);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto findByEmail(String email) {
        try{
            User user = userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            return this.MapUserToDto(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDto findByUsername(String username) {
        return null;
    }

    @Override
    public UserDto registerUser(RegisterUserDto userDto) {
        try {
            if (userRepository.existsByEmail(userDto.getEmail())) {
                throw new UserCustomException.UserAlreadyExistsException();
            }

            User user = new User();
            user.setEmail(userDto.getEmail());
            user.setUsername(userDto.getUsername());
            user.setPassword(hashPassword(userDto.getPassword()));
            user.setPictureUrl(userDto.getPictureUrl());
            user.setFirstName(userDto.getFirstName());
            user.setLastName(userDto.getLastName());
            user.setPhone(userDto.getPhone());
            user.setAddress(userDto.getAddress());

            Role role = this.roleService.findRoleByName(RoleEnum.USER.getRoleName());
            user.setRole(role);

            userRepository.save(user);

            return this.MapUserToDto(user);
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
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public LoginResponse loginUser(LoginUserDto loginUserDto) {
        try{
            User user = this.userRepository.findByEmail(loginUserDto.getEmail()).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            if (!passwordEncoder.matches(loginUserDto.getPassword(), user.getPassword())) {
                throw new UserCustomException.PasswordMismatchException();
            }

            String token = this.jwtService.generateNormalUserToken(user);
            String refreshToken = this.jwtService.generateRefreshToken(user);
            UserDto userDto = this.MapUserToDto(user);
            this.setUserDtoRoleName(userDto);

            user.setRefreshToken(refreshToken);
            this.userRepository.save(user);

            LoginResponse response = new LoginResponse(userDto, token, refreshToken);

            return response;
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public LoginResponse checkIfRefreshTokenIsValidAndGenerateNewTokens(String email, String refreshToken) {
        try{
            User user = this.userRepository.findByEmail(email).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            if (refreshToken.equals(user.getRefreshToken())) {
                String newToken = this.jwtService.generateNormalUserToken(user);
                String newRefreshToken = this.jwtService.generateRefreshToken(user);
                UserDto userDto = this.MapUserToDto(user);
                this.setUserDtoRoleName(userDto);

                user.setRefreshToken(refreshToken);
                this.userRepository.save(user);

                return new LoginResponse(userDto, newToken, newRefreshToken);
            }

            return new LoginResponse();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String loginGoogleUser(LoginGoogleUserDto userDto) {
        try{
            User user = this.userRepository.findByGoogleId(userDto.getGoogleId()).orElse(null);

            if (user == null) {
                throw new UserCustomException.GoogleUserNotFoundException();
            }

            return this.jwtService.generateGoogleUserToken(user);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public UserDto updateUser(UserDto userDto) {
        try{
            User userForUpdate = this.userRepository.findByEmail(userDto.getEmail()).orElse(null);

            if (userForUpdate == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            userForUpdate.setFirstName(userDto.getFirstName());
            userForUpdate.setLastName(userDto.getLastName());
            userForUpdate.setPhone(userDto.getPhone());
            userForUpdate.setAddress(userDto.getAddress());
            userForUpdate.setPictureUrl(userDto.getPictureUrl());
            userForUpdate.setUsername(userDto.getUsername());

            this.userRepository.save(userForUpdate);

            return this.MapUserToDto(userForUpdate);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public GoogleUserDto updateGoogleUser(GoogleUserDto userDto) {
        try{
            User userForUpdate = this.userRepository.findByGoogleId(userDto.getGoogleId()).orElse(null);

            if (userForUpdate == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            userForUpdate.setUsername(userDto.getUsername());
            userForUpdate.setPictureUrl(userDto.getPictureUrl());
            userForUpdate.setEmail(userDto.getEmail());

            this.userRepository.save(userForUpdate);

            return MapUserToGoogleUserDto(userForUpdate);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public void updatePassword(ChangeUserPasswordDto userDto) {
        try{
            User user = this.userRepository.findByEmail(userDto.getEmail()).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            if (!passwordEncoder.matches(userDto.getOldPassword(), user.getPassword())){
                throw new UserCustomException.PasswordMismatchException();
            }

            user.setPassword(hashPassword(userDto.getNewPassword()));
            this.userRepository.save(user);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDto deleteUser(UUID id) {
        try{
            User userForDelete = this.userRepository.findById(id).orElse(null);

            if (userForDelete == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            this.userRepository.delete(userForDelete);

            return this.MapUserToDto(userForDelete);
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = this.userRepository.findByEmail(email).orElse(null);

        if (user == null) {
            throw new UserCustomException.UserNotFoundException();
        }
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                new ArrayList<>()
        );
    }

    private String hashPassword(String password) {
        return passwordEncoder.encode(password);
    }

    private UserDto MapUserToDto(User user) {
        try{
            return this.modelMapper.map(user, UserDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private GoogleUserDto MapUserToGoogleUserDto(User user) {
        try{
            return this.modelMapper.map(user, GoogleUserDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UserDto MapUserDtoToUserEntity(User user) {
        try{
            return this.modelMapper.map(user, UserDto.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void setUserDtoRoleName(UserDto user){
        String roleName = this.roleService.findRoleById(user.getRoleId()).getName();
        user.setRoleName(roleName);
    }
}
