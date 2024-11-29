package javawizzards.officespace.service.OfficeRoom;

import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;
import javawizzards.officespace.entity.Company;
import javawizzards.officespace.entity.OfficeRoom;
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
    }

    @Override
    public List<OfficeRoomDto> getOfficeRooms() {
        try{
            List<OfficeRoom> officeRooms = officeRoomRepository.findAll();
            return officeRooms.stream()
                    .map(officeRoom -> modelMapper.map(officeRoom, OfficeRoomDto.class))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public OfficeRoomDto createOfficeRoom(OfficeRoomDto officeRoomDto) {
        try {
            Company company = companyRepository.findById(officeRoomDto.getCompanyId())
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            OfficeRoom officeRoom = new OfficeRoom();
            officeRoom.setName(officeRoomDto.getOfficeRoomName());
            officeRoom.setAddress(officeRoomDto.getAddress());
            officeRoom.setBuilding(officeRoomDto.getBuilding());
            officeRoom.setFloor(officeRoomDto.getFloor());
            officeRoom.setType(officeRoomDto.getType());
            officeRoom.setCapacity(officeRoomDto.getCapacity());
            officeRoom.setStatus(RoomStatus.OCCUPIED);
            officeRoom.setPictureUrl(officeRoomDto.getPictureUrl());
            officeRoom.setPricePerHour(officeRoomDto.getPricePerHour());
            officeRoom.setCompany(company);

            OfficeRoom savedOfficeRoom = officeRoomRepository.save(officeRoom);
            return mapToDto(savedOfficeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while creating office room: " + e.getMessage());
            throw e;
        }
    }

    @Override
    public OfficeRoomDto updateOfficeRoom(UUID officeRoomId, OfficeRoomDto officeRoomDto) {
        try {
            OfficeRoom existingOfficeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            Company company = companyRepository.findById(officeRoomDto.getCompanyId())
                    .orElseThrow(OfficeRoomCustomException.OfficeRoomNotFoundException::new);

            existingOfficeRoom.setName(officeRoomDto.getOfficeRoomName());
            existingOfficeRoom.setAddress(officeRoomDto.getAddress());
            existingOfficeRoom.setBuilding(officeRoomDto.getBuilding());
            existingOfficeRoom.setFloor(officeRoomDto.getFloor());
            existingOfficeRoom.setType(officeRoomDto.getType());
            existingOfficeRoom.setCapacity(officeRoomDto.getCapacity());
//            existingOfficeRoom.setStatus(officeRoomDto.getStatus());
            existingOfficeRoom.setPictureUrl(officeRoomDto.getPictureUrl());
            existingOfficeRoom.setPricePerHour(officeRoomDto.getPricePerHour());
            existingOfficeRoom.setCompany(company);

            OfficeRoom updatedOfficeRoom = officeRoomRepository.save(existingOfficeRoom);
            return mapToDto(updatedOfficeRoom);
        } catch (Exception e) {
            logger.severe("Error occurred while updating office room: " + e.getMessage());
            throw e;
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
            return modelMapper.map(officeRoom, OfficeRoomDto.class);
        } catch (Exception e) {
            logger.severe("Error occurred while mapping office room to DTO: " + e.getMessage());
            throw new RuntimeException("Error mapping office room to DTO", e);
        }
    }
}