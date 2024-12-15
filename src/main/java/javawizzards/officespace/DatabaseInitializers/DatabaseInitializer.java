package javawizzards.officespace.DatabaseInitializers;

import javawizzards.officespace.entity.*;
import javawizzards.officespace.enumerations.OfficeRoom.RoomStatus;
import javawizzards.officespace.enumerations.OfficeRoom.RoomType;
import javawizzards.officespace.enumerations.Resource.ResourceStatus;
import javawizzards.officespace.enumerations.Resource.ResourceType;
import javawizzards.officespace.enumerations.User.RoleEnum;
import javawizzards.officespace.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class DatabaseInitializer implements CommandLineRunner {

    private final CompanyRepository companyRepository;
    private final DepartmentRepository departmentRepository;
    private final OfficeRoomRepository officeRoomRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ResourceRepository resourceRepository;

    @Override
    public void run(String... args) {
        List<Role> roles;
        if (roleRepository.count() == 0) {
            roles = initializeRoles();
            System.out.println("Roles have been initialized");
        } else {
            roles = roleRepository.findAll();
        }

        Map<String, Role> roleMap = roles.stream()
                .collect(Collectors.toMap(Role::getName, role -> role));

        if (userRepository.count() == 0) {
            initializeUsers(roleMap);
            System.out.println("Users have been initialized");
        }

        if (companyRepository.count() == 0) {
            List<Company> companies = Arrays.asList(
                    createCompany("Tech Solutions Inc.", "123 Innovation Drive", "Technology"),
                    createCompany("Finance Pro Ltd.", "456 Wall Street", "Finance"),
                    createCompany("Green Energy Co.", "789 Sustainable Way", "Energy"),
                    createCompany("Healthcare Plus", "321 Medical Center Blvd", "Healthcare"),
                    createCompany("Creative Studios", "654 Artist Avenue", "Media")
            );

            companies = companyRepository.saveAll(companies);

            System.out.println("Companies have been initialized");

            for (Company company : companies) {
                List<Department> departments = createDepartmentsForCompany(company);
                departmentRepository.saveAll(departments);

                System.out.println("Departments have been initialized");

                List<OfficeRoom> officeRooms = createOfficeRoomsForCompany(company);
                officeRoomRepository.saveAll(officeRooms);

                System.out.println("Office Rooms have been initialized");
            }

            if (resourceRepository.count() == 0) {
                List<OfficeRoom> allRooms = officeRoomRepository.findAll();
                for (OfficeRoom room : allRooms) {
                    List<Resource> resources = createResourcesForRoom(room);
                    resourceRepository.saveAll(resources);
                }
                System.out.println("Resources have been initialized");
            }

            System.out.println("Database has been initialized with sample data");
        }
    }

    private List<Role> initializeRoles() {
        List<Role> roles = new ArrayList<>();

        for (RoleEnum roleEnum : RoleEnum.values()) {
            Role role = new Role();
            role.setName(roleEnum.getRoleName());
            role.setUsers(new ArrayList<>());
            roles.add(role);
        }

        return roleRepository.saveAll(roles);
    }

    private void initializeUsers(Map<String, Role> roleMap) {
        User adminUser = new User();
        adminUser.setEmail("admin@gmail.com");
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("admin123"));
        adminUser.setRole(roleMap.get("ADMIN"));
        adminUser.setFirstName("Admin");
        adminUser.setLastName("User");
        adminUser.setPhone("+1234567890");
        adminUser.setAddress("123 Admin Street");
        adminUser.setPictureUrl("https://example.com/profiles/admin.jpg");
        adminUser.setReservations(new ArrayList<>());
        adminUser.setPayments(new ArrayList<>());

        User regularUser = new User();
        regularUser.setEmail("user@gmail.com");
        regularUser.setUsername("user");
        regularUser.setPassword(passwordEncoder.encode("user123"));
        regularUser.setRole(roleMap.get("USER"));
        regularUser.setFirstName("Regular");
        regularUser.setLastName("User");
        regularUser.setPhone("+1987654321");
        regularUser.setAddress("456 User Avenue");
        regularUser.setPictureUrl("https://example.com/profiles/user.jpg");
        regularUser.setReservations(new ArrayList<>());
        regularUser.setPayments(new ArrayList<>());

        userRepository.saveAll(Arrays.asList(adminUser, regularUser));
        System.out.println("Users initialized: admin@officespace.com and user@officespace.com");
    }

    private Company createCompany(String name, String address, String type) {
        Company company = new Company();
        company.setName(name);
        company.setAddress(address);
        company.setType(type);
        company.setOfficeRooms(new ArrayList<>());
        company.setEmployees(new ArrayList<>());
        company.setDepartments(new ArrayList<>());
        return company;
    }

    private List<Department> createDepartmentsForCompany(Company company) {
        List<String> departmentNames = Arrays.asList(
                "Human Resources",
                "Finance",
                "Marketing",
                "Operations",
                "IT",
                "Sales",
                "Research & Development"
        );

        List<Department> departments = new ArrayList<>();

        int numDepartments = 3 + (int)(Math.random() * 3);
        for (int i = 0; i < numDepartments; i++) {
            Department department = new Department();
            department.setName(departmentNames.get(i));
            department.setCompany(company);
            department.setEmployees(new ArrayList<>());
            departments.add(department);
        }

        return departments;
    }

    private List<OfficeRoom> createOfficeRoomsForCompany(Company company) {
        List<OfficeRoom> officeRooms = new ArrayList<>();

        createRoom(officeRooms, company, "Main Conference Room", RoomType.CONFERENCE_ROOM, 20, "1st", new BigDecimal("100.00"));
        createRoom(officeRooms, company, "Small Meeting Room A", RoomType.MEETING_ROOM, 6, "2nd", new BigDecimal("50.00"));
        createRoom(officeRooms, company, "Small Meeting Room B", RoomType.MEETING_ROOM, 6, "2nd", new BigDecimal("50.00"));
        createRoom(officeRooms, company, "Open Workspace", RoomType.OFFICE, 30, "3rd", new BigDecimal("150.00"));
        createRoom(officeRooms, company, "Private Office 1", RoomType.MEETING_ROOM, 1, "4th", new BigDecimal("75.00"));
        createRoom(officeRooms, company, "Private Office 2", RoomType.TRAINING_ROOM, 1, "4th", new BigDecimal("75.00"));

        return officeRooms;
    }

    private void createRoom(List<OfficeRoom> rooms, Company company, String name, RoomType type,
                            int capacity, String floor, BigDecimal pricePerHour) {
        OfficeRoom room = new OfficeRoom();
        room.setName(name);
        room.setAddress(company.getAddress());
        room.setBuilding(company.getName() + " Building");
        room.setFloor(floor);
        room.setType(type);
        room.setCapacity(capacity);
        room.setStatus(RoomStatus.AVAILABLE);
        room.setPictureUrl("https://example.com/rooms/" + name.toLowerCase().replace(" ", "-") + ".jpg");
        room.setPricePerHour(pricePerHour);
        room.setCompany(company);
        room.setReservations(new ArrayList<>());
        room.setResources(new ArrayList<>());

        rooms.add(room);
    }

    private List<Resource> createResourcesForRoom(OfficeRoom room) {
        List<Resource> resources = new ArrayList<>();

        switch (room.getType()) {
            case CONFERENCE_ROOM:
                addConferenceRoomResources(resources, room);
                break;
            case MEETING_ROOM:
                addMeetingRoomResources(resources, room);
                break;
            case OFFICE:
                addOfficeResources(resources, room);
                break;
            case TRAINING_ROOM:
                addTrainingRoomResources(resources, room);
                break;
        }

        return resources;
    }

    private void addConferenceRoomResources(List<Resource> resources, OfficeRoom room) {
        resources.add(createResource("Conference Table", ResourceType.TABLE, 1, room,
                "Large conference table suitable for meetings"));
        resources.add(createResource("Conference Chairs", ResourceType.CHAIR, room.getCapacity(), room,
                "Comfortable chairs for conference participants"));
        resources.add(createResource("Projector System", ResourceType.PROJECTOR, 1, room,
                "High-definition projector with screen"));
        resources.add(createResource("Video Conference System", ResourceType.VIDEO_CONFERENCE_SYSTEM, 1, room,
                "Professional video conferencing equipment"));
        resources.add(createResource("Whiteboard", ResourceType.WHITEBOARD, 2, room,
                "Large whiteboard for presentations"));
        resources.add(createResource("Power Strips", ResourceType.POWER_STRIP, 4, room,
                "Power strips for laptops and devices"));
    }

    private void addMeetingRoomResources(List<Resource> resources, OfficeRoom room) {
        resources.add(createResource("Meeting Table", ResourceType.TABLE, 1, room,
                "Medium-sized meeting table"));
        resources.add(createResource("Meeting Chairs", ResourceType.CHAIR, room.getCapacity(), room,
                "Comfortable chairs for meeting participants"));
        resources.add(createResource("Monitor", ResourceType.MONITOR, 1, room,
                "Large monitor for presentations"));
        resources.add(createResource("Whiteboard", ResourceType.WHITEBOARD, 1, room,
                "Whiteboard for brainstorming"));
        resources.add(createResource("Power Strip", ResourceType.POWER_STRIP, 2, room,
                "Power strips for devices"));
    }

    private void addOfficeResources(List<Resource> resources, OfficeRoom room) {
        resources.add(createResource("Work Desks", ResourceType.TABLE, room.getCapacity(), room,
                "Individual work desks"));
        resources.add(createResource("Office Chairs", ResourceType.CHAIR, room.getCapacity(), room,
                "Ergonomic office chairs"));
        resources.add(createResource("Monitors", ResourceType.MONITOR, room.getCapacity(), room,
                "Individual monitors for workstations"));
        resources.add(createResource("Power Strips", ResourceType.POWER_STRIP, room.getCapacity() / 2, room,
                "Power strips for workstations"));
    }

    private void addTrainingRoomResources(List<Resource> resources, OfficeRoom room) {
        resources.add(createResource("Training Tables", ResourceType.TABLE, room.getCapacity() / 2, room,
                "Tables for training sessions"));
        resources.add(createResource("Training Chairs", ResourceType.CHAIR, room.getCapacity(), room,
                "Chairs for training participants"));
        resources.add(createResource("Projector", ResourceType.PROJECTOR, 1, room,
                "Training room projector"));
        resources.add(createResource("Whiteboard", ResourceType.WHITEBOARD, 2, room,
                "Whiteboards for training sessions"));
        resources.add(createResource("Training Laptops", ResourceType.LAPTOP, room.getCapacity(), room,
                "Laptops for training purposes"));
        resources.add(createResource("Power Strips", ResourceType.POWER_STRIP, room.getCapacity() / 2, room,
                "Power strips for laptops"));
    }

    private Resource createResource(String name, ResourceType type, int quantity, OfficeRoom room, String description) {
        Resource resource = new Resource();
        resource.setName(name);
        resource.setType(type);
        resource.setQuantity(quantity);
        resource.setStatus(ResourceStatus.AVAILABLE);
        resource.setDescription(description);
        resource.setOfficeRoom(room);
        return resource;
    }
}