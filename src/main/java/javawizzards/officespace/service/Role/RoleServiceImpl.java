package javawizzards.officespace.service.Role;

import javawizzards.officespace.entity.Role;
import javawizzards.officespace.exception.User.RoleCustomException;
import javawizzards.officespace.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
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
}
