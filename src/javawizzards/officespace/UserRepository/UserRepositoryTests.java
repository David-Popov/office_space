package javawizzards.officespace.UserRepository;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

@Entity
class User {

    @Id
    private Long id;
    private String username;

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

class UserRepositoryTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindByUsername() {
        // Arrange
        String username = "testUser";
        User user = new User();
        user.setUsername(username);
        when(userRepository.findByUsername(username)).thenReturn(Optional.of(user));

        // Act
        Optional<User> foundUser = userRepository.findByUsername(username);

        // Assert
        assertTrue(foundUser.isPresent());
        assertEquals(username, foundUser.get().getUsername());
        verify(userRepository, times(1)).findByUsername(username);
    }

    @Test
    void testFindByUsernameNotFound() {
        // Arrange
        String username = "nonexistentUser";
        when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act
        Optional<User> foundUser = userRepository.findByUsername(username);

        // Assert
        assertFalse(foundUser.isPresent());
        verify(userRepository, times(1)).findByUsername(username);
    }
}


