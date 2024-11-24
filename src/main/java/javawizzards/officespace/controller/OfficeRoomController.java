package javawizzards.officespace.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import javawizzards.officespace.enumerations.User.UserMessages;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;

import com.fasterxml.jackson.core.JsonProcessingException;

import org.springframework.web.bind.annotation.*;
import javawizzards.officespace.dto.OfficeRoom.OfficeRoomDto;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.enumerations.OfficeRoom.OfficeRoomMessages;
import javawizzards.officespace.service.OfficeRoom.OfficeRoomService;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.utility.LoggingUtils;


@RestController
@RequestMapping("/officerooms")
public class OfficeRoomController {
    private final OfficeRoomService officeRoomService;
    private final RequestAndResponseService requestAndResponseService;

    public OfficeRoomController(OfficeRoomService officeRoomService, RequestAndResponseService requestAndResponseService) {
        this.officeRoomService = officeRoomService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @GetMapping("/get-all")
    public ResponseEntity<Response<?>> getAll() {
        Response<?> response;

        try{
            var data = this.officeRoomService.getOfficeRooms();
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse("", response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse("", response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Response<String>> createOfficeRoom(
        @RequestBody Request<OfficeRoomDto> request, 
        BindingResult bindingResult) throws JsonProcessingException {
            if (bindingResult.hasErrors()) {
                String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
                return ResponseEntity.badRequest().body(new Response<>(errorMessage));
            }
    
        try {
            OfficeRoomDto createdOfficeRoom = officeRoomService.createOfficeRoom(request.getData());
            if (createdOfficeRoom == null) {
                return ResponseEntity.badRequest().body(new Response<>(OfficeRoomMessages.OFFICE_ROOM_CREATION_FAILED.getMessage()));
            }
    
            return ResponseEntity.ok(new Response<>(HttpStatus.CREATED, OfficeRoomMessages.OFFICE_ROOM_CREATION_SUCCESS.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }
    

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<Void>> deleteOfficeRoom(
            @PathVariable UUID id) throws JsonProcessingException {
        try {
            officeRoomService.deleteOfficeRoom(id);
            Response<Void> response = new Response<>(null, HttpStatus.NO_CONTENT, "Office room deleted successfully");

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
    public ResponseEntity<Response<OfficeRoomDto>> updateOfficeRoom(
            @PathVariable UUID id,
            @RequestBody OfficeRoomDto officeRoomDto,
            BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            OfficeRoomDto updatedOfficeRoom = officeRoomService.updateOfficeRoom(id, officeRoomDto);
            Response<OfficeRoomDto> response = new Response<>(updatedOfficeRoom, HttpStatus.OK, "Office room updated successfully");

            Request<OfficeRoomDto> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(officeRoomDto);

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OfficeRoomDto>> getOfficeRoomById(
            @PathVariable UUID id) throws JsonProcessingException {
                try {
                    OfficeRoomDto officeRoom = officeRoomService.findOfficeRoomById(id);
                    Response<OfficeRoomDto> response = new Response<>(officeRoom, HttpStatus.OK, "Office room fetched successfully");
        
                    Request<UUID> request = new Request<>();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setData(id);
        
                    requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                    return ResponseEntity.ok(response);
                } catch (Exception e) {
                    Response<OfficeRoomDto> errorResponse = new Response<>(e.getMessage());
        
                    Request<UUID> request = new Request<>();
                    request.setRequestId(UUID.randomUUID().toString());
                    request.setData(id);
        
                    requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                    return ResponseEntity.internalServerError().body(errorResponse);
                }
    }

    @GetMapping("/filter")
    public ResponseEntity<Response<List<OfficeRoomDto>>> filterOfficeRooms(
        @RequestParam(name = "name", required = false) String name,
        @RequestParam(name = "building", required = false) String building,
        @RequestParam(name = "floor", required = false) String floor,
        @RequestParam(name = "type", required = false) String type,
        @RequestParam(name = "capacity", required = false) Integer capacity) {
        try {

            List<OfficeRoomDto> filteredOfficeRooms = officeRoomService.filterOfficeRooms(name, building, floor, type, capacity);
            Response<List<OfficeRoomDto>> response = new Response<>(filteredOfficeRooms, HttpStatus.OK, "Office rooms fetched successfully");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

    @GetMapping("/availability")
    public ResponseEntity<Response<List<OfficeRoomDto>>> findAvailableRooms(
        @RequestParam(name = "startDateTime", required = false) LocalDateTime startDateTime,
        @RequestParam(name = "endDateTime", required = false) LocalDateTime endDateTime) throws JsonProcessingException {
        try {
            List<OfficeRoomDto> availableRooms = officeRoomService.findAvailableRooms(startDateTime, endDateTime);

            Response<List<OfficeRoomDto>> response = new Response<>(availableRooms, HttpStatus.OK, "Office rooms fetched successfully");
            Request<UUID> request = new Request<>();

            request.setRequestId(UUID.randomUUID().toString());
            request.setData(UUID.randomUUID());

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

}
