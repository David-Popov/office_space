package javawizzards.officespace.service.User;

import javawizzards.officespace.dto.User.*;
import javawizzards.officespace.entity.Role;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.repository.UserRepository;
import javawizzards.officespace.service.Role.RoleService;
import javawizzards.officespace.utility.JwtUtility;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.logging.Logger;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;
    private final RoleService roleService;
    private final JwtUtility jwtUtility;
    private final Logger logger;

    public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper, BCryptPasswordEncoder passwordEncoder, RoleService roleService, JwtUtility jwtUtility) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
        this.roleService = roleService;
        this.jwtUtility = jwtUtility;
        this.logger = Logger.getLogger(this.getClass().getName());
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
    public String loginUser(LoginUserDto userDto) {
        try{
            User user = this.userRepository.findByEmail(userDto.getEmail()).orElse(null);

            if (user == null) {
                throw new UserCustomException.UserNotFoundException();
            }

            if (!passwordEncoder.matches(userDto.getPassword(), user.getPassword())) {
                throw new UserCustomException.PasswordMismatchException();
            }

            return this.jwtUtility.generateNormalUserToken(user);
        }
        catch (Exception e){
            throw e;
        }
    }

    @Override
    public String loginGoogleUser(LoginGoogleUserDto userDto) {
        try{
            User user = this.userRepository.findByGoogleId(userDto.getGoogleId()).orElse(null);

            if (user == null) {
                throw new UserCustomException.GoogleUserNotFoundException();
            }

            return this.jwtUtility.generateGoogleUserToken(user);
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
}
