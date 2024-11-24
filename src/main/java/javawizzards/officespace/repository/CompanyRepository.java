package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;

public interface CompanyRepository extends JpaRepository<Company, UUID>{
    Optional<Company> findByName(String name);
    List<Company> findByType(String type);
}
