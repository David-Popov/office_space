package javawizzards.officespace.entity;

import java.util.UUID;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import javawizzards.officespace.enumerations.Resource.ResourceStatus;
import javawizzards.officespace.enumerations.Resource.ResourceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotNull
    @Column(nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceType type;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false)
    private int quantity;

    @Column(length = 1000)
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResourceStatus status = ResourceStatus.AVAILABLE;

    @Column(nullable = true)
    private String maintenanceNotes;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "office_room_uuid", nullable = false)
    private OfficeRoom officeRoom;
}