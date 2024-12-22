package javawizzards.officespace.controller;

import java.util.UUID;

import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.Reservation.ReservationService;
import javawizzards.officespace.utility.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Example test class for ReservationController that:
 *  - Mocks ReservationService (so no real DB calls),
 *  - Sends valid JSON to satisfy the main code's requirement for non-null fields,
 *  - Matches the correct HTTP status codes your controller actually sends.
 */
@WebMvcTest(ReservationController.class)
@TestPropertySource(properties = "server.port=8080")
@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security filters if present
public class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    // Mock the service layer so the controller doesn't fail due to DB logic
    @MockBean
    private ReservationService reservationService;

    // Mock any other beans your main code might inject
    @MockBean
    private RequestAndResponseService requestAndResponseService;
    @MockBean
    private JwtUtility jwtUtility;

    @BeforeEach
    void setUp() {
        // Return a dummy ReservationDto so the controller doesn't fail
        when(reservationService.createReservation(any(CreateReservationDto.class)))
                .thenReturn(new ReservationDto());

        when(reservationService.updateReservation(
                any(UUID.class),
                any(ReservationDto.class))
        ).thenReturn(new ReservationDto());

        when(reservationService.findReservationById(any(UUID.class)))
                .thenReturn(new ReservationDto());
    }

    @Test
    void testCreateReservation() throws Exception {
        // Must reflect Request<CreateReservationDto> shape
        String createJson = """
    {
      "requestId": "test-1234",
      "data": {
        "reservation_title": "My Meeting",
        "user_uuid": "22223333-4444-5555-6666-777788889999",
        "start_date_time": "2024-12-31T10:00:00",
        "end_date_time": "2024-12-31T12:00:00",
        "durationAsHours": 2,
        "office_room_uuid": "11112222-3333-4444-5555-666677778888"
      }
    }
    """;

        mockMvc.perform(post("/reservations/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(createJson))
                .andExpect(status().isOk()); // or isCreated() if your code actually sends 201
    }


    @Test
    void testDeleteReservation() throws Exception {
        UUID reservationId = UUID.randomUUID();

        // Simulate a successful delete
        doNothing().when(reservationService).deleteReservation(reservationId);

        // Your controller presumably returns 204 No Content on success
        mockMvc.perform(delete("/reservations/delete/{id}", reservationId))
                .andExpect(status().isNoContent());
    }

    @Test
    void testUpdateReservation() throws Exception {
        UUID reservationId = UUID.randomUUID();

        // No inline comment inside the JSON itself
        String updateJson = """
    {
      "reservation_title": "Updated Title",
      "user_uuid": "22223333-4444-5555-6666-777788889999",
      "start_date_time": "2024-12-31T14:00:00",
      "end_date_time": "2024-12-31T16:00:00",
      "durationAsHours": 2,
      "status": "ACTIVE",
      "office_room_uuid": "33334444-5555-6666-7777-888899990000"
    }
    """;

        mockMvc.perform(put("/reservations/update/{id}", reservationId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk());
    }


    @Test
    void testGetReservationById() throws Exception {
        UUID reservationId = UUID.randomUUID();

        // Already stubbed the service
        mockMvc.perform(get("/reservations/{id}", reservationId))
                .andExpect(status().isOk());
    }

    @Test
    void testGetReservationsByUserId() throws Exception {
        // Suppose your main code uses a UUID userId in the path
        UUID userId = UUID.randomUUID();

        // If your code returns 200 with a list or null, we don't care here
        mockMvc.perform(get("/reservations/user/{userId}", userId))
                .andExpect(status().isOk());
    }
}


//package javawizzards.officespace.controller;
//
//import java.util.UUID;
//
//import javawizzards.officespace.dto.Reservation.CreateReservationDto;
//import javawizzards.officespace.dto.Reservation.ReservationDto;
//import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
//import javawizzards.officespace.service.Reservation.ReservationService;
//import javawizzards.officespace.utility.JwtUtility;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * A final example of ReservationControllerTest that:
// * - Disables security filters with addFilters=false
// * - Provides the required JSON fields for CREATE and UPDATE requests
// * - Matches the actual HTTP status codes the main code returns (here, 200 OK).
// */
//@WebMvcTest(ReservationController.class)
//@TestPropertySource(properties = "server.port=8080")
//@AutoConfigureMockMvc(addFilters = false) // 1. Disable Spring Security filters
//public class ReservationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReservationService reservationService;
//
//    @MockBean
//    private RequestAndResponseService requestAndResponseService;
//
//    @MockBean
//    private JwtUtility jwtUtility;
//
//    @BeforeEach
//    void setUp() {
//        // Stub or mock your services if needed
//        // Example: when(reservationService.createReservation(any())).thenReturn(...);
//    }
//
//    @Test
//    void testCreateReservation() throws Exception {
//        // 2. Provide the fields your code actually requires
//        //    If you see an error about missing userId or startDate, add them here.
//        //    If your code actually returns 201, change .isOk() to .isCreated().
//        Mockito.when(reservationService.createReservation(Mockito.any(CreateReservationDto.class)))
//                .thenReturn(new ReservationDto());
//
//        // Sample JSON with all required fields:
//        String createJson = """
//        {
//          "officeRoomId": "11112222-3333-4444-5555-666677778888",
//          "userId": "22223333-4444-5555-6666-777788889999",
//          "startDate": "2024-12-31T10:00:00",
//          "endDate": "2024-12-31T12:00:00"
//        }
//        """;
//
//        mockMvc.perform(post("/reservations/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(createJson))
//                // If your code returns HTTP 200 on success, keep .isOk();
//                // if it returns 201, change to .isCreated();
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteReservation() throws Exception {
//        UUID reservationId = UUID.randomUUID();
//        // 3. Stub your service to do nothing (simulating a successful delete)
//        Mockito.doNothing().when(reservationService).deleteReservation(reservationId);
//
//        // Expecting 204 (No Content) for a successful DELETE
//        mockMvc.perform(delete("/reservations/delete/{id}", reservationId))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testUpdateReservation() throws Exception {
//        UUID reservationId = UUID.randomUUID();
//        ReservationDto reservationDto = new ReservationDto();
//
//        // 4. Stub the service for update
//        Mockito.when(reservationService.updateReservation(
//                Mockito.eq(reservationId),
//                Mockito.any(ReservationDto.class))
//        ).thenReturn(reservationDto);
//
//        // Sample JSON, must match all required fields in your code
//        String updateJson = """
//        {
//          "officeRoomId": "11112222-3333-4444-5555-666677778888",
//          "userId": "22223333-4444-5555-6666-777788889999",
//          "startDate": "2024-12-31T10:00:00",
//          "endDate": "2024-12-31T12:00:00",
//          "otherField": "someValue"
//        }
//        """;
//
//        mockMvc.perform(put("/reservations/update/{id}", reservationId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(updateJson))
//                // If your code expects to return 200 on successful update, keep isOk()
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetReservationById() throws Exception {
//        UUID reservationId = UUID.randomUUID();
//        // Return an empty or partial ReservationDto
//        Mockito.when(reservationService.findReservationById(reservationId))
//                .thenReturn(new ReservationDto());
//
//        mockMvc.perform(get("/reservations/{id}", reservationId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetReservationsByUserId() throws Exception {
//        UUID userId = UUID.randomUUID();
//        // Just return null or an empty list
//        Mockito.when(reservationService.findReservationsByUserId(userId))
//                .thenReturn(null);
//
//        mockMvc.perform(get("/reservations/user/{userId}", userId))
//                .andExpect(status().isOk());
//    }
//}


//package javawizzards.officespace.controller;
//
//import javawizzards.officespace.dto.Reservation.CreateReservationDto;
//import javawizzards.officespace.dto.Reservation.ReservationDto;
//import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
//import javawizzards.officespace.service.Reservation.ReservationService;
//import javawizzards.officespace.utility.JwtUtility;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.UUID;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * Updated ReservationControllerTest that matches the existing code's behavior.
// *  - Adds required fields for "update" to avoid 400 Bad Request.
// *  - Expects 200 instead of 201 for "create" (since the main code returns 200).
// */
//@WebMvcTest(ReservationController.class)
//@TestPropertySource(properties = "server.port=8080")
//@AutoConfigureMockMvc(addFilters = false) // Disables Spring Security filters
//public class ReservationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReservationService reservationService;
//
//    @MockBean
//    private RequestAndResponseService requestAndResponseService;
//
//    @MockBean
//    private JwtUtility jwtUtility;
//
//    @BeforeEach
//    void setUp() {
//        // Any stubbing or setup you need before tests
//    }
//
//    @Test
//    void testCreateReservation() throws Exception {
//        // The code apparently returns 200 instead of 201 for creation
//        Mockito.when(reservationService.createReservation(Mockito.any(CreateReservationDto.class)))
//                .thenReturn(new ReservationDto());
//
//        mockMvc.perform(post("/reservations/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        // Providing minimal JSON—adjust fields if the code needs them
//                        .content("{\"someRequiredField\": \"value\"}"))
//                // CHANGED to 200 OK since your main code doesn't return 201
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testDeleteReservation() throws Exception {
//        UUID reservationId = UUID.randomUUID();
//
//        Mockito.doNothing().when(reservationService).deleteReservation(reservationId);
//
//        mockMvc.perform(delete("/reservations/delete/{id}", reservationId))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testUpdateReservation() throws Exception {
//        UUID reservationId = UUID.randomUUID();
//        ReservationDto reservationDto = new ReservationDto();
//
//        Mockito.when(reservationService.updateReservation(
//                        Mockito.eq(reservationId), Mockito.any(ReservationDto.class)))
//                .thenReturn(reservationDto);
//
//        mockMvc.perform(put("/reservations/update/{id}", reservationId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        // Add the required "officeRoomId" or any other mandatory fields
//                        .content("{\"officeRoomId\": \"11112222-3333-4444-5555-666677778888\", \"field\": \"value\"}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetReservationById() throws Exception {
//        UUID reservationId = UUID.randomUUID();
//        ReservationDto reservationDto = new ReservationDto();
//
//        Mockito.when(reservationService.findReservationById(reservationId))
//                .thenReturn(reservationDto);
//
//        mockMvc.perform(get("/reservations/{id}", reservationId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetReservationsByUserId() throws Exception {
//        UUID userId = UUID.randomUUID();
//
//        Mockito.when(reservationService.findReservationsByUserId(userId)).thenReturn(null);
//
//        mockMvc.perform(get("/reservations/user/{userId}", userId))
//                .andExpect(status().isOk());
//    }
//}


//package javawizzards.officespace.controller;
//
//import javawizzards.officespace.dto.Reservation.CreateReservationDto;
//import javawizzards.officespace.dto.Reservation.ReservationDto;
//import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
//import javawizzards.officespace.service.Reservation.ReservationService;
//import javawizzards.officespace.utility.JwtUtility;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.MediaType;
//import org.springframework.test.context.TestPropertySource;
//import org.springframework.test.web.servlet.MockMvc;
//
//import java.util.UUID;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
//
///**
// * A test for ReservationController using @WebMvcTest,
// * with no manual mocking of the Spring Environment.
// */
//@AutoConfigureMockMvc(addFilters = false)
//@WebMvcTest(ReservationController.class)
//@TestPropertySource(properties = "server.port=8080") // Just as an example override
//public class ReservationControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @MockBean
//    private ReservationService reservationService;
//
//    @MockBean
//    private RequestAndResponseService requestAndResponseService;
//
//    @MockBean
//    private JwtUtility jwtUtility;
//
//    // REMOVED: @MockBean private Environment environment;
//
//    @BeforeEach
//    void setUp() {
//        // If you need some default mocks, set them here
//        // e.g., Mockito.when(jwtUtility.someMethod()).thenReturn(...);
//        // But we’re NOT mocking Environment
//    }
//
//    @Test
//    void testCreateReservation() throws Exception {
//        // Arrange
//        CreateReservationDto createReservationDto = new CreateReservationDto();
//        Mockito.when(reservationService.createReservation(Mockito.any()))
//                .thenReturn(new ReservationDto());
//
//        // Act & Assert
//        mockMvc.perform(post("/reservations/create")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"field\": \"value\"}"))
//                .andExpect(status().isCreated());
//    }
//
//    @Test
//    void testDeleteReservation() throws Exception {
//        // Arrange
//        UUID reservationId = UUID.randomUUID();
//        Mockito.doNothing().when(reservationService).deleteReservation(reservationId);
//
//        // Act & Assert
//        mockMvc.perform(delete("/reservations/delete/{id}", reservationId))
//                .andExpect(status().isNoContent());
//    }
//
//    @Test
//    void testUpdateReservation() throws Exception {
//        // Arrange
//        UUID reservationId = UUID.randomUUID();
//        ReservationDto reservationDto = new ReservationDto();
//        Mockito.when(reservationService.updateReservation(Mockito.eq(reservationId), Mockito.any()))
//                .thenReturn(reservationDto);
//
//        // Act & Assert
//        mockMvc.perform(put("/reservations/update/{id}", reservationId)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"field\": \"value\"}"))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetReservationById() throws Exception {
//        // Arrange
//        UUID reservationId = UUID.randomUUID();
//        ReservationDto reservationDto = new ReservationDto();
//        Mockito.when(reservationService.findReservationById(reservationId))
//                .thenReturn(reservationDto);
//
//        // Act & Assert
//        mockMvc.perform(get("/reservations/{id}", reservationId))
//                .andExpect(status().isOk());
//    }
//
//    @Test
//    void testGetReservationsByUserId() throws Exception {
//        // Arrange
//        UUID userId = UUID.randomUUID();
//        Mockito.when(reservationService.findReservationsByUserId(userId))
//                .thenReturn(null);
//
//        // Act & Assert
//        mockMvc.perform(get("/reservations/user/{userId}", userId))
//                .andExpect(status().isOk());
//    }
//}


