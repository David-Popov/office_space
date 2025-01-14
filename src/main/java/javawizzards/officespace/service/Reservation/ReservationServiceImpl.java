package javawizzards.officespace.service.Reservation;

import jakarta.mail.MessagingException;
import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.dto.Reservation.GetReservationsResponseObject;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.entity.Event;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.entity.Reservation;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.OfficeRoom.OfficeRoomMessages;
import javawizzards.officespace.enumerations.Reservation.ReservationStatus;
import javawizzards.officespace.exception.OfficeRoom.OfficeRoomCustomException;
import javawizzards.officespace.exception.Reservation.ReservationCustomException;
import javawizzards.officespace.repository.OfficeRoomRepository;
import javawizzards.officespace.repository.ReservationRepository;
import javawizzards.officespace.repository.UserRepository;
import javawizzards.officespace.service.Email.EmailService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;
    private final OfficeRoomRepository officeRoomRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final EmailService emailService;

    public ReservationServiceImpl(ReservationRepository reservationRepository,
                                  OfficeRoomRepository officeRoomRepository,
                                  UserRepository userRepository,
                                  ModelMapper modelMapper, EmailService emailService) {
        this.reservationRepository = reservationRepository;
        this.officeRoomRepository = officeRoomRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.emailService = emailService;
    }

    @Override
    public List<GetReservationsResponseObject> getAllReservations() {
        try {
            List<Reservation> reservations = reservationRepository.findAll();

            return reservations.stream()
                    .map(reservation -> {
                        GetReservationsResponseObject responseObject = this.modelMapper.map(reservation, GetReservationsResponseObject.class);
                        responseObject.setUserEmail(reservation.getUser().getEmail());
                        responseObject.setOfficeRoomName(reservation.getOfficeRoom().getName());
                        return responseObject;
                    })
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw e;
        }
    }

    @Override
    public ReservationDto createReservation(CreateReservationDto reservationDto) {
        try {
            if (reservationDto == null) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            if (reservationDto.getStartDateTime() == null || reservationDto.getEndDateTime() == null) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            if (reservationDto.getStartDateTime().isAfter(reservationDto.getEndDateTime())) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            OfficeRoom officeRoom = officeRoomRepository.findById(reservationDto.getOfficeRoomId())
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException());

            User user = userRepository.findById(reservationDto.getUserId())
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException());

            reservationRepository.findByOfficeRoomIdAndStartDateTimeBetween(
                            reservationDto.getOfficeRoomId(),
                            reservationDto.getStartDateTime(),
                            reservationDto.getEndDateTime())
                    .ifPresent(existingReservation -> {
                        throw new ReservationCustomException.ReservationConflictException();
                    });

            Reservation reservation = new Reservation();
            reservation.setReservationTitle(reservationDto.getReservationTitle());
            reservation.setUser(user);
            reservation.setStartDateTime(reservationDto.getStartDateTime());
            reservation.setEndDateTime(reservationDto.getEndDateTime());
            reservation.setStatus(ReservationStatus.PENDING);
            reservation.setOfficeRoom(officeRoom);
            reservation.setDurationAsHours((reservationDto.getDurationAsHours()));

            if (reservationDto.getEvent() != null) {
                Event event = new Event();
                event.setMeetingTitle(reservationDto.getEvent().getMeetingTitle());
                event.setDescription(reservationDto.getEvent().getDescription());
                event.setAttendees(reservationDto.getEvent().getAttendees());
                event.setContactEmail(reservationDto.getEvent().getContactEmail());
                event.setDepartment(reservationDto.getEvent().getDepartment());
                reservation.setEvent(event);
            }

            user.getReservations().add(reservation);

            Reservation savedReservation = reservationRepository.save(reservation);
            userRepository.save(user);

            this.sendReservationConfirmationEmail(savedReservation, officeRoom, user);

            return mapToDto(savedReservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error creating reservation", e);
        }
    }

    @Override
    public void deleteReservation(UUID id) {
        try {
            if (id == null) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException());
            reservationRepository.delete(reservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error deleting reservation", e);
        }
    }

    @Override
    public ReservationDto updateReservation(UUID reservationId, ReservationDto reservationDto) {
        try {
            if (reservationId == null || reservationDto == null) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            if (reservationDto.getStartDateTime() == null || reservationDto.getEndDateTime() == null) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            if (reservationDto.getStartDateTime().isAfter(reservationDto.getEndDateTime())) {
                throw new ReservationCustomException.InvalidReservationDateException();
            }

            Reservation existingReservation = reservationRepository.findById(reservationId)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException());

            reservationRepository.findByOfficeRoomIdAndStartDateTimeBetween(
                            reservationDto.getOfficeRoomId(),
                            reservationDto.getStartDateTime(),
                            reservationDto.getEndDateTime())
                    .ifPresent(reservation -> {
                        if (!reservation.getId().equals(existingReservation.getId())) {
                            throw new ReservationCustomException.ReservationConflictException();
                        }
                    });

            User user = userRepository.findById(reservationDto.getUserId())
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException());

            existingReservation.setReservationTitle(reservationDto.getReservationTitle());
            existingReservation.setUser(user);
            existingReservation.setStartDateTime(reservationDto.getStartDateTime());
            existingReservation.setEndDateTime(reservationDto.getEndDateTime());
            existingReservation.setDurationAsHours(reservationDto.getDurationAsHours());
            existingReservation.setStatus(reservationDto.getStatus());

            if (!existingReservation.getOfficeRoom().getId().equals(reservationDto.getOfficeRoomId())) {
                OfficeRoom officeRoom = officeRoomRepository.findById(reservationDto.getOfficeRoomId())
                        .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException());
                existingReservation.setOfficeRoom(officeRoom);
            }

            if (reservationDto.getEvent() != null) {
                Event event = existingReservation.getEvent();
                if (event == null) {
                    event = new Event();
                }
                event.setMeetingTitle(reservationDto.getEvent().getMeetingTitle());
                event.setDescription(reservationDto.getEvent().getDescription());
                event.setAttendees(reservationDto.getEvent().getAttendees());
                event.setContactEmail(reservationDto.getEvent().getContactEmail());
                event.setDepartment(reservationDto.getEvent().getDepartment());
                existingReservation.setEvent(event);
            }

            Reservation updatedReservation = reservationRepository.save(existingReservation);
            return mapToDto(updatedReservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error updating reservation", e);
        }
    }

    @Override
    public ReservationDto findReservationById(UUID id) {
        try {
            if (id == null) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(() -> new ReservationCustomException.ReservationNotFoundException());
            return mapToDto(reservation);
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding reservation by ID", e);
        }
    }

    @Override
    public List<ReservationDto> findReservationsByOfficeRoomId(UUID officeRoomId) {
        try {
            if (officeRoomId == null) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            List<Reservation> reservations = reservationRepository.findByOfficeRoomId(officeRoomId);
            if (reservations.isEmpty()) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            return reservations.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding reservations by office room ID", e);
        }
    }

    @Override
    public List<ReservationDto> findReservationsByUserId(UUID userId) {
        try {
            if (userId == null) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            List<Reservation> reservations = reservationRepository.findByUserId(userId);
            if (reservations.isEmpty()) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            return reservations.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error finding reservations by user Id", e);
        }
    }

    @Override
    public List<String> getReservationStatusList() {
        try {
            List<String> statusList = Stream.of(ReservationStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());

            if (statusList.isEmpty()) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            return statusList;
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error getting reservation status list", e);
        }
    }

    private ReservationDto mapToDto(Reservation reservation) {
        try {
            if (reservation == null) {
                throw new ReservationCustomException.ReservationNotFoundException();
            }

            ReservationDto dto = modelMapper.map(reservation, ReservationDto.class);
            dto.setUserId(reservation.getUser().getId());
            dto.setOfficeRoomId(reservation.getOfficeRoom().getId());
            return dto;
        } catch (ReservationCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("Error mapping reservation to DTO", e);
        }
    }

    private void sendReservationConfirmationEmail(Reservation reservation, OfficeRoom officeRoom, User user) throws MessagingException {
        try {
            String confirmationNumber = reservation.getId().toString();
            String reservationUrl = "http://localhost:5173/profile";

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

            String date = reservation.getStartDateTime().format(dateFormatter);
            String startTime = reservation.getStartDateTime().format(timeFormatter);
            String endTime = reservation.getEndDateTime().format(timeFormatter);

            List<String> roomResources = officeRoom.getResources().stream()
                    .map(resource -> resource.getName())
                    .collect(Collectors.toList());

            String totalPrice = "$" + officeRoom.getPricePerHour().multiply(
                    BigDecimal.valueOf(reservation.getDurationAsHours())).toString();

            emailService.sendReservationConfirmation(
                    user.getEmail(),
                    user.getUsername(),
                    officeRoom.getName(),
                    date,
                    startTime,
                    endTime,
                    officeRoom.getBuilding(),
                    officeRoom.getFloor(),
                    totalPrice,
                    roomResources,
                    confirmationNumber,
                    reservationUrl
            );
        } catch (MessagingException e) {
            throw e;
        }
    }
}