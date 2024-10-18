package javawizzards.officespace.service.Role;

import javawizzards.officespace.entity.Role;
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
        try{
            Role role = this.roleRepository.findByName(roleName).orElse(null);

            if (role == null) {
                return null;
            }

            return role;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Role findRoleById(int roleId) {
        try{
            Role role = this.roleRepository.findById(roleId).orElse(null);

            if (role == null) {
                throw new RuntimeException("Role not found");
            }

            return role;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Role CreateRole(Role role) {
        try{
            Role existingRole = roleRepository.findByName(role.getName()).orElse(null);

            if (existingRole != null) {
                throw new RuntimeException("Role with name " + role.getName() + " already exists");
            }

            return roleRepository.save(role);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Role UpdateRole(Role role) {
        try{
            Role existingRole = roleRepository.findById(role.getId()).orElse(null);

            if (existingRole == null) {
                throw new RuntimeException("Role with ID " + role.getId() + " not found");
            }

            existingRole.setName(role.getName());

            return roleRepository.save(existingRole);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Role DeleteRole(String roleName) {
        try{
            Role role = roleRepository.findByName(roleName).orElse(null);

            if (role == null) {
                throw new RuntimeException("Role with name " + roleName + " not found");
            }

            roleRepository.delete(role);
            return role;
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
