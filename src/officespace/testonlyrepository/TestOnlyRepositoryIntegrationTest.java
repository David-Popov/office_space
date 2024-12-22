// src/test/java/javawizzards/officespace/testonlyrepository/TestOnlyRepositoryIntegrationTest.java

package javawizzards.officespace.testonlyrepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

/**
 * This test only picks up the JPA stuff in this package,
 * ignoring the main UserRepository bean or entity classes.
 */


@DataJpaTest
@org.springframework.context.annotation.ComponentScan(
        basePackages = "javawizzards.officespace.testonlyrepository"
)
@org.springframework.data.jpa.repository.config.EnableJpaRepositories(
        basePackages = "javawizzards.officespace.testonlyrepository"
)
@org.springframework.boot.autoconfigure.domain.EntityScan(
        basePackages = "javawizzards.officespace.testonlyrepository"
)
public class TestOnlyRepositoryIntegrationTest {

    @Autowired
    private TestOnlyRepository repo;

    @Test
    void testSaveAndFind() {
        // Create a test entity
        TestOnlyEntity entity = new TestOnlyEntity();
        entity.setId(1L);
        entity.setUsername("alice");

        // Save to in-memory DB
        repo.save(entity);

        // Query
        var found = repo.findByUsername("alice");
        // Asserts
    }
}
