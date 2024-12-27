package javawizzards.officespace.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import jakarta.validation.Valid;
import javawizzards.officespace.dto.OfficeRoom.CreateOfficeRoomDto;
import javawizzards.officespace.dto.OfficeRoom.UpdateOfficeRoomRequest;
import javawizzards.officespace.exception.OfficeRoom.OfficeRoomCustomException;
import javawizzards.officespace.exception.Resource.ResourceCustomException;
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
@RequestMapping("/office-rooms")
public class OfficeRoomController {
    private final OfficeRoomService officeRoomService;
    private final RequestAndResponseService requestAndResponseService;

    public OfficeRoomController(OfficeRoomService officeRoomService, RequestAndResponseService requestAndResponseService) {
        this.officeRoomService = officeRoomService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @GetMapping
    @ResponseBody
    public ResponseEntity<Response<?>> getAll() {
        Response<?> response;
        String requestId = UUID.randomUUID().toString();

        try {
            var data = this.officeRoomService.getOfficeRooms();
            response = new Response<>(data, HttpStatus.OK, OfficeRoomMessages.OFFICE_ROOMS_FETCH_SUCCESS.getMessage());
            return ResponseEntity.ok(response);
        } catch (OfficeRoomCustomException e) {
            response = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, OfficeRoomMessages.CUSTOM_ERROR.getMessage());
            return ResponseEntity.badRequest().body(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PostMapping("/create")
    public ResponseEntity<Response<String>> createOfficeRoom(
            @RequestBody @Valid Request<CreateOfficeRoomDto> request,
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
        } catch (OfficeRoomCustomException e) {
            return ResponseEntity.badRequest().body(new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, OfficeRoomMessages.CUSTOM_ERROR.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Response<Void>> deleteOfficeRoom(
            @PathVariable UUID id) throws JsonProcessingException {
        try {
            officeRoomService.deleteOfficeRoom(id);
            Response<Void> response = new Response<>(null, HttpStatus.NO_CONTENT, OfficeRoomMessages.OFFICE_ROOM_DELETE_SUCCESS.getMessage());

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(response);
        } catch (OfficeRoomCustomException e) {
            Response<Void> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, OfficeRoomMessages.CUSTOM_ERROR.getMessage());
            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
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
    public ResponseEntity<Response<OfficeRoomDto>> updateOfficeRoom(@PathVariable UUID id, @RequestBody @Valid Request<UpdateOfficeRoomRequest> request, BindingResult bindingResult) throws JsonProcessingException {
        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try {
            OfficeRoomDto updatedOfficeRoom = officeRoomService.updateOfficeRoom(id, request.getData());
            Response<OfficeRoomDto> response = new Response<>(updatedOfficeRoom, HttpStatus.OK, OfficeRoomMessages.OFFICE_ROOM_UPDATE_SUCCESS.getMessage());

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (OfficeRoomCustomException e) {
            Response<OfficeRoomDto> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, OfficeRoomMessages.CUSTOM_ERROR.getMessage());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Response<OfficeRoomDto>> getOfficeRoomById(@PathVariable UUID id) throws JsonProcessingException {
        try {
            OfficeRoomDto officeRoom = officeRoomService.findOfficeRoomById(id);
            Response<OfficeRoomDto> response = new Response<>(officeRoom, HttpStatus.OK, OfficeRoomMessages.OFFICE_ROOM_FETCH_SUCCESS.getMessage());

            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (OfficeRoomCustomException e) {
            Response<OfficeRoomDto> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, OfficeRoomMessages.CUSTOM_ERROR.getMessage());
            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(id);
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
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
        @RequestParam(name = "capacity", required = false) Integer capacity,
        @RequestParam(name = "minPrice", required = false) BigDecimal minPrice,
        @RequestParam(name = "maxPrice", required = false) BigDecimal maxPrice) {
    try {
        List<OfficeRoomDto> filteredOfficeRooms = officeRoomService.filterOfficeRooms(
                name, building, floor, type, capacity, minPrice, maxPrice);
        Response<List<OfficeRoomDto>> response = new Response<>(filteredOfficeRooms, HttpStatus.OK, OfficeRoomMessages.OFFICE_ROOMS_FETCH_SUCCESS.getMessage());
        return ResponseEntity.ok(response);
    } catch (OfficeRoomCustomException e) {
        return ResponseEntity.badRequest().body(new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, OfficeRoomMessages.CUSTOM_ERROR.getMessage()));
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

            Response<List<OfficeRoomDto>> response = new Response<>(availableRooms, HttpStatus.OK, OfficeRoomMessages.OFFICE_ROOMS_FETCH_SUCCESS.getMessage());
            Request<UUID> request = new Request<>();

            request.setRequestId(UUID.randomUUID().toString());
            request.setData(UUID.randomUUID());

            requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);
        } catch (OfficeRoomCustomException e) {
            Response<List<OfficeRoomDto>> errorResponse = new Response<>(e.getMessage(), HttpStatus.BAD_REQUEST, OfficeRoomMessages.CUSTOM_ERROR.getMessage());
            Request<UUID> request = new Request<>();
            request.setRequestId(UUID.randomUUID().toString());
            request.setData(UUID.randomUUID());
            requestAndResponseService.CreateRequestAndResponse(request, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(e.getMessage()));
        }
    }

    @PostMapping("/{officeRoomId}/resources/{resourceId}")
    public ResponseEntity<Response<OfficeRoomDto>> addResource(
            @PathVariable UUID officeRoomId,
            @PathVariable UUID resourceId) {
        try {
            OfficeRoomDto updatedRoom = officeRoomService.addResourceToRoom(officeRoomId, resourceId);
            return ResponseEntity.ok(new Response<>(
                    updatedRoom,
                    HttpStatus.OK,
                    OfficeRoomMessages.RESOURCE_ADDED_SUCCESS.getMessage()
            ));
        } catch (OfficeRoomCustomException | ResourceCustomException e) {
            return ResponseEntity.badRequest().body(new Response<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    OfficeRoomMessages.CUSTOM_ERROR.getMessage()
            ));
        }
    }

    @PostMapping("/{officeRoomId}/resources")
    public ResponseEntity<Response<OfficeRoomDto>> addResources(
            @PathVariable UUID officeRoomId,
            @RequestBody @Valid Request<List<UUID>> resourceIds) {
        try {
            OfficeRoomDto updatedRoom = officeRoomService.addResourcesToRoom(officeRoomId, resourceIds.getData());
            return ResponseEntity.ok(new Response<>(
                    updatedRoom,
                    HttpStatus.OK,
                    OfficeRoomMessages.RESOURCES_ADDED_SUCCESS.getMessage()
            ));
        } catch (OfficeRoomCustomException | ResourceCustomException e) {
            return ResponseEntity.badRequest().body(new Response<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    OfficeRoomMessages.CUSTOM_ERROR.getMessage()
            ));
        }
    }

    @DeleteMapping("/{officeRoomId}/resources/{resourceId}")
    public ResponseEntity<Response<OfficeRoomDto>> removeResource(
            @PathVariable UUID officeRoomId,
            @PathVariable UUID resourceId) {
        try {
            OfficeRoomDto updatedRoom = officeRoomService.removeResourceFromRoom(officeRoomId, resourceId);
            return ResponseEntity.ok(new Response<>(
                    updatedRoom,
                    HttpStatus.OK,
                    OfficeRoomMessages.RESOURCE_REMOVED_SUCCESS.getMessage()
            ));
        } catch (OfficeRoomCustomException | ResourceCustomException e) {
            return ResponseEntity.badRequest().body(new Response<>(
                    null,
                    HttpStatus.BAD_REQUEST,
                    e.getMessage()
            ));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(new Response<>(
                    null,
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    OfficeRoomMessages.CUSTOM_ERROR.getMessage()
            ));
        }
    }

    @GetMapping("/get-statuses")
    public ResponseEntity<List<String>> getOfficeRoomStatuses() {
            List<String> officeStatuses = this.officeRoomService.getOfficeRoomStatusList();
            return ResponseEntity.ok(officeStatuses);
    }

    @GetMapping("/get-types")
    public ResponseEntity<List<String>> getOfficeRoomTypes() {
        List<String> officeTypes = this.officeRoomService.getOfficeRoomTypeList();
        return ResponseEntity.ok(officeTypes);
    }
}
