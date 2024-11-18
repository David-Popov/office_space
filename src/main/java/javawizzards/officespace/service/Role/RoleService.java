package javawizzards.officespace.service.Role;

import javawizzards.officespace.dto.Role.RoleDto;
import javawizzards.officespace.entity.Role;

import java.util.List;

public interface RoleService {
    List<RoleDto> getRoles();
    Role findRoleByName(String roleName);
    Role findRoleById(int roleId);
    Role CreateRole(Role role);
    Role UpdateRole(Role role);
    Role DeleteRole(String roleName);
}
