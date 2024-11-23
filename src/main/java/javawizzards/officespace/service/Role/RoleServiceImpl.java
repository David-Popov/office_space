package javawizzards.officespace.service.Role;

import javawizzards.officespace.dto.Role.RoleDto;
import javawizzards.officespace.dto.User.UserDto;
import javawizzards.officespace.entity.Role;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.exception.User.RoleCustomException;
import javawizzards.officespace.repository.RoleRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final ModelMapper modelMapper;

    public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
        this.roleRepository = roleRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<RoleDto> getRoles() {
        try{
            return MapToListOfRoleDto(this.roleRepository.findAll());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public Role findRoleByName(String roleName) {
        Role role = this.roleRepository.findByName(roleName).orElse(null);
        if (role == null) {
            throw new RoleCustomException.RoleNotFoundException();
        }
        return role;
    }

    @Override
    public Role findRoleById(int roleId) {
        Role role = this.roleRepository.findById(roleId).orElse(null);
        if (role == null) {
            throw new RoleCustomException.RoleIdNotFoundException();
        }
        return role;
    }

    @Override
    public Role CreateRole(Role role) {
        Role existingRole = roleRepository.findByName(role.getName()).orElse(null);
        if (existingRole != null) {
            throw new RoleCustomException.RoleAlreadyExistsException();
        }
        return roleRepository.save(role);
    }

    @Override
    public Role UpdateRole(Role role) {
        Role existingRole = roleRepository.findById(role.getId()).orElse(null);
        if (existingRole == null) {
            throw new RoleCustomException.RoleIdNotFoundException();
        }

        existingRole.setName(role.getName());
        return roleRepository.save(existingRole);
    }

    @Override
    public Role DeleteRole(String roleName) {
        Role role = roleRepository.findByName(roleName).orElse(null);
        if (role == null) {
            throw new RoleCustomException.RoleNotFoundException();
        }

        roleRepository.delete(role);
        return role;
    }

    private List<RoleDto> MapToListOfRoleDto(List<Role> roles) {
        try{
            Type listType = new TypeToken<List<RoleDto>>() {}.getType();
            return this.modelMapper.map(roles, listType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
