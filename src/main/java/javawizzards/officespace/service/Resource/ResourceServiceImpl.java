package javawizzards.officespace.service.Resource;

import javawizzards.officespace.dto.Resource.ResourceDto;
import javawizzards.officespace.entity.Resource;
import javawizzards.officespace.entity.OfficeRoom;
import javawizzards.officespace.exception.Resource.ResourceCustomException;
import javawizzards.officespace.repository.ResourceRepository;
import javawizzards.officespace.repository.OfficeRoomRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ResourceServiceImpl implements ResourceService {

    private final ResourceRepository resourceRepository;
    private final OfficeRoomRepository officeRoomRepository;
    private final ModelMapper modelMapper;

    public ResourceServiceImpl(ResourceRepository resourceRepository, OfficeRoomRepository officeRoomRepository, ModelMapper modelMapper) {
        this.resourceRepository = resourceRepository;
        this.officeRoomRepository = officeRoomRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResourceDto findResourceById(UUID id) {
        try {
            Resource resource = resourceRepository.findById(id)
                    .orElseThrow(ResourceCustomException.ResourceNotFoundException::new);
            return mapToDto(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error finding resource by ID", e);
        }
    }

    @Override
    public List<ResourceDto> findResourcesByOfficeRoomId(UUID officeRoomId) {
        try {
            List<Resource> resources = resourceRepository.findByOfficeRoomId(officeRoomId);
            if (resources.isEmpty()) {
                throw new ResourceCustomException.ResourceNotFoundException();
            }
            return resources.stream().map(this::mapToDto).collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("Error finding resources by office room ID", e);
        }
    }

    @Override
    public ResourceDto createResource(ResourceDto resourceDto) {
        try {
            if (resourceRepository.findByType(resourceDto.getType()).isPresent()) {
                throw new ResourceCustomException.ResourceAlreadyExistsException();
            }

            OfficeRoom officeRoom = officeRoomRepository.findById(resourceDto.getOfficeRoomId())
                    .orElseThrow(() -> new RuntimeException("Office Room not found"));

            Resource resource = new Resource();
            resource.setName(resourceDto.getName());
            resource.setType(resourceDto.getType());
            resource.setQuantity(resourceDto.getQuantity());
            resource.setOfficeRoom(officeRoom);

            resourceRepository.save(resource);
            return mapToDto(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error creating resource", e);
        }
    }

    @Override
    public ResourceDto updateResource(UUID id, ResourceDto resourceDto) {
        try {
            Resource resource = resourceRepository.findById(id)
                    .orElseThrow(ResourceCustomException.ResourceNotFoundException::new);

            resource.setName(resourceDto.getName());
            resource.setType(resourceDto.getType());
            resource.setQuantity(resourceDto.getQuantity());

            resourceRepository.save(resource);
            return mapToDto(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error updating resource", e);
        }
    }

    @Override
    public void deleteResource(UUID id) {
        try {
            Resource resource = resourceRepository.findById(id)
                    .orElseThrow(ResourceCustomException.ResourceNotFoundException::new);
            resourceRepository.delete(resource);
        } catch (Exception e) {
            throw new RuntimeException("Error deleting resource", e);
        }
    }

    private ResourceDto mapToDto(Resource resource) {
        return modelMapper.map(resource, ResourceDto.class);
    }
}
