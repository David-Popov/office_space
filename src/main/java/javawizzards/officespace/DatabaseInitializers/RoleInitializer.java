package javawizzards.officespace.DatabaseInitializers;

import javawizzards.officespace.entity.Role;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.service.Role.RoleService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RoleInitializer implements CommandLineRunner {
    private final RoleService roleService;

    public RoleInitializer(RoleService roleService) {
        this.roleService = roleService;
    }

    @Override
    public void run(String... args) throws Exception {
        createRoleIfNotExists(RoleEnum.USER);
        createRoleIfNotExists(RoleEnum.ADMIN);
        createRoleIfNotExists(RoleEnum.MANAGER);
    }

    private void createRoleIfNotExists(RoleEnum roleEnum) {
        Role role = roleService.findRoleByName(roleEnum.getRoleName());

        if (role == null) {
            Role newRole = new Role();
            newRole.setName(roleEnum.getRoleName());
            this.roleService.CreateRole(newRole);
            System.out.println("Inserted role: " + roleEnum.getRoleName());
        }
    }
}
