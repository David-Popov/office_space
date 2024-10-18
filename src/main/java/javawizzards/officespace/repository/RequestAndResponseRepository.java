package javawizzards.officespace.repository;

import javawizzards.officespace.entity.RequestAndResponse;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RequestAndResponseRepository extends JpaRepository<RequestAndResponse, UUID> {
    Optional<RequestAndResponse> findByRequestId(String requestId);
    Optional<RequestAndResponse> findByResponseId(String responseId);
}
