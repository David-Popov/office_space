package javawizzards.officespace.service.Employee;

import javawizzards.officespace.dto.Employee.EmployeeDto;
import javawizzards.officespace.entity.Company;
import javawizzards.officespace.entity.Department;
import javawizzards.officespace.entity.Employee;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.exception.Company.CompanyCustomException;
import javawizzards.officespace.exception.Department.DepartmentCustomException;
import javawizzards.officespace.exception.User.UserCustomException;
import javawizzards.officespace.exception.Employee.EmployeeCustomException;
import javawizzards.officespace.repository.CompanyRepository;
import javawizzards.officespace.repository.DepartmentRepository;
import javawizzards.officespace.repository.EmployeeRepository;
import javawizzards.officespace.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository, CompanyRepository companyRepository,
                               DepartmentRepository departmentRepository, UserRepository userRepository, ModelMapper modelMapper) {
        this.employeeRepository = employeeRepository;
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.logger = Logger.getLogger(EmployeeServiceImpl.class.getName());
    }

    @Override
    public EmployeeDto createEmployee(EmployeeDto employeeDto) {
        try {
            User user = userRepository.findByEmail(employeeDto.getUserEmail())
                    .orElseThrow(() -> new UserCustomException.UserNotFoundException());
            Company company = companyRepository.findByName(employeeDto.getCompanyName())
                    .orElseThrow(() -> new CompanyCustomException.CompanyNotFoundException());
            Department department = departmentRepository.findByName(employeeDto.getDepartmentName())
                    .orElseThrow(() -> new DepartmentCustomException.DepartmentNotFoundException());

            Employee employee = new Employee();
            employee.setUser(user);
            employee.setCompany(company);
            employee.setDepartment(department);
            employee.setPosition(employeeDto.getPosition());

            Employee savedEmployee = employeeRepository.save(employee);
            return mapToDto(savedEmployee);
        } catch (Exception e) {
            logger.severe("Error occurred while creating employee: " + e.getMessage());
            throw e;
        }
    }

    // edit
    @Override
    public EmployeeDto updateEmployee(UUID employeeId, EmployeeDto employeeDto) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(EmployeeCustomException.EmployeeNotFoundException::new);

            User user = userRepository.findByEmail(employeeDto.getUserEmail())
                    .orElseThrow(() -> new UserCustomException.UserNotFoundException());
            Company company = companyRepository.findByName(employeeDto.getCompanyName())
                    .orElseThrow(() -> new CompanyCustomException.CompanyNotFoundException());
            Department department = departmentRepository.findByName(employeeDto.getDepartmentName())
                    .orElseThrow(() -> new DepartmentCustomException.DepartmentNotFoundException());

            employee.setUser(user);
            employee.setCompany(company);
            employee.setDepartment(department);
            employee.setPosition(employeeDto.getPosition());

            Employee updatedEmployee = employeeRepository.save(employee);
            return mapToDto(updatedEmployee);
        } catch (Exception e) {
            logger.severe("Error occurred while updating employee: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public void deleteEmployeeById(UUID employeeId) {
        try {
            Employee employee = employeeRepository.findById(employeeId)
                    .orElseThrow(EmployeeCustomException.EmployeeNotFoundException::new);
            employeeRepository.delete(employee);
        } catch (Exception e) {
            logger.severe("Error occurred while deleting employee: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<EmployeeDto> findEmployeesByCompanyName(String companyName) {
        try {
            List<Employee> employees = employeeRepository.findByCompanyName(companyName);
            return employees.isEmpty() ? new ArrayList<>() : employees.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching employees by company name: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<EmployeeDto> findEmployeesByDepartmentName(String departmentName) {
        try {
            List<Employee> employees = employeeRepository.findByDepartmentName(departmentName);
            return employees.isEmpty() ? new ArrayList<>() : employees.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching employees by department name: " + e.getMessage());
            throw e;
        }
    }

    private EmployeeDto mapToDto(Employee employee) {
        try {
            return modelMapper.map(employee, EmployeeDto.class);
        } catch (Exception e) {
            logger.severe("Error occurred while mapping employee to DTO: " + e.getMessage());
            throw new RuntimeException("Error mapping employee to DTO", e);
        }
    }
}