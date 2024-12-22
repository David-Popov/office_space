package javawizzards.officespace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import javawizzards.officespace.config.TestSecurityConfig;
import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.Reservation.ReservationService;
import javawizzards.officespace.utility.JwtUtility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Import(TestSecurityConfig.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservationService reservationService;

    @MockBean
    private RequestAndResponseService requestAndResponseService;

    @MockBean
    private JwtUtility jwtUtility;

    private CreateReservationDto createReservationDto;
    private ReservationDto reservationDto;

    @BeforeEach
    void setUp() {
        createReservationDto = new CreateReservationDto();
        createReservationDto.setUserId(UUID.randomUUID());
        createReservationDto.setStartDateTime(LocalDateTime.parse("2023-12-22T10:00:00"));
        createReservationDto.setEndDateTime(LocalDateTime.parse("2023-12-22T12:00:00"));

        reservationDto = new ReservationDto();
        reservationDto.setId(UUID.randomUUID());
        reservationDto.setUserId(createReservationDto.getUserId());
        reservationDto.setStartDateTime(createReservationDto.getStartDateTime());
        reservationDto.setEndDateTime(createReservationDto.getEndDateTime());

        // Mock JwtUtility behavior
        when(jwtUtility.validateToken(anyString())).thenReturn(true);
        when(jwtUtility.extractUsername(anyString())).thenReturn("test-user");
    }

    @Test
    void testCreateReservationSuccess() throws Exception {
        when(reservationService.createReservation(any(CreateReservationDto.class))).thenReturn(reservationDto);

        mockMvc.perform(post("/reservations")
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReservationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationDto.getId().toString()))
                .andExpect(jsonPath("$.userId").value(reservationDto.getUserId().toString()))
                .andExpect(jsonPath("$.startDateTime").value(reservationDto.getStartDateTime().toString()))
                .andExpect(jsonPath("$.endDateTime").value(reservationDto.getEndDateTime().toString()));

        verify(reservationService, times(1)).createReservation(any(CreateReservationDto.class));
    }

    @Test
    void testCreateReservationValidationError() throws Exception {
        createReservationDto.setStartDateTime(null); // Invalid data

        mockMvc.perform(post("/reservations")
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createReservationDto)))
                .andExpect(status().isBadRequest());

        verify(reservationService, times(0)).createReservation(any(CreateReservationDto.class));
    }

    @Test
    void testFindReservationByIdSuccess() throws Exception {
        when(reservationService.findReservationById(any(UUID.class))).thenReturn(reservationDto);

        mockMvc.perform(get("/reservations/{id}", reservationDto.getId())
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(reservationDto.getId().toString()))
                .andExpect(jsonPath("$.userId").value(reservationDto.getUserId().toString()))
                .andExpect(jsonPath("$.startDateTime").value(reservationDto.getStartDateTime().toString()))
                .andExpect(jsonPath("$.endDateTime").value(reservationDto.getEndDateTime().toString()));

        verify(reservationService, times(1)).findReservationById(any(UUID.class));
    }

    @Test
    void testFindReservationByIdNotFound() throws Exception {
        when(reservationService.findReservationById(any(UUID.class))).thenThrow(new RuntimeException("Reservation not found"));

        mockMvc.perform(get("/reservations/{id}", UUID.randomUUID())
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).findReservationById(any(UUID.class));
    }

    @Test
    void testDeleteReservationSuccess() throws Exception {
        doNothing().when(reservationService).deleteReservation(any(UUID.class));

        mockMvc.perform(delete("/reservations/{id}", UUID.randomUUID())
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteReservation(any(UUID.class));
    }

    @Test
    void testDeleteReservationNotFound() throws Exception {
        doThrow(new RuntimeException("Reservation not found")).when(reservationService).deleteReservation(any(UUID.class));

        mockMvc.perform(delete("/reservations/{id}", UUID.randomUUID())
                        .header("Authorization", "Bearer mock-token")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(reservationService, times(1)).deleteReservation(any(UUID.class));
    }
}
