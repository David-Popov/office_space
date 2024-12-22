// src/test/java/javawizzards/officespace/testonlyrepository/TestOnlyRepository.java

package javawizzards.officespace.testonlyrepository;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TestOnlyRepository extends JpaRepository<TestOnlyEntity, Long> {
    Optional<TestOnlyEntity> findByUsername(String username);
}
