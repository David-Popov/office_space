package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository extends JpaRepository<Employee, UUID>{
    List<Employee> findByDepartmentName(String departmentName);
    List<Employee> findByCompanyName(String companyName);
    Optional<Employee> findByPosition(String position);
    Optional<Employee> findByEmail(String email);
    Optional<Employee> findByName(String name);
}
