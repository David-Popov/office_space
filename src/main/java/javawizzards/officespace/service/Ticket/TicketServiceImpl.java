package javawizzards.officespace.service.Ticket;

import javawizzards.officespace.dto.Ticket.TicketDto;
import javawizzards.officespace.entity.Department;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.entity.Ticket;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.Department.DepartmentType;
import javawizzards.officespace.enumerations.Notification.NotificationType;
import javawizzards.officespace.enumerations.Ticket.TicketType;
import javawizzards.officespace.enumerations.Ticket.TicketStatus;
import javawizzards.officespace.exception.Department.DepartmentCustomException;
import javawizzards.officespace.exception.Ticket.TicketCustomException;
import javawizzards.officespace.repository.DepartmentRepository;
import javawizzards.officespace.repository.TicketRepository;
import javawizzards.officespace.repository.OfficeRoomRepository;
import javawizzards.officespace.repository.UserRepository;
import javawizzards.officespace.exception.OfficeRoom.OfficeRoomCustomException;

import javawizzards.officespace.service.Notification.NotificationService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;
    private final ModelMapper modelMapper;
    private final OfficeRoomRepository officeRoomRepository;

    @Autowired
    public TicketServiceImpl(TicketRepository ticketRepository,
                             DepartmentRepository departmentRepository,
                             UserRepository userRepository, NotificationService notificationService,
                             ModelMapper modelMapper,
                             OfficeRoomRepository officeRoomRepository) {
        this.ticketRepository = ticketRepository;
        this.departmentRepository = departmentRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
        this.modelMapper = modelMapper;
        this.officeRoomRepository = officeRoomRepository;

        configureModelMapper(modelMapper);
    }

    private void configureModelMapper(ModelMapper modelMapper) {
        modelMapper.addMappings(new org.modelmapper.PropertyMap<Ticket, TicketDto>() {
            @Override
            protected void configure() {
                map(source.getUser().getId(), destination.getUserId());
                map(source.getOfficeRoom().getId(), destination.getOfficeRoomId());
            }
        });
    }

    @Override
    public TicketDto createTicket(TicketDto ticketDto) {
        if (ticketDto == null) {
            throw new TicketCustomException.TicketNotFoundException();
        }

        if (ticketDto.getTicketDate() == null || ticketDto.getTicketDesc() == null) {
            throw new TicketCustomException.TicketDataInvalidException();
        }

        User user = userRepository.findById(ticketDto.getUserId())
                .orElseThrow(() -> new TicketCustomException.TicketUserNotFoundException());

        OfficeRoom officeRoom = officeRoomRepository.findById(ticketDto.getOfficeRoomId())
                .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());

        UUID departmentId = findDepartmentByTicketTypeAndCompanyId(
                ticketDto.getTicketType(),
                officeRoom.getCompany().getId()
        );

        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new DepartmentCustomException.DepartmentNotFoundException());

        Ticket ticket = new Ticket();
        ticket.setDepartment(department);
        ticket.setOfficeRoom(officeRoom);
        ticket.setTicketType(ticketDto.getTicketType());
        ticket.setTicketTitle(ticketDto.getTicketTitle());
        ticket.setTicketDesc(ticketDto.getTicketDesc());
        ticket.setTicketDate(ticketDto.getTicketDate());
        ticket.setTicketStatus(TicketStatus.NEW);
        ticket.setUser(user);

        List<UUID> usersIdsToSendTicket = department.getUsers().stream().map(User::getId).collect(Collectors.toList());
        usersIdsToSendTicket.add(user.getId());

        Ticket savedTicket = ticketRepository.save(ticket);

        notificationService.sendSystemNotification(
                "Ticket was submitted to department " + department.getName(),
                NotificationType.TICKET_CREATED,
                usersIdsToSendTicket
        );

        return mapToDto(savedTicket);
    }

    public UUID findDepartmentByTicketTypeAndCompanyId(TicketType ticketType, UUID companyId) {

        DepartmentType departmentType = mapTicketTypeToDepartment(ticketType);
        System.out.println("Department Type: " + departmentType); 
        System.out.println("Company ID: " + companyId);
    
        Department department = departmentRepository.findByDepartmentTypeAndCompanyId(departmentType, companyId)
                .orElseThrow(() -> new DepartmentCustomException.DepartmentNotFoundException());
    
        return department.getId(); 
    }

    public UUID findDepartmentByTicketType(TicketType type) {
        DepartmentType departmentType = switch (type) {
            case IT -> DepartmentType.IT_SUPPORT;
            case MAINTENANCE -> DepartmentType.MAINTENANCE;
            case CLEANING -> DepartmentType.CLEANING;
            case OTHER -> DepartmentType.GENERAL;
        };

        Department department = departmentRepository.findByDepartmentTypeAndCompany_Id(departmentType, 
                UUID.fromString("c4939c9c-1415-4f8b-b345-6f8566228724"))
                .orElseThrow(() -> new RuntimeException("Department not found for type: " + departmentType));
        return department.getId();
    }

    @Override
    public Ticket changeTicketStatus(UUID id, TicketStatus newStatus) {
        try{
            Ticket ticket = ticketRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));

            ticket.setTicketStatus(newStatus);

            Ticket savedTicket = ticketRepository.save(ticket);

            if (savedTicket == null && savedTicket.getUser() == null) {
                throw new TicketCustomException.TicketStatusChangeFailure();
            }

            notificationService.sendSystemNotification(
                    "Ticket status updated to: " + newStatus,
                    NotificationType.TICKET_STATUS_CHANGED,
                    List.of(savedTicket.getUser().getId())
            );

            return savedTicket;
        } catch (TicketCustomException e) {
            throw e;
        } catch (Exception e) {
           throw new RuntimeException(e);
        }
    }

    @Override
    public Ticket changeTicketType(UUID id, TicketType newType) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found for id: " + id));

        ticket.setTicketType(newType);
        return ticketRepository.save(ticket);
    }

    @Override
    public void deleteTicket(UUID id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new TicketCustomException.TicketNotFoundException());
        ticketRepository.delete(ticket);
    }

    @Override
    public List<TicketDto> getAllTicketsOfUser(UUID userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found for id: " + userId));

        List<Ticket> tickets = ticketRepository.findAllByUserId(userId);
        return tickets.stream()
                .map(this::mapToDto)
                .toList();
    }

    private DepartmentType mapTicketTypeToDepartment(TicketType ticketType) {
        switch (ticketType) {
            case IT:
                return DepartmentType.IT_SUPPORT;
            case MAINTENANCE:
                return DepartmentType.MAINTENANCE;
            case CLEANING:
                return DepartmentType.CLEANING;
            default:
                return DepartmentType.GENERAL;
        }
    }

    private TicketDto mapToDto(Ticket ticket) {
        if (ticket == null) {
            return null; 
        }
        return modelMapper.map(ticket, TicketDto.class);
    }
}
