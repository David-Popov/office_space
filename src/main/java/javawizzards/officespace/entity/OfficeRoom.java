package javawizzards.officespace.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import javawizzards.officespace.enumerations.OfficeRoom.RoomStatus;
import javawizzards.officespace.enumerations.OfficeRoom.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "office_rooms")
public class OfficeRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = true)
    private String address;

    @Column(nullable = true)
    private String building;

    @Column(nullable = false)
    private String floor;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private RoomType type;

    @Column(nullable = false)
    private int capacity;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoomStatus status;

    @Column(nullable = true)
    private String pictureUrl;

    @NotNull
    @PositiveOrZero
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal pricePerHour;

    @ManyToOne
    @JoinColumn(nullable = false, name = "company_uuid")
    @JsonBackReference
    private Company company;

    @OneToMany(mappedBy = "officeRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "officeRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources = new ArrayList<>();


}