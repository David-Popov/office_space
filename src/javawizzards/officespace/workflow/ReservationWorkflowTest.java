//package javawizzards.officespace.workflow;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
//
//@SpringBootTest(properties = {
//        "spring.datasource.url=jdbc:h2:mem:testdb",
//        "spring.datasource.driver-class-name=org.h2.Driver",
//        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
//        "spring.jpa.hibernate.ddl-auto=update"
//})
//@AutoConfigureMockMvc
//@TestPropertySource(locations = "classpath:application-test.properties")
//public class ReservationWorkflowTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Test
//    public void testReservationEndpoint() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.get("/reservations"))
//                .andExpect(MockMvcResultMatchers.status().isOk());
//    }
//}

//package javawizzards.officespace.workflow;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.web.servlet.MockMvc;
//
//import javawizzards.officespace.repository.ReservationRepository;
//import javawizzards.officespace.service.Reservation.ReservationService;
//
//@SpringBootTest(properties = {"logging.level.org.springframework=DEBUG", "SERVER_PORT=8080"})
//@AutoConfigureMockMvc
//@EnableJpaRepositories(basePackages = "javawizzards.officespace.repository")
//@ComponentScan(basePackages = "javawizzards.officespace")
//@ContextConfiguration
//public class ReservationWorkflowTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReservationRepository reservationRepository;
//
//    @MockBean
//    private ReservationService reservationService;
//
//    @Test
//    void contextLoads() {
//        // Test the application context loads successfully
//    }
//}
