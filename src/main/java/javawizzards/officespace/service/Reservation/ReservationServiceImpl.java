package javawizzards.officespace.service.Reservation;

import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.entity.Reservation;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.OfficeRoom.RoomType;
import javawizzards.officespace.enumerations.Reservation.ReservationStatus;
import javawizzards.officespace.exception.Reservation.ReservationCustomException;
import javawizzards.officespace.repository.OfficeRoomRepository;
import javawizzards.officespace.repository.ReservationRepository;
import javawizzards.officespace.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

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

    public ReservationServiceImpl(ReservationRepository reservationRepository, 
                                  OfficeRoomRepository officeRoomRepository, 
                                  UserRepository userRepository, 
                                  ModelMapper modelMapper) {
        this.reservationRepository = reservationRepository;
        this.officeRoomRepository = officeRoomRepository;
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ReservationDto createReservation(ReservationDto reservationDto) {
        try {
            OfficeRoom officeRoom = officeRoomRepository.findById(reservationDto.getOfficeRoomId())
                    .orElseThrow(ReservationCustomException.ReservationNotFoundException::new);

            Reservation reservation = new Reservation();
            reservation.setReservationTitle(reservationDto.getReservationTitle());
            reservation.setUserId(reservationDto.getUserId());
            reservation.setStartDateTime(reservationDto.getStartDateTime());
            reservation.setEndDateTime(reservationDto.getEndDateTime());
            reservation.setStatus(reservationDto.getStatus());
            reservation.setUserId(reservationDto.getUserId());
            reservation.setOfficeRoom(officeRoom);
            reservation.setDurationAsHours((reservationDto.getDurationAsHours()));

            List<User> participants = reservationDto.getParticipantIds().stream()
                    .map(userId -> userRepository.findById(userId)
                    .orElseThrow(ReservationCustomException.ReservationNotFoundException::new))
                    .collect(Collectors.toList());
            reservation.setParticipants(participants);

            Reservation savedReservation = reservationRepository.save(reservation);
            return mapToDto(savedReservation);
        } catch (Exception e) {
            throw new RuntimeException("Error creating reservation", e);
        }
    }

    @Override
    public void deleteReservation(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(ReservationCustomException.ReservationNotFoundException::new);
            reservationRepository.delete(reservation);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting reservation", e);
        }
    }

    @Override
    public ReservationDto updateReservation(UUID reservationId, ReservationDto reservationDto) {
        try {
            Reservation existingReservation = reservationRepository.findById(reservationId)
                    .orElseThrow(ReservationCustomException.ReservationNotFoundException::new);

            reservationRepository.findByOfficeRoomIdAndStartDateTimeBetween(
                    reservationDto.getOfficeRoomId(), reservationDto.getStartDateTime(), reservationDto.getEndDateTime())
                    .ifPresent(reservation -> {
                        if (reservation.getId() != existingReservation.getId()) {
                            throw new ReservationCustomException.ReservationConflictException();
                        }
                    });

            existingReservation.setReservationTitle(reservationDto.getReservationTitle());
            existingReservation.setUserId(reservationDto.getUserId());
            existingReservation.setStartDateTime(reservationDto.getStartDateTime());
            existingReservation.setEndDateTime(reservationDto.getEndDateTime());
            existingReservation.setDurationAsHours(reservationDto.getDurationAsHours());
            existingReservation.setStatus(reservationDto.getStatus());

            if (existingReservation.getOfficeRoom().getId() != reservationDto.getOfficeRoomId()) {
                OfficeRoom officeRoom = officeRoomRepository.findById(reservationDto.getOfficeRoomId())
                        .orElseThrow(ReservationCustomException.ReservationNotFoundException::new);
                existingReservation.setOfficeRoom(officeRoom);
            }

            List<User> updatedParticipants = reservationDto.getParticipantIds().stream()
                    .map(userId -> userRepository.findById(userId)
                    .orElseThrow(ReservationCustomException.ReservationNotFoundException::new))
                    .collect(Collectors.toList());
            existingReservation.setParticipants(updatedParticipants);

            Reservation updatedReservation = reservationRepository.save(existingReservation);
            return mapToDto(updatedReservation);
        } catch (Exception e) {
            throw new RuntimeException("Error updating reservation", e);
        }
    }

    @Override
    public ReservationDto findReservationById(UUID id) {
        try {
            Reservation reservation = reservationRepository.findById(id)
                    .orElseThrow(ReservationCustomException.ReservationNotFoundException::new);
            return mapToDto(reservation);
        } catch (Exception e) {
            throw new RuntimeException("Error finding reservation by ID", e);
        }
    }

    @Override
    public List<ReservationDto> findReservationsByOfficeRoomId(UUID officeRoomId) {
        try {
            return reservationRepository.findByOfficeRoomId(officeRoomId)
                    .stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error finding reservations by office room ID", e);
        }
    }

    @Override
    public List<ReservationDto> findReservationsByUserId(UUID userId) {
        try {
            return reservationRepository.findByUserId(userId)
                    .stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error finding reservations by user Id", e);
        }
    }

    @Override
    public List<String> getReservationStatusList() {
        List<String> statusList = Stream.of(ReservationStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return statusList;
    }

    private ReservationDto mapToDto(Reservation reservation) {
        return modelMapper.map(reservation, ReservationDto.class);
    }
}