package javawizzards.officespace.service.OfficeRoom;

import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;
import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.dto.Resource.ResourceDto;
import javawizzards.officespace.entity.*;
import javawizzards.officespace.enumerations.OfficeRoom.RoomStatus;
import javawizzards.officespace.enumerations.OfficeRoom.RoomType;
import javawizzards.officespace.exception.OfficeRoom.OfficeRoomCustomException;
import javawizzards.officespace.repository.CompanyRepository;
import javawizzards.officespace.repository.OfficeRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OfficeRoomServiceImpl implements OfficeRoomService {

    private final OfficeRoomRepository officeRoomRepository;
    private final CompanyRepository companyRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    public OfficeRoomServiceImpl(OfficeRoomRepository officeRoomRepository, CompanyRepository companyRepository, ModelMapper modelMapper) {
        this.officeRoomRepository = officeRoomRepository;
        this.companyRepository = companyRepository;
        this.modelMapper = modelMapper;
        this.logger = Logger.getLogger(OfficeRoomServiceImpl.class.getName());

        modelMapper.createTypeMap(OfficeRoom.class, OfficeRoomDto.class)
                .addMapping(OfficeRoom::getName, OfficeRoomDto::setOfficeRoomName)
                .addMapping(OfficeRoom::getId, OfficeRoomDto::setId)
                .addMapping(OfficeRoom::getReservations, OfficeRoomDto::setReservations)
                .addMapping(OfficeRoom::getResources, OfficeRoomDto::setResources)
                .addMapping(OfficeRoom::getCompany, OfficeRoomDto::setCompany);

        modelMapper.createTypeMap(CreateReservationDto.class, Reservation.class)
                .addMappings(mapper -> {
                    mapper.skip(Reservation::setId);
                    mapper.skip(Reservation::setStatus);
                });
    }

    @Override
    public List<OfficeRoomDto> getOfficeRooms() {
        try {
            List<OfficeRoom> officeRooms = officeRoomRepository.findAll();
            return officeRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error fetching office rooms: " + e.getMessage());
            throw new RuntimeException("Failed to fetch office rooms", e);
        }
    }

    @Override
    public OfficeRoomDto createOfficeRoom(OfficeRoomDto officeRoomDto) {
        try {
            Company company = companyRepository.findById(officeRoomDto.getCompany().getId())
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            OfficeRoom officeRoom = new OfficeRoom();
            officeRoom.setName(officeRoomDto.getOfficeRoomName());
            officeRoom.setAddress(officeRoomDto.getAddress());
            officeRoom.setBuilding(officeRoomDto.getBuilding());
            officeRoom.setFloor(officeRoomDto.getFloor());
            officeRoom.setType(officeRoomDto.getType());
            officeRoom.setCapacity(officeRoomDto.getCapacity());
            officeRoom.setStatus(RoomStatus.AVAILABLE);
            officeRoom.setPictureUrl(officeRoomDto.getPictureUrl());
            officeRoom.setPricePerHour(officeRoomDto.getPricePerHour());
            officeRoom.setCompany(company);
            officeRoom.setResources(new ArrayList<>());
            officeRoom.setReservations(new ArrayList<>());

            OfficeRoom savedOfficeRoom = officeRoomRepository.save(officeRoom);
            return mapToDto(savedOfficeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while creating office room: " + e.getMessage());
            throw new RuntimeException("Failed to create office room", e);
        }
    }

    @Override
    public OfficeRoomDto updateOfficeRoom(UUID officeRoomId, OfficeRoomDto officeRoomDto) {
        try {
            OfficeRoom existingOfficeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            Company company = companyRepository.findById(officeRoomDto.getCompany().getId())
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            existingOfficeRoom.setName(officeRoomDto.getOfficeRoomName());
            existingOfficeRoom.setAddress(officeRoomDto.getAddress());
            existingOfficeRoom.setBuilding(officeRoomDto.getBuilding());
            existingOfficeRoom.setFloor(officeRoomDto.getFloor());
            existingOfficeRoom.setType(officeRoomDto.getType());
            existingOfficeRoom.setCapacity(officeRoomDto.getCapacity());
            existingOfficeRoom.setPictureUrl(officeRoomDto.getPictureUrl());
            existingOfficeRoom.setPricePerHour(officeRoomDto.getPricePerHour());
            existingOfficeRoom.setCompany(company);

            OfficeRoom updatedOfficeRoom = officeRoomRepository.save(existingOfficeRoom);
            return mapToDto(updatedOfficeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while updating office room: " + e.getMessage());
            throw new RuntimeException("Failed to update office room", e);
        }
    }

    @Override
    public void deleteOfficeRoom(UUID officeRoomId) {
        try {
            OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);
            officeRoomRepository.delete(officeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while deleting office room: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public OfficeRoomDto findOfficeRoomById(UUID officeRoomId) {
        try {
            OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);
            return mapToDto(officeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while fetching office room by ID: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByCompanyName(String companyName) {
        try {
            List<OfficeRoom> officeRooms = officeRoomRepository.findByCompanyName(companyName);
            return officeRooms.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching office rooms by company name: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomByStatus(String status) {
        try {
            return officeRoomRepository.findByStatus(status).stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching office rooms by status: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByCapacity(int capacity) {
        try {
            List<OfficeRoom> officeRooms = officeRoomRepository.findByCapacity(capacity);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }
            return officeRooms.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching office rooms by capacity: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByFloor(String floor) {
        try {
            List<OfficeRoom> officeRooms = officeRoomRepository.findByFloor(floor);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }
            return officeRooms.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching office rooms by floor: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByType(String type) {
        try {
            List<OfficeRoom> officeRooms = officeRoomRepository.findByType(type);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }
            return officeRooms.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            logger.severe("Error occurred while fetching office rooms by type: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public List<OfficeRoomDto> filterOfficeRooms(String name, String building, String floor, String type, Integer capacity) {
        List<OfficeRoom> officeRooms = officeRoomRepository.filterByCriteria(name, building, floor, type, capacity);
        return officeRooms.stream()
                          .map(this::mapToDto)
                          .collect(Collectors.toList());
    }

@Override
public List<OfficeRoomDto> findAvailableRooms(LocalDateTime startDateTime, LocalDateTime endDateTime) {
    try {
        List<OfficeRoom> officeRooms = officeRoomRepository.findAvailableRooms(startDateTime, endDateTime);
        return officeRooms.stream().map(this::mapToDto).collect(Collectors.toList());
    } catch (Exception e) {
        logger.severe("Error occurred while finding available office rooms: " + e.getMessage());
        throw e;
    }
}

    @Override
    public OfficeRoomDto addResourceToRoom(UUID officeRoomId, ResourceDto resourceDto) {
        try {
            OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            Resource resource = new Resource();
            resource.setName(resourceDto.getName());
//            resource.setAvailable(resourceDto.isAvailable());
            resource.setOfficeRoom(officeRoom);

            if (officeRoom.getResources() == null) {
                officeRoom.setResources(new ArrayList<>());
            }
            officeRoom.getResources().add(resource);

            OfficeRoom updatedOfficeRoom = officeRoomRepository.save(officeRoom);

            return mapToDto(updatedOfficeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while adding resource to office room: " + e.getMessage());
            throw new RuntimeException("Failed to add resource to office room", e);
        }
    }

    @Override
    public OfficeRoomDto addResourcesToRoom(UUID officeRoomId, List<ResourceDto> resourceDtos) {
        try {
            OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            List<Resource> newResources = resourceDtos.stream()
                    .map(dto -> {
                        Resource resource = new Resource();
                        resource.setName(dto.getName());
//                        resource.setAvailable(dto.isAvailable());
                        resource.setOfficeRoom(officeRoom);
                        return resource;
                    })
                    .collect(Collectors.toList());

            if (officeRoom.getResources() == null) {
                officeRoom.setResources(new ArrayList<>());
            }
            officeRoom.getResources().addAll(newResources);

            OfficeRoom updatedOfficeRoom = officeRoomRepository.save(officeRoom);
            return mapToDto(updatedOfficeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while adding resources to office room: " + e.getMessage());
            throw new RuntimeException("Failed to add resources to office room", e);
        }
    }

    @Override
    public OfficeRoomDto removeResourceFromRoom(UUID officeRoomId, UUID resourceId) {
        try {
            OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            officeRoom.getResources().removeIf(resource -> resource.getId().equals(resourceId));

            OfficeRoom updatedOfficeRoom = officeRoomRepository.save(officeRoom);
            return mapToDto(updatedOfficeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while removing resource from office room: " + e.getMessage());
            throw new RuntimeException("Failed to remove resource from office room", e);
        }
    }

    @Override
    public List<String> getOfficeRoomStatusList() {
        List<String> statusList = Stream.of(RoomStatus.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return statusList;
    }

    @Override
    public List<String> getOfficeRoomTypeList() {
        List<String> statusList = Stream.of(RoomType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return statusList;
    }

    private OfficeRoomDto mapToDto(OfficeRoom officeRoom) {
        try {
            OfficeRoomDto dto = modelMapper.map(officeRoom, OfficeRoomDto.class);
//            dto.setOfficeRoomName(officeRoom.getName());
//            dto.setId(officeRoom.getId());

            return dto;
        } catch (Exception e) {
            logger.severe("Error occurred while mapping office room to DTO: " + e.getMessage());
            throw new RuntimeException("Error mapping office room to DTO", e);
        }
    }
}