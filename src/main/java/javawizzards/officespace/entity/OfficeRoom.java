package javawizzards.officespace.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(nullable = true)
    private String type;

    @Column(nullable = false)
    private int capacity;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private String pictureUrl;

    @ManyToOne
    @JoinColumn(nullable = false, name = "company_uuid")
    private Company company;

    @OneToMany(mappedBy = "officeRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    @OneToMany(mappedBy = "officeRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resource> resources = new ArrayList<>();
}