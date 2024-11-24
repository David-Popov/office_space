package javawizzards.officespace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    private String reservationTitle;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    private String status;

    @ManyToOne
    @JoinColumn(name = "office_room_uuid", nullable = false)
    private OfficeRoom officeRoom;  

    @ManyToMany
    @JoinTable(
        name = "reservation_participants",
        joinColumns = @JoinColumn(name = "reservation_uuid"),
        inverseJoinColumns = @JoinColumn(name = "user_uuid")
    )
    private List<User> participants = new ArrayList<>();
}