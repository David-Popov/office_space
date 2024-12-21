package javawizzards.officespace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.enumerations.Reservation.ReservationStatus;
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

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    private String reservationTitle;

    @Column(name = "start_date_time", nullable = false)
    private LocalDateTime startDateTime;

    @Column(name = "end_date_time", nullable = false)
    private LocalDateTime endDateTime;

    @Column(name = "duration", nullable = false)
    private int durationAsHours;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "office_room_uuid", nullable = false)
    private OfficeRoom officeRoom;  

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "event_id", unique = true)
    private Event event;

    public void setEvent(Event event) {
        if (event == null) {
            if (this.event != null) {
                this.event.setReservation(null);
            }
        } else {
            event.setReservation(this);
        }
        this.event = event;
    }
}
