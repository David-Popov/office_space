package javawizzards.officespace.service.Resource;

import javawizzards.officespace.dto.Resource.ResourceDto;
import javawizzards.officespace.enumerations.Resource.ResourceStatus;

import java.util.List;
import java.util.UUID;

public interface ResourceService {
    ResourceDto findResourceById(UUID id);
    List<ResourceDto> findResourcesByOfficeRoomId(UUID officeRoomId);
    ResourceDto createResource(UUID officeRoomId, ResourceDto resourceDto);
    ResourceDto updateResource(UUID id, ResourceDto resourceDto);
    void deleteResource(UUID id);
    ResourceDto updateResourceStatus(UUID id, ResourceStatus status, String maintenanceNotes);
}