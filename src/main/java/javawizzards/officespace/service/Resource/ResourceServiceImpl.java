package javawizzards.officespace.service.Resource;

import javawizzards.officespace.dto.Resource.ResourceDto;
import javawizzards.officespace.entity.Resource;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.enumerations.Resource.ResourceStatus;
import javawizzards.officespace.exception.Resource.ResourceCustomException;
import javawizzards.officespace.repository.ResourceRepository;
import javawizzards.officespace.repository.OfficeRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final OfficeRoomRepository officeRoomRepository;
    private final ModelMapper modelMapper;

    public ResourceServiceImpl(ResourceRepository resourceRepository,
                               OfficeRoomRepository officeRoomRepository,
                               ModelMapper modelMapper) {
        this.resourceRepository = resourceRepository;
        this.officeRoomRepository = officeRoomRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public ResourceDto findResourceById(UUID id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(ResourceCustomException.ResourceNotFoundException::new);
        return mapToDto(resource);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ResourceDto> findResourcesByOfficeRoomId(UUID officeRoomId) {
        List<Resource> resources = resourceRepository.findByOfficeRoomId(officeRoomId);
        if (resources.isEmpty()) {
            throw new ResourceCustomException.ResourceNotFoundException();
        }
        return resources.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ResourceDto createResource(UUID officeRoomId, ResourceDto resourceDto) {
        OfficeRoom officeRoom = officeRoomRepository.findById(officeRoomId)
                .orElseThrow(() -> new ResourceCustomException.OfficeRoomNotFoundException());

        if (resourceRepository.existsByNameAndTypeAndOfficeRoomId(
                resourceDto.getName(), resourceDto.getType().toString(), officeRoomId)) {
            throw new ResourceCustomException.ResourceAlreadyExistsException();
        }

        Resource resource = new Resource();
        updateResourceFromDto(resource, resourceDto);
        resource.setOfficeRoom(officeRoom);
        resource.setStatus(ResourceStatus.AVAILABLE);

        return mapToDto(resourceRepository.save(resource));
    }

    @Override
    @Transactional
    public ResourceDto updateResource(UUID id, ResourceDto resourceDto) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(ResourceCustomException.ResourceNotFoundException::new);

        if (!resource.getName().equals(resourceDto.getName()) ||
                !resource.getType().equals(resourceDto.getType())) {
            if (resourceRepository.existsByNameAndTypeAndOfficeRoomId(
                    resourceDto.getName(),
                    resourceDto.getType().toString(),
                    resource.getOfficeRoom().getId())) {
                throw new ResourceCustomException.ResourceAlreadyExistsException();
            }
        }

        updateResourceFromDto(resource, resourceDto);
        return mapToDto(resourceRepository.save(resource));
    }

    @Override
    @Transactional
    public void deleteResource(UUID id) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(ResourceCustomException.ResourceNotFoundException::new);
        resourceRepository.delete(resource);
    }

    @Override
    @Transactional
    public ResourceDto updateResourceStatus(UUID id, ResourceStatus status, String maintenanceNotes) {
        Resource resource = resourceRepository.findById(id)
                .orElseThrow(ResourceCustomException.ResourceNotFoundException::new);

        resource.setStatus(status);
        if (status == ResourceStatus.UNDER_MAINTENANCE) {
            resource.setMaintenanceNotes(maintenanceNotes);
        }

        return mapToDto(resourceRepository.save(resource));
    }

    private void updateResourceFromDto(Resource resource, ResourceDto dto) {
        resource.setName(dto.getName());
        resource.setType(dto.getType());
        resource.setQuantity(dto.getQuantity());
        resource.setDescription(dto.getDescription());
        if (dto.getStatus() != null) {
            resource.setStatus(dto.getStatus());
        }
        if (dto.getMaintenanceNotes() != null) {
            resource.setMaintenanceNotes(dto.getMaintenanceNotes());
        }
    }

    private ResourceDto mapToDto(Resource resource) {
        return modelMapper.map(resource, ResourceDto.class);
    }
}