package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    List<Department> findByCompanyName(String companyName);
    Optional<Department> findByName(String departmentName);
}