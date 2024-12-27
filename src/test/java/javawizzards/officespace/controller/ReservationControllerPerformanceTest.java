package javawizzards.officespace.controller;

import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.Reservation.ReservationService;
import javawizzards.officespace.utility.JwtUtility;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest(ReservationController.class) // WebMvcTest to test only the controller layer
public class ReservationControllerPerformanceTest {

    @Autowired
    private MockMvc mockMvc;

    // Mocking the dependencies that are required by ReservationController
    @MockBean
    private JwtUtility jwtUtility;  // Mock JwtUtility

    @MockBean
    private ReservationService reservationService;  // Mock ReservationService

    @MockBean
    private RequestAndResponseService requestAndResponseService;  // Mock RequestAndResponseService

    @Test
    void testCreateReservationPerformance() throws Exception {
        // Start measuring time for performance
        long startTime = System.currentTimeMillis();

        // Perform the request to the reservation creation endpoint
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .param("userId", "1")
                        .param("roomId", "1")
                        .param("date", "2024-12-23"))
                .andExpect(result -> {
                    // Measure time taken for the request to complete
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;

                    // Ensure the request completes within 1000ms (1 second)
                    assert(duration < 1000);
                });
    }
}
