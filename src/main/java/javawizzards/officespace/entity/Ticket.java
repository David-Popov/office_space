package javawizzards.officespace.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import javawizzards.officespace.enumerations.Ticket.TicketStatus;
import javawizzards.officespace.enumerations.Ticket.TicketType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "user_uuid", nullable = false)
    private User user;

    @NotNull
    private String ticketTitle;

    @NotNull
    @Column(columnDefinition = "TEXT")
    private String ticketDesc;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus ticketStatus = TicketStatus.NEW;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketType ticketType;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "department_uuid", nullable = false)
    private Department department;

    @ManyToOne
    @JoinColumn(name = "office_room_uuid", nullable = false)
    private OfficeRoom officeRoom;

    @NotNull
    private LocalDateTime ticketDate = LocalDateTime.now();

    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
