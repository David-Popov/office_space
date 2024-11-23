package javawizzards.officespace.repository;

import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.nimbusds.jose.util.Resource;

import java.util.Optional;
import java.util.UUID;
import java.time.LocalDateTime;
import java.util.List;

public interface OfficeRoomRepository extends JpaRepository<OfficeRoom, UUID> {
    Optional<OfficeRoom> findById(UUID id);
    List<OfficeRoom> findByStatus(String status);
    Optional<OfficeRoom> findByName(String name);
    List<OfficeRoom> findByCompanyName(String companyName);
    List<OfficeRoom> findByFloor(String floor);
    List<OfficeRoom> findByType(String type);
    List<OfficeRoom> findByCapacity(int capacity);
    List<OfficeRoom> findByResources(List<Resource> resources);

    @Query("SELECT o FROM OfficeRoom o WHERE " +
            "(:name IS NULL OR o.name = :name) OR " +
            "(:building IS NULL OR o.building = :building) OR " +
            "(:floor IS NULL OR o.floor = :floor) OR " +
            "(:type IS NULL OR o.type = :type) OR " +
            "(:capacity IS NULL OR o.capacity >= :capacity)")
    List<OfficeRoom> filterByCriteria(
            String name,
            String building,
            String floor,
            String type,
            Integer capacity);

    @Query("SELECT o FROM OfficeRoom o WHERE NOT EXISTS (" +
            "SELECT r FROM Reservation r WHERE r.officeRoom.id = o.id " +
            "AND (r.startDateTime < :endDateTime AND r.endDateTime > :startDateTime))")
    List<OfficeRoom> findAvailableRooms(
            LocalDateTime startDateTime, 
            LocalDateTime endDateTime);

}
