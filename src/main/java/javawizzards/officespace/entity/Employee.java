package javawizzards.officespace.entity;

import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(name = "email", nullable = false)
    private String email;

    @ManyToOne
    @JoinColumn(nullable = false, name = "user_uuid")
    private User user;

    @Column(nullable = false)
    private String position;

    @ManyToOne
    @JoinColumn(nullable = false, name = "company_uuid")
    private Company company;

    @ManyToOne
    @JoinColumn(nullable = false, name = "department_uuid")
    private Department department;
}
