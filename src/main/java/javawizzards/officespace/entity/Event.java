package javawizzards.officespace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(name = "meeting_title", nullable = false)
    private String meetingTitle;

    @Column(name = "description", length = 1000)
    private String description;

    @ElementCollection
    @CollectionTable(
            name = "event_attendees",
            joinColumns = @JoinColumn(name = "event_id")
    )
    @Column(name = "email")
    private List<String> attendees;

    @NotNull
    @Email
    @Column(name = "contact_email", nullable = false)
    private String contactEmail;

    @NotNull
    @Column(name = "department", nullable = false)
    private String department;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reservation_uuid", unique = true)
    private Reservation reservation;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }
}
