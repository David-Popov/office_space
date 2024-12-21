package javawizzards.officespace.repository;

import javawizzards.officespace.entity.Resource;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ResourceRepository extends JpaRepository<Resource, UUID> {
    List<Resource> findByOfficeRoomId(UUID officeRoomId);
    Optional<Resource> findByType(String type);
    boolean existsByNameAndTypeAndOfficeRoomId(String name, String type, UUID officeRoomId);
}
