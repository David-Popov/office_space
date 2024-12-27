package javawizzards.officespace.Reservation;

import javawizzards.officespace.controller.ReservationController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(ReservationController.class)
public class ReservationControllerEdgeCaseTest {

    @Autowired
    private MockMvc mockMvc;

    // Test for invalid userId
    @Test
    void testCreateReservationWithInvalidUserId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .param("userId", "invalid")
                        .param("roomId", "1")
                        .param("date", "2024-12-23"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Invalid user ID"));
    }

    // Test for missing parameters (e.g., roomId)
    @Test
    void testCreateReservationWithMissingRoomId() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .param("userId", "1")
                        .param("date", "2024-12-23"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Missing required parameter: roomId"));
    }

    // Test for invalid date format
    @Test
    void testCreateReservationWithInvalidDateFormat() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/reservations")
                        .param("userId", "1")
                        .param("roomId", "1")
                        .param("date", "invalid-date"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Invalid date format"));
    }
}
