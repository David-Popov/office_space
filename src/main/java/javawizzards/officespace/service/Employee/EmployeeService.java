package javawizzards.officespace.service.Employee;

import javawizzards.officespace.dto.Employee.EmployeeDto;

import java.util.List;
import java.util.UUID;

public interface EmployeeService {
    EmployeeDto createEmployee(EmployeeDto employeeDto);
    EmployeeDto updateEmployee(UUID employeeId, EmployeeDto employeeDto);
    void deleteEmployeeById(UUID employeeId);
    List<EmployeeDto> findEmployeesByCompanyName(String companyName);
    List<EmployeeDto> findEmployeesByDepartmentName(String departmentName);
}