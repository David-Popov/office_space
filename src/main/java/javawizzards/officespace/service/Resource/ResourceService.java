package javawizzards.officespace.service.Resource;

import javawizzards.officespace.dto.Resource.ResourceDto;

import java.util.List;
import java.util.UUID;

public interface ResourceService {
    ResourceDto findResourceById(UUID id);
    List<ResourceDto> findResourcesByOfficeRoomId(UUID officeRoomId);
    ResourceDto createResource(ResourceDto resourceDto);
    ResourceDto updateResource(UUID id, ResourceDto resourceDto);
    void deleteResource(UUID id);
}