package javawizzards.officespace.testonlyrepository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;

@Entity
public class TestOnlyEntity {

    @Id
    private Long id;

    private String username;

    // A no-arg constructor
    public TestOnlyEntity() {
    }

    // GETTER & SETTER for 'id'
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    // GETTER & SETTER for 'username'
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
}
