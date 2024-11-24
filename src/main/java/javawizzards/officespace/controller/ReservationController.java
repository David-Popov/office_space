package javawizzards.officespace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.enumerations.Reservation.ReservationMessages;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Reservation.ReservationDto;
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

    public ReservationController(ReservationService reservationService, RequestAndResponseService requestAndResponseService) {
        this.reservationService = reservationService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @PostMapping("/create")
    public ResponseEntity<Response<String>> createReservation(
        @RequestBody Request<ReservationDto> request, 
        BindingResult bindingResult) throws JsonProcessingException {
        // 1) Валидиране на заявката 
        //->проверява се за грешки в данните, подадени в заявката
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }
    
        try {
            //2) Създаване на резервацията чрез reservatioService
            //->извиква се методът за създаване на резервация с подадените данни
            ReservationDto createdReservation = reservationService.createReservation(request.getData());
    
            //3) Проверка дали създаването на резервацията е успешно
            Response<String> response;
            if (createdReservation == null) {
                response = new Response<>(ReservationMessages.RESERVATION_FAILED.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);  
            }
    
            //4) Връщане на успешния отговор със съответния статус и съобщение
            response = new Response<>(HttpStatus.CREATED, ReservationMessages.RESERVATION_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
    
            return ResponseEntity.ok(response);
    
        } catch (Exception e) {
            Response<String> errorResponse = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<Void>> deleteReservation(
            @PathVariable UUID id) throws JsonProcessingException{
        try {
            reservationService.deleteReservation(id);
            Response<Void> response = new Response<>(null, HttpStatus.NO_CONTENT, "Reservation deleted successfully");

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (Exception e) {
            Response<Void> errorResponse = new Response<>(e.getMessage());

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);

            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PutMapping("/update/{id}")
    public ResponseEntity<Response<ReservationDto>> updateReservation(
            @PathVariable UUID id,
            @RequestBody ReservationDto reservationDto,
            BindingResult bindingResult) throws JsonProcessingException {

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            ReservationDto updatedReservation = reservationService.updateReservation(id, reservationDto);
            Response<ReservationDto> response = new Response<>(updatedReservation, HttpStatus.OK, "Reservation updated successfully");

            Request<ReservationDto> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(reservationDto);

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<ReservationDto> errorResponse = new Response<>(e.getMessage());

            Request<ReservationDto> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(reservationDto);

            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<ReservationDto>> getReservationById(
            @PathVariable UUID id) throws JsonProcessingException{
        try {
            ReservationDto reservation = reservationService.findReservationById(id);
            Response<ReservationDto> response = new Response<>(reservation, HttpStatus.OK, "Reservation fetched successfully");

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<ReservationDto> errorResponse = new Response<>(e.getMessage());

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);

            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/office-room/{officeRoomId}")
    public ResponseEntity<Response<List<ReservationDto>>> getReservationsByOfficeRoomId(
            @PathVariable UUID officeRoomId) throws JsonProcessingException{
        try {
            List<ReservationDto> reservations = reservationService.findReservationsByOfficeRoomId(officeRoomId);
            Response<List<ReservationDto>> response = new Response<>(reservations, HttpStatus.OK, "Reservations fetched successfully");

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(officeRoomId);

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<List<ReservationDto>> errorResponse = new Response<>(e.getMessage());

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(officeRoomId);

            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<Response<List<ReservationDto>>> getReservationsByUserId(
            @PathVariable UUID userId) throws JsonProcessingException {
        try {
            List<ReservationDto> reservations = reservationService.findReservationsByUserId(userId);
    
            Response<List<ReservationDto>> response = new Response<>(reservations, HttpStatus.OK, "Reservations fetched successfully");
    
            Request<String> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(userId.toString()); 
    
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            Response<List<ReservationDto>> errorResponse = new Response<>(e.getMessage());
    
            Request<String> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(userId.toString()); 
    
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
}
