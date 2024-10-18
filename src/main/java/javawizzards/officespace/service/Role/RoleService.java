package javawizzards.officespace.service.Role;

import javawizzards.officespace.entity.Role;

public interface RoleService {
    Role findRoleByName(String roleName);
    Role findRoleById(int roleId);
    Role CreateRole(Role role);
    Role UpdateRole(Role role);
    Role DeleteRole(String roleName);
}
