package javawizzards.officespace.repository;

import javawizzards.officespace.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByGoogleId(String googleId);
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByGoogleId(String googleId);
    boolean existsByEmail(String email);
}
