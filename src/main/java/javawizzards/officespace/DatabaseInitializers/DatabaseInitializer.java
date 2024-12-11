package javawizzards.officespace.DatabaseInitializers;

import javawizzards.officespace.entity.*;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


@Component
public class DatabaseInitializer implements CommandLineRunner {
    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final OfficeRoomRepository officeRoomRepository;
    private final ReservationRepository reservationRepository;

    public DatabaseInitializer(
            CompanyRepository companyRepository,
            DepartmentRepository departmentRepository,
            RoleRepository roleRepository,
            UserRepository userRepository,
            EmployeeRepository employeeRepository,
            OfficeRoomRepository officeRoomRepository,
            ReservationRepository reservationRepository) {
        this.companyRepository = companyRepository;
        this.departmentRepository = departmentRepository;
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.officeRoomRepository = officeRoomRepository;
        this.reservationRepository = reservationRepository;
    }

    @Override
    public void run(String... args) {
        if (roleRepository.count() > 0 || userRepository.count() > 0) {
            return;
        }

//        initializeRoles();
//        initializeEntities();
    }

    private void initializeRoles() {
        for (RoleEnum roleEnum : RoleEnum.values()) {
            Role existingRole = roleRepository.findByName(roleEnum.getRoleName()).orElse(null);
            if (existingRole == null) {
                Role newRole = new Role();
                newRole.setName(roleEnum.getRoleName());
                roleRepository.save(newRole);
                System.out.println("Inserted role: " + roleEnum.getRoleName());
            }
        }
    }

//    private void initializeEntities() {
//        try{
//            String companyName = "Автокомплекс Бранков ЕООД";
//
//            if (companyRepository.findByName(companyName).isPresent()) {
//                return;
//            }
//
//            String adminUsername = "admin";
//            if (userRepository.findByUsername(adminUsername).isPresent()) {
//                return;
//            }
//
//            String departmentName = "IT Department";
//            if (departmentRepository.findByName(departmentName).isPresent()) {
//                return;
//            }
//
//            Role adminRole = roleRepository.findByName(RoleEnum.ADMIN.getRoleName()).orElseThrow();
//            Role normalUserRole = roleRepository.findByName(RoleEnum.USER.getRoleName()).orElseThrow();
//
//            Company company = new Company();
//            company.setName("Автокомплекс Бранков ЕООД");
//            company.setAddress("ул. Примерна 1");
//            company.setType("ЕООД");
//            company = companyRepository.save(company);
//
//            Department department = new Department();
//            department.setName("IT Department");
//            department.setCompany(company);
//            department = departmentRepository.save(department);
//
//            User admin = new User();
//            admin.setEmail("admin@example.com");
//            admin.setUsername("admin");
//            admin.setPassword("admin1234");
//            admin.setRole(adminRole);
//            userRepository.save(admin);
//
//            User user = new User();
//            user.setEmail("user1@example.com");
//            user.setUsername("user1");
//            user.setPassword("user1234");
//            user.setRole(adminRole);
//            user = userRepository.save(user);
//
//            Employee employee = new Employee();
//            employee.setName("John Doe");
//            employee.setEmail(user.getEmail());
//            employee.setPosition("IT Manager");
//            employee.setUser(user);
//            employee.setCompany(company);
//            employee.setDepartment(department);
//            employeeRepository.save(employee);
//
//            OfficeRoom officeRoom = new OfficeRoom();
//            officeRoom.setName("Main Conference Room");
//            officeRoom.setCapacity(20);
//            officeRoom.setStatus(RoomStatus.AVAILABLE);
//            officeRoom.setType(RoomType.CONFERENCE_ROOM);
//            officeRoom.setCompany(company);
//            officeRoom = officeRoomRepository.save(officeRoom);
//
//            Reservation reservation = new Reservation();
////            reservation.setUserId(user.getId());
//            reservation.setReservationTitle("Team Meeting");
//            reservation.setStartDateTime(LocalDateTime.now().plusDays(1));
//            reservation.setEndDateTime(LocalDateTime.now().plusDays(1).plusHours(2));
//            reservation.setDurationAsHours(2);
//            reservation.setStatus(ReservationStatus.PENDING);
//            reservation.setOfficeRoom(officeRoom);
//            reservation.setParticipants(List.of(user));
//            reservationRepository.save(reservation);
//
//            System.out.println("Database initialized with connected entities");
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//
//    }
}