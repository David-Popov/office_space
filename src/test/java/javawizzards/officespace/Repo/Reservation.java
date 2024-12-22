package javawizzards.officespace.Repo;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false)
    private UUID officeRoomId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private LocalDateTime startDateTime;

    @Column(nullable = false)
    private LocalDateTime endDateTime;

    // Default constructor
    public Reservation() {
    }

    // Constructor with all fields
    public Reservation(UUID id, UUID officeRoomId, UUID userId, LocalDateTime startDateTime, LocalDateTime endDateTime) {
        this.id = id;
        this.officeRoomId = officeRoomId;
        this.userId = userId;
        this.startDateTime = startDateTime;
        this.endDateTime = endDateTime;
    }

    // Getters and Setters
    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getOfficeRoomId() {
        return officeRoomId;
    }

    public void setOfficeRoomId(UUID officeRoomId) {
        this.officeRoomId = officeRoomId;
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }

    public LocalDateTime getStartDateTime() {
        return startDateTime;
    }

    public void setStartDateTime(LocalDateTime startDateTime) {
        this.startDateTime = startDateTime;
    }

    public LocalDateTime getEndDateTime() {
        return endDateTime;
    }

    public void setEndDateTime(LocalDateTime endDateTime) {
        this.endDateTime = endDateTime;
    }
}
