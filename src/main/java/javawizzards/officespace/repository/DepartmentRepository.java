package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Department;
import javawizzards.officespace.enumerations.Department.DepartmentType;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DepartmentRepository extends JpaRepository<Department, UUID> {
    List<Department> findByCompanyName(String companyName);
    Optional<Department> findByName(String departmentName);
    Optional<Department> findByDepartmentType(DepartmentType type);
    Optional<Department> findByDepartmentTypeAndCompany_Id(DepartmentType departmentType, UUID companyId);
}