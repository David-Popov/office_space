package javawizzards.officespace.controller;

import javawizzards.officespace.entity.Ticket;
import javawizzards.officespace.entity.User;
import javawizzards.officespace.enumerations.Notification.NotificationType;
import javawizzards.officespace.enumerations.Reservation.ReservationMessages;
import javawizzards.officespace.enumerations.Ticket.TicketMessages;
import javawizzards.officespace.enumerations.Ticket.TicketStatus;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.Ticket.TicketDto;
import javawizzards.officespace.exception.Reservation.ReservationCustomException;
import javawizzards.officespace.exception.Ticket.TicketCustomException;
import javawizzards.officespace.repository.DepartmentRepository;
import javawizzards.officespace.repository.UserRepository;
import javawizzards.officespace.service.Notification.NotificationService;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.Ticket.TicketService;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.modelmapper.ModelMapper;

import jakarta.validation.Valid;

import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    private final TicketService ticketService;
    private final NotificationService notificationService;
    private final RequestAndResponseService requestAndResponseService;
    private final DepartmentRepository departmentRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    public TicketController(TicketService ticketService, NotificationService notificationService,RequestAndResponseService requestAndResponseService, DepartmentRepository departmentRepository, ModelMapper modelMapper, UserRepository userRepository) {
        this.ticketService = ticketService;
        this.notificationService = notificationService;
        this.requestAndResponseService = requestAndResponseService;
        this.departmentRepository = departmentRepository;
        this.modelMapper = modelMapper;
        this.userRepository = userRepository;
    }

    @PostMapping("/create")
    public ResponseEntity<Response<String>> createTicket(
            @RequestBody @Valid Request<TicketDto> request,
            BindingResult bindingResult) throws JsonProcessingException {
    
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }
    
        try {
            TicketDto createdTicket = ticketService.createTicket(request.getData());
    
            Response<String> response;

            if (createdTicket == null) {
                response = new Response<>(TicketMessages.TICKET_CREATION_FAILED.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.badRequest().body(response);
            }

            response = new Response<>(HttpStatus.CREATED, ReservationMessages.RESERVATION_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
    
        } catch (TicketCustomException e) {
            Response<String> errorResponse = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<String> errorResponse = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }
    
    @PatchMapping("update-status/{id}/status")
    public ResponseEntity<Response<String>> updateTicketStatus(@PathVariable UUID id, @RequestParam TicketStatus newStatus) {
        try {
            if (id.toString().isEmpty() || newStatus.toString().isEmpty() || newStatus == null) {
                return ResponseEntity.badRequest().body(new Response<>("Id or new status can not be null"));
            }

            ticketService.changeTicketStatus(id, newStatus);

            String message = "Ticket status updated successfully to: " + newStatus;
            Response<String> response = new Response<>(message, HttpStatus.OK, "Operation completed successfully.");
            return ResponseEntity.ok(response);

        } catch (TicketCustomException e) {
            return ResponseEntity.badRequest().body(
                new Response<>("Failed to update ticket status: " + e.getMessage(), HttpStatus.BAD_REQUEST, "Operation failed."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                new Response<>("An internal server error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, "Operation failed."));
    }
}

    @GetMapping("/all/{userId}")
    public ResponseEntity<Response<List<TicketDto>>> getAllTicketsOfUser(@PathVariable UUID userId) {
        try {
            List<TicketDto> ticketDtos = ticketService.getAllTicketsOfUser(userId);
            Response<List<TicketDto>> response = new Response<>(ticketDtos, HttpStatus.OK, "Tickets retrieved successfully.");
            return ResponseEntity.ok(response);
        } catch (TicketCustomException e) {
            return ResponseEntity.badRequest().body(new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, "Failed to retrieve tickets."));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<Void>> deleteTicket(
            @PathVariable UUID id) throws JsonProcessingException {

        Request<UUID> request = new Request<>();
        request.setRequestId(UUID.randomUUID().toString());
        request.setData(id);

        try {
            ticketService.deleteTicket(id);
            Response<Void> response = new Response<>(null, HttpStatus.NO_CONTENT, TicketMessages.TICKET_DELETE_SUCCESS.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (TicketCustomException e) {
            Response<Void> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            Response<Void> errorResponse = new Response<>(e.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

}
