package javawizzards.officespace.service.OfficeRoom;

import jakarta.transaction.Transactional;
import javawizzards.officespace.dto.OfficeRoom.CreateOfficeRoomDto;
import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;
import javawizzards.officespace.dto.OfficeRoom.UpdateOfficeRoomRequest;
import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.dto.Resource.ResourceDto;
import javawizzards.officespace.entity.*;
import javawizzards.officespace.enumerations.OfficeRoom.RoomStatus;
import javawizzards.officespace.enumerations.OfficeRoom.RoomType;
import javawizzards.officespace.enumerations.Resource.ResourceStatus;
import javawizzards.officespace.exception.OfficeRoom.OfficeRoomCustomException;
import javawizzards.officespace.exception.Resource.ResourceCustomException;
import javawizzards.officespace.repository.CompanyRepository;
import javawizzards.officespace.repository.OfficeRoomRepository;
import javawizzards.officespace.repository.ResourceRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OfficeRoomServiceImpl implements OfficeRoomService {

    private final OfficeRoomRepository officeRoomRepository;
    private final CompanyRepository companyRepository;
    private final ResourceRepository resourceRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    public OfficeRoomServiceImpl(OfficeRoomRepository officeRoomRepository, CompanyRepository companyRepository, ResourceRepository resourceRepository, ModelMapper modelMapper) {
        this.officeRoomRepository = officeRoomRepository;
        this.companyRepository = companyRepository;
        this.resourceRepository = resourceRepository;
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
            throw e;
        }
    }

    @Override
    public OfficeRoomDto createOfficeRoom(CreateOfficeRoomDto officeRoomDto) {
        try {
            Company company = companyRepository.findById(officeRoomDto.getCompanyId())
                    .orElseThrow(() -> new OfficeRoomCustomException.CompanyNotFoundException());

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
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.OfficeRoomCreationFailedException();
        }
    }

    @Override
    public OfficeRoomDto updateOfficeRoom(UUID id, UpdateOfficeRoomRequest officeRoomRequest) {
        try {
            if (id == null) {
                throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
            }

            OfficeRoom existingOfficeRoom = officeRoomRepository.findById(id)
                    .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());

            Company company = companyRepository.findById(officeRoomRequest.getCompanyId())
                    .orElseThrow(() -> new OfficeRoomCustomException.CompanyNotFoundException());

            updateOfficeRoomFields(existingOfficeRoom, officeRoomRequest, company);

            OfficeRoom updatedOfficeRoom = officeRoomRepository.save(existingOfficeRoom);
            return mapToDto(updatedOfficeRoom);
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.OfficeRoomUpdateFailedException();
        }
    }

    @Override
    public void deleteOfficeRoom(UUID officeRoomId) {
        try {
            if (officeRoomId == null) {
                throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
            }

            OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());

            officeRoomRepository.delete(officeRoom);
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.OfficeRoomDeletionFailedException();
        }
    }

    @Override
    public OfficeRoomDto findOfficeRoomById(UUID officeRoomId) {
        try {
            if (officeRoomId == null) {
                throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
            }

            OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                    .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());
            return mapToDto(officeRoom);
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByCompanyName(String companyName) {
        try {
            if (companyName == null || companyName.trim().isEmpty()) {
                throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
            }

            List<OfficeRoom> officeRooms = officeRoomRepository.findByCompanyName(companyName);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }

            return officeRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomByStatus(String status) {
        try {
            if (status == null || status.trim().isEmpty()) {
                throw new OfficeRoomCustomException.InvalidRoomStatusException();
            }

            List<OfficeRoom> officeRooms = officeRoomRepository.findByStatus(status);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }

            return officeRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByType(String type) {
        try {
            if (type == null || type.trim().isEmpty()) {
                throw new OfficeRoomCustomException.InvalidRoomTypeException();
            }

            List<OfficeRoom> officeRooms = officeRoomRepository.findByType(type);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }

            return officeRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByCapacity(int capacity) {
        try {
            if (capacity <= 0) {
                throw new OfficeRoomCustomException.InvalidCapacityException();
            }

            List<OfficeRoom> officeRooms = officeRoomRepository.findByCapacity(capacity);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }

            return officeRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    @Override
    public List<OfficeRoomDto> findOfficeRoomsByFloor(String floor) {
        try {
            if (floor == null || floor.trim().isEmpty()) {
                throw new OfficeRoomCustomException.InvalidFloorException();
            }

            List<OfficeRoom> officeRooms = officeRoomRepository.findByFloor(floor);
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }

            return officeRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }
    
    @Override
    public List<OfficeRoomDto> filterOfficeRooms(String name, String building, String floor, 
                                                  String type, Integer capacity, 
                                                  BigDecimal minPrice, BigDecimal maxPrice) {
        try {
            List<OfficeRoom> officeRooms = officeRoomRepository.filterByCriteriaWithPriceRange(
                    name, building, floor, type, capacity, minPrice, maxPrice);
    
            if (officeRooms.isEmpty()) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }
    
            return officeRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
    
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }
    @Override
    public List<OfficeRoomDto> findAvailableRooms(LocalDateTime startDateTime, LocalDateTime endDateTime) {
        try {
            if (startDateTime == null || endDateTime == null) {
                throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
            }
            if (startDateTime.isAfter(endDateTime)) {
                throw new OfficeRoomCustomException.RoomNotAvailableException();
            }

            List<OfficeRoom> availableRooms = officeRoomRepository.findAvailableRooms(startDateTime, endDateTime);
            if (availableRooms.isEmpty()) {
                throw new OfficeRoomCustomException.RoomNotAvailableException();
            }

            return availableRooms.stream()
                    .map(this::mapToDto)
                    .collect(Collectors.toList());
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    @Override
    @Transactional
    public OfficeRoomDto addResourceToRoom(UUID officeRoomId, UUID resourceId) {
        if (officeRoomId == null || resourceId == null) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }

        OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceCustomException.ResourceNotFoundException());

        if (resource.getOfficeRoom() != null) {
            if (resource.getOfficeRoom().getId().equals(officeRoomId)) {
                throw new ResourceCustomException.ResourceAlreadyExistsException();
            } else {
                throw new ResourceCustomException.ResourceInUseException();
            }
        }

        if (resource.getStatus() == ResourceStatus.UNDER_MAINTENANCE) {
            throw new ResourceCustomException.ResourceUnderMaintenanceException();
        }

        resource.setOfficeRoom(officeRoom);
        if (officeRoom.getResources() == null) {
            officeRoom.setResources(new ArrayList<>());
        }
        officeRoom.getResources().add(resource);

        OfficeRoom updatedOfficeRoom = officeRoomRepository.save(officeRoom);
        return mapToDto(updatedOfficeRoom);
    }

    @Override
    @Transactional
    public OfficeRoomDto addResourcesToRoom(UUID officeRoomId, List<UUID> resourceIds) {
        if (officeRoomId == null || resourceIds == null || resourceIds.isEmpty()) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }

        OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());

        List<Resource> resources = resourceIds.stream()
                .map(id -> resourceRepository.findById(id)
                        .orElseThrow(() -> new ResourceCustomException.ResourceNotFoundException()))
                .collect(Collectors.toList());

        for (Resource resource : resources) {
            if (resource.getOfficeRoom() != null) {
                if (resource.getOfficeRoom().getId().equals(officeRoomId)) {
                    throw new ResourceCustomException.ResourceAlreadyExistsException();
                } else {
                    throw new ResourceCustomException.ResourceInUseException();
                }
            }
            if (resource.getStatus() == ResourceStatus.UNDER_MAINTENANCE) {
                throw new ResourceCustomException.ResourceUnderMaintenanceException();
            }
        }

        if (officeRoom.getResources() == null) {
            officeRoom.setResources(new ArrayList<>());
        }

        resources.forEach(resource -> {
            resource.setOfficeRoom(officeRoom);
            officeRoom.getResources().add(resource);
        });

        OfficeRoom updatedOfficeRoom = officeRoomRepository.save(officeRoom);
        return mapToDto(updatedOfficeRoom);
    }

    @Override
    @Transactional
    public OfficeRoomDto removeResourceFromRoom(UUID officeRoomId, UUID resourceId) {
        if (officeRoomId == null || resourceId == null) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }

        OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                .orElseThrow(() -> new OfficeRoomCustomException.OfficeRoomNotFoundException());

        Resource resource = resourceRepository.findById(resourceId)
                .orElseThrow(() -> new ResourceCustomException.ResourceNotFoundException());

        if (resource.getOfficeRoom() == null || !resource.getOfficeRoom().getId().equals(officeRoomId)) {
            throw new ResourceCustomException.ResourceNotFoundException();
        }

        if (resource.getStatus() == ResourceStatus.IN_USE) {
            throw new ResourceCustomException.ResourceInUseException();
        }

        resource.setOfficeRoom(null);
        officeRoom.getResources().remove(resource);

        OfficeRoom updatedOfficeRoom = officeRoomRepository.save(officeRoom);
        return mapToDto(updatedOfficeRoom);
    }

    private void validateResourceDtos(List<ResourceDto> resourceDtos, OfficeRoom officeRoom) {
        Set<String> resourceKeys = new HashSet<>();
        for (ResourceDto dto : resourceDtos) { //we check for duplicate resources in the dto
            String key = dto.getName() + "-" + dto.getType();
            if (!resourceKeys.add(key)) {
                throw new ResourceCustomException.ResourceAlreadyExistsException();
            }

            boolean exists = officeRoom.getResources().stream()
                    .anyMatch(r -> r.getName().equals(dto.getName())
                            && r.getType().equals(dto.getType()));
            if (exists) {
                throw new ResourceCustomException.ResourceAlreadyExistsException();
            }

            if (dto.getQuantity() < 0) {
                throw new ResourceCustomException.InvalidQuantityException();
            }
        }
    }

    private Resource createResourceFromDto(ResourceDto dto, OfficeRoom officeRoom) {
        Resource resource = new Resource();
        resource.setName(dto.getName());
        resource.setType(dto.getType());
        resource.setQuantity(dto.getQuantity());
        resource.setDescription(dto.getDescription());
        resource.setStatus(ResourceStatus.AVAILABLE);
        resource.setOfficeRoom(officeRoom);
        return resource;
    }

    @Override
    public List<String> getOfficeRoomStatusList() {
        try {
            List<String> statusList = Stream.of(RoomStatus.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());

            if (statusList.isEmpty()) {
                throw new OfficeRoomCustomException.InvalidRoomStatusException();
            }

            return statusList;
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    @Override
    public List<String> getOfficeRoomTypeList() {
        try {
            List<String> typeList = Stream.of(RoomType.values())
                    .map(Enum::name)
                    .collect(Collectors.toList());

            if (typeList.isEmpty()) {
                throw new OfficeRoomCustomException.InvalidRoomTypeException();
            }

            return typeList;
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }

    private void validateOfficeRoomData(CreateOfficeRoomDto officeRoomDto) {
        if (officeRoomDto == null) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
//        if (officeRoomDto.getCompany() == null || officeRoomDto.getCompany().getId() == null) {
//            throw new OfficeRoomCustomException.CompanyNotFoundException();
//        }
        if (officeRoomDto.getCapacity() <= 0) {
            throw new OfficeRoomCustomException.InvalidCapacityException();
        }
        if (officeRoomDto.getPricePerHour().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OfficeRoomCustomException.InvalidPriceException();
        }
        if (officeRoomDto.getFloor() == null || officeRoomDto.getFloor().trim().isEmpty()) {
            throw new OfficeRoomCustomException.InvalidFloorException();
        }
        if (officeRoomDto.getType() == null) {
            throw new OfficeRoomCustomException.InvalidRoomTypeException();
        }
    }

    private void updateOfficeRoomFields(OfficeRoom existingOfficeRoom, UpdateOfficeRoomRequest officeRoomDto, Company company) {
        if (officeRoomDto.getCapacity() <= 0) {
            throw new OfficeRoomCustomException.InvalidCapacityException();
        }
        if (officeRoomDto.getPricePerHour().compareTo(BigDecimal.ZERO) <= 0) {
            throw new OfficeRoomCustomException.InvalidPriceException();
        }

        existingOfficeRoom.setName(officeRoomDto.getOfficeRoomName());
        existingOfficeRoom.setAddress(officeRoomDto.getAddress());
        existingOfficeRoom.setBuilding(officeRoomDto.getBuilding());
        existingOfficeRoom.setFloor(officeRoomDto.getFloor());
        existingOfficeRoom.setType(officeRoomDto.getType());
        existingOfficeRoom.setCapacity(officeRoomDto.getCapacity());
        existingOfficeRoom.setPictureUrl(officeRoomDto.getPictureUrl());
        existingOfficeRoom.setPricePerHour(officeRoomDto.getPricePerHour());
        existingOfficeRoom.setCompany(company);
    }

    private OfficeRoomDto mapToDto(OfficeRoom officeRoom) {
        try {
            if (officeRoom == null) {
                throw new OfficeRoomCustomException.OfficeRoomNotFoundException();
            }
            return modelMapper.map(officeRoom, OfficeRoomDto.class);
        } catch (OfficeRoomCustomException e) {
            throw e;
        } catch (Exception e) {
            throw new OfficeRoomCustomException.InvalidOfficeRoomDataException();
        }
    }
}