package javawizzards.officespace.service.Role;

import javawizzards.officespace.dto.Role.RoleDto;
import javawizzards.officespace.entity.Role;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.exception.User.RoleCustomException;
import javawizzards.officespace.repository.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RoleServiceImplTest {

    @Mock
    private RoleRepository roleRepository;

    private ModelMapper modelMapper;

    private RoleService roleService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        modelMapper = new ModelMapper();
        roleService = new RoleServiceImpl(roleRepository, modelMapper);
    }

    @Test
    void getRoles_ReturnsListOfRoleDtos() {
        List<Role> roles = Arrays.asList(
                createTestRole(1, RoleEnum.USER.getRoleName()),
                createTestRole(2, RoleEnum.ADMIN.getRoleName()),
                createTestRole(3, RoleEnum.MANAGER.getRoleName())
        );

        when(roleRepository.findAll()).thenReturn(roles);

        List<RoleDto> result = roleService.getRoles();

        assertNotNull(result);
        assertEquals(3, result.size());
        assertEquals(RoleEnum.USER.getRoleName(), result.get(0).getName());
        assertEquals(1, result.get(0).getId());
        assertEquals(RoleEnum.ADMIN.getRoleName(), result.get(1).getName());
        assertEquals(2, result.get(1).getId());
        assertEquals(RoleEnum.MANAGER.getRoleName(), result.get(2).getName());
        assertEquals(3, result.get(2).getId());
        verify(roleRepository).findAll();
    }

    @Test
    void findRoleByName_ExistingRole_ReturnsRole() {
        String roleName = RoleEnum.ADMIN.getRoleName();
        Role expectedRole = createTestRole(2, roleName);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(expectedRole));

        Role result = roleService.findRoleByName(roleName);

        assertNotNull(result);
        assertEquals(roleName, result.getName());
        assertEquals(2, result.getId());
        verify(roleRepository).findByName(roleName);
    }

    @Test
    void findRoleById_ExistingRole_ReturnsRole() {
        int roleId = 1;
        Role expectedRole = createTestRole(roleId, RoleEnum.USER.getRoleName());
        when(roleRepository.findById(roleId)).thenReturn(Optional.of(expectedRole));

        Role result = roleService.findRoleById(roleId);

        assertNotNull(result);
        assertEquals(roleId, result.getId());
        assertEquals(RoleEnum.USER.getRoleName(), result.getName());
        verify(roleRepository).findById(roleId);
    }

    @Test
    void createRole_NewRole_Success() {
        Role newRole = createTestRole(3, RoleEnum.MANAGER.getRoleName());
        when(roleRepository.findByName(newRole.getName())).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);

        Role result = roleService.CreateRole(newRole);

        assertNotNull(result);
        assertEquals(RoleEnum.MANAGER.getRoleName(), result.getName());
        assertEquals(3, result.getId());
        verify(roleRepository).save(newRole);
    }

    @Test
    void createRole_ExistingRole_ThrowsException() {
        Role existingRole = createTestRole(1, RoleEnum.USER.getRoleName());
        when(roleRepository.findByName(existingRole.getName())).thenReturn(Optional.of(existingRole));

        assertThrows(RoleCustomException.RoleAlreadyExistsException.class,
                () -> roleService.CreateRole(existingRole));
    }

    @Test
    void updateRole_ExistingRole_Success() {
        Role roleToUpdate = createTestRole(1, RoleEnum.USER.getRoleName());
        Role existingRole = createTestRole(1, "OLD_ROLE");
        when(roleRepository.findById(roleToUpdate.getId())).thenReturn(Optional.of(existingRole));
        when(roleRepository.save(any(Role.class))).thenReturn(roleToUpdate);

        Role result = roleService.UpdateRole(roleToUpdate);

        assertNotNull(result);
        assertEquals(RoleEnum.USER.getRoleName(), result.getName());
        assertEquals(1, result.getId());
        verify(roleRepository).save(any(Role.class));
    }

    @Test
    void updateRole_NonExistingRole_ThrowsException() {
        Role nonExistingRole = createTestRole(999, "NON_EXISTING");
        when(roleRepository.findById(nonExistingRole.getId())).thenReturn(Optional.empty());

        assertThrows(RoleCustomException.RoleIdNotFoundException.class,
                () -> roleService.UpdateRole(nonExistingRole));
    }

    @Test
    void deleteRole_ExistingRole_Success() {
        String roleName = RoleEnum.USER.getRoleName();
        Role roleToDelete = createTestRole(1, roleName);
        when(roleRepository.findByName(roleName)).thenReturn(Optional.of(roleToDelete));

        Role result = roleService.DeleteRole(roleName);

        assertNotNull(result);
        assertEquals(roleName, result.getName());
        assertEquals(1, result.getId());
        verify(roleRepository).delete(roleToDelete);
    }

    private Role createTestRole(int id, String name) {
        Role role = new Role();
        role.setId(id);
        role.setName(name);
        return role;
    }
}