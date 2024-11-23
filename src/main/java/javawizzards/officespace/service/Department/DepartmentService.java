package javawizzards.officespace.service.Department;

import javawizzards.officespace.dto.Department.DepartmentDto;
import java.util.List;
import java.util.UUID;

public interface DepartmentService {
    DepartmentDto createDepartment(DepartmentDto createDepartmentDto);
    DepartmentDto updateDepartment(UUID departmentId, DepartmentDto updateDepartmentDto);
    void deleteDepartment(UUID departmentId);
    List<DepartmentDto> findDepartmentsByCompanyName(String companyName);
    DepartmentDto findDepartmentByName(String departmentName);
}