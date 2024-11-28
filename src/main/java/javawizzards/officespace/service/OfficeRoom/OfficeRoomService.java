package javawizzards.officespace.service.OfficeRoom;

import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface OfficeRoomService {
    List<OfficeRoomDto> getOfficeRooms();
    OfficeRoomDto createOfficeRoom(OfficeRoomDto officeRoomDto);
    OfficeRoomDto updateOfficeRoom(UUID id, OfficeRoomDto officeRoomDto);
    void deleteOfficeRoom(UUID id);
    OfficeRoomDto findOfficeRoomById(UUID id);
    List<OfficeRoomDto> findOfficeRoomsByCompanyName(String companyName);
    List<OfficeRoomDto> findOfficeRoomByStatus(String status);
    List<OfficeRoomDto> findOfficeRoomsByCapacity(int capacity);
    List<OfficeRoomDto> findOfficeRoomsByFloor(String floor);
    List<OfficeRoomDto> findOfficeRoomsByType(String type);
    List<OfficeRoomDto> filterOfficeRooms(String name, String building, String floor, String type, Integer capacity);
    List<OfficeRoomDto> findAvailableRooms(LocalDateTime startDateTime, LocalDateTime endDateTime);
    List<String> getOfficeRoomStatusList();
    List<String> getOfficeRoomTypeList();
}