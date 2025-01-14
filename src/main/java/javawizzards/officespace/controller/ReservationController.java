package javawizzards.officespace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import javawizzards.officespace.dto.Reservation.CreateReservationDto;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.enumerations.Reservation.ReservationMessages;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Reservation.ReservationDto;
import javawizzards.officespace.exception.Reservation.ReservationCustomException;
import javawizzards.officespace.service.GoogleCalendar.GoogleCalendarService;
import javawizzards.officespace.service.Reservation.ReservationService;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final RequestAndResponseService requestAndResponseService;
    private final GoogleCalendarService googleCalendarService;

    public ReservationController(ReservationService reservationService, RequestAndResponseService requestAndResponseService, GoogleCalendarService googleCalendarService) {
        this.reservationService = reservationService;
        this.requestAndResponseService = requestAndResponseService;
        this.googleCalendarService = googleCalendarService;
    }
    @PostMapping("/create")
    public ResponseEntity<Response<String>> createReservation(
            @RequestBody @Valid Request<CreateReservationDto> request,
            BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            ReservationDto createdReservation = reservationService.createReservation(request.getData());

            Response<String> response;
            if (createdReservation == null) {
                response = new Response<>(ReservationMessages.RESERVATION_FAILED.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            googleCalendarService.createEvent(request.getData());

            response = new Response<>(HttpStatus.CREATED, ReservationMessages.RESERVATION_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (ReservationCustomException e) {
            Response<String> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<String> errorResponse = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<Void>> deleteReservation(
            @PathVariable UUID id) throws JsonProcessingException {

        Request<UUID> request = new Request<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(id);

        try {
            reservationService.deleteReservation(id);
            Response<Void> response = new Response<>(null, HttpStatus.NO_CONTENT, ReservationMessages.RESERVATION_DELETE_SUCCESS.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (ReservationCustomException e) {
            Response<Void> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<Void> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Response<ReservationDto>> updateReservation(
            @PathVariable UUID id,
            @RequestBody @Valid ReservationDto reservationDto,
            BindingResult bindingResult) throws JsonProcessingException {

        Request<ReservationDto> request = new Request<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(reservationDto);

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            ReservationDto updatedReservation = reservationService.updateReservation(id, reservationDto);
            Response<ReservationDto> response = new Response<>(updatedReservation, HttpStatus.OK, ReservationMessages.RESERVATION_UPDATE_SUCCESS.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (ReservationCustomException e) {
            Response<ReservationDto> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<ReservationDto> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ReservationDto>> getReservationById(
            @PathVariable UUID id) throws JsonProcessingException {

        Request<UUID> request = new Request<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(id);

        try {
            ReservationDto reservation = reservationService.findReservationById(id);
            Response<ReservationDto> response = new Response<>(reservation, HttpStatus.OK, "Reservation fetched successfully");
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (ReservationCustomException e) {
            Response<ReservationDto> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<ReservationDto> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/office-room/{officeRoomId}")
    public ResponseEntity<Response<List<ReservationDto>>> getReservationsByOfficeRoomId(
            @PathVariable UUID officeRoomId) throws JsonProcessingException {

        Request<UUID> request = new Request<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(officeRoomId);

        try {
            List<ReservationDto> reservations = reservationService.findReservationsByOfficeRoomId(officeRoomId);
            Response<List<ReservationDto>> response = new Response<>(reservations, HttpStatus.OK, "Reservations fetched successfully");
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (ReservationCustomException e) {
            Response<List<ReservationDto>> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<List<ReservationDto>> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<List<ReservationDto>>> getReservationsByUserId(
            @PathVariable UUID userId) throws JsonProcessingException {

        Request<String> request = new Request<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(userId.toString());

        try {
            List<ReservationDto> reservations = reservationService.findReservationsByUserId(userId);
            Response<List<ReservationDto>> response = new Response<>(reservations, HttpStatus.OK, "Reservations fetched successfully");
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (ReservationCustomException e) {
            Response<List<ReservationDto>> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<List<ReservationDto>> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/get-statuses")
    public ResponseEntity<Response<List<String>>> getOfficeRoomStatuses() throws JsonProcessingException {
        Request<Void> request = new Request<>();
        request.setRequestId(UUID.randomUUID().toString());

        try {
            List<String> statuses = this.reservationService.getReservationStatusList();
            Response<List<String>> response = new Response<>(statuses, HttpStatus.OK, "Reservation statuses fetched successfully");
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (ReservationCustomException e) {
            Response<List<String>> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<List<String>> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}
