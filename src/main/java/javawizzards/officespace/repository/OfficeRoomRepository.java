package javawizzards.officespace.repository;

import javawizzards.officespace.entity.OfficeRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.nimbusds.jose.util.Resource;

import java.util.Optional;
import java.util.UUID;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OfficeRoomRepository extends JpaRepository<OfficeRoom, UUID> {
    List<OfficeRoom> findByStatus(String status);
    Optional<OfficeRoom> findByName(String name);
    List<OfficeRoom> findByCompanyName(String companyName);
    List<OfficeRoom> findByFloor(String floor);
    List<OfficeRoom> findByType(String type);
    List<OfficeRoom> findByCapacity(int capacity);
    List<OfficeRoom> findByResources(List<Resource> resources);

@Query("SELECT o FROM OfficeRoom o WHERE " +
    "(:name IS NULL OR o.name LIKE %:name%) AND " +
    "(:building IS NULL OR o.building = :building) AND " +
    "(:floor IS NULL OR o.floor = :floor) AND " +
    "(:type IS NULL OR o.type = :type) AND " +
    "(:capacity IS NULL OR o.capacity >= :capacity) AND " +
    "(:minPrice IS NULL OR o.pricePerHour >= :minPrice) AND " +
    "(:maxPrice IS NULL OR o.pricePerHour <= :maxPrice)")
List<OfficeRoom> filterByCriteriaWithPriceRange(
    String name,
    String building,
    String floor,
    String type,
    Integer capacity,
    BigDecimal minPrice,
    BigDecimal maxPrice);
    @Query("SELECT o FROM OfficeRoom o WHERE NOT EXISTS (" +
            "SELECT r FROM Reservation r WHERE r.officeRoom.id = o.id " +
            "AND (r.startDateTime < :endDateTime AND r.endDateTime > :startDateTime))")
    List<OfficeRoom> findAvailableRooms(
            LocalDateTime startDateTime, 
            LocalDateTime endDateTime);
}
