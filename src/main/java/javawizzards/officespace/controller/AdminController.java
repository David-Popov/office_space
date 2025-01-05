package javawizzards.officespace.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import javawizzards.officespace.dto.Company.CompanyDto;
import javawizzards.officespace.dto.Company.UpdateCompanyRequest;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.dto.User.GoogleUserDto;
import javawizzards.officespace.dto.User.UpdateUserRequest;
import javawizzards.officespace.enumerations.Company.CompanyMessages;
import javawizzards.officespace.enumerations.User.UserMessages;
import javawizzards.officespace.exception.Company.CompanyCustomException;
import javawizzards.officespace.service.Company.CompanyService;
import javawizzards.officespace.service.RequestAndResponse.RequestAndResponseService;
import javawizzards.officespace.service.Reservation.ReservationService;
import javawizzards.officespace.service.User.UserService;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;
import java.util.UUID;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final CompanyService companyService;
    private final ReservationService reservationService;
    private final RequestAndResponseService requestAndResponseService;

    public AdminController(UserService userService, CompanyService companyService, ReservationService reservationService, RequestAndResponseService requestAndResponseService) {
        this.userService = userService;
        this.companyService = companyService;
        this.reservationService = reservationService;
        this.requestAndResponseService = requestAndResponseService;
    }

    @DeleteMapping("/delete-user/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable UUID id) {
        Response response = null;
        try{
            if (id == null || id.toString().isEmpty()){
                response = new Response(HttpStatus.BAD_REQUEST, "Invalid Id");
                this.requestAndResponseService.CreateRequestAndResponse(id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            var data = this.userService.deleteUser(id);

            if (data == null){
                response = new Response(HttpStatus.BAD_REQUEST, UserMessages.USER_NOT_FOUND.getMessage());
                this.requestAndResponseService.CreateRequestAndResponse(id, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }

            response = new Response(HttpStatus.OK, UserMessages.USER_DELETED.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(response);
        } catch (Exception e) {
            var errorResponse = new Response<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR, e.getStackTrace().toString());
            this.requestAndResponseService.CreateRequestAndResponse(id, errorResponse, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(errorResponse);
        }
    }

    @PutMapping("/update-user/{id}")
    public ResponseEntity<Response<?>> updateUser(@PathVariable UUID id, @RequestBody @Valid Request<UpdateUserRequest> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try{
            var data = this.userService.updateUser(id, request.getData());
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/update-google-user")
    public ResponseEntity<Response<?>> updateGoogleUser(@RequestBody @Valid Request<GoogleUserDto> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try{
            var data = this.userService.updateGoogleUser(request.getData());
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("get-users")
    public ResponseEntity<Response<?>> getAllUsers() {
        Response<?> response;
        String requestId = UUID.randomUUID().toString();

        try{
            var data = this.userService.getUsers();
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
//            this.requestAndResponseService.CreateRequestAndResponse(requestId, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(requestId, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("get-companies")
    public ResponseEntity<Response<?>> getAllCompanies() {
        Response<?> response;

        try{
            var data = this.companyService.getAllCompanies();
            response = new Response<>(data, HttpStatus.OK, "Companies fetch successful");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("get-company/{id}")
    public ResponseEntity<Response<?>> getCompany(@PathVariable UUID id) {
        Response<?> response;

        try{
            CompanyDto data = this.companyService.findCompanyById(id);

            if (data == null){
                response = new Response<>(new CompanyCustomException.CompanyNotFoundException().toString());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            response = new Response<>(data, HttpStatus.OK, "Company fetch successful");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @PutMapping("/update-company/{id}")
    public ResponseEntity<Response<?>> updateCompany(@PathVariable UUID id, @RequestBody @Valid Request<UpdateCompanyRequest> request, BindingResult bindingResult) throws JsonProcessingException {
        Response<?> response;

        if (bindingResult.hasErrors()) {
            String errorMessage = Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage();
            return ResponseEntity.badRequest().body(new Response<>(errorMessage));
        }

        try{
            var data = this.companyService.updateCompany(id, request.getData());
            response = new Response<>(data, HttpStatus.OK, UserMessages.USER_UPDATE_SUCCESS.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            this.requestAndResponseService.CreateRequestAndResponse(request, response, LoggingUtils.logControllerName(this), LoggingUtils.logMethodName());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @DeleteMapping("/delete-company/{id}")
    public ResponseEntity<Response<?>> deleteCompany(@PathVariable UUID id) {
        Response<?> response;
        try{
            if (id.toString().isEmpty()){
                response = new Response<>(new CompanyCustomException.InvalidCompanyId().toString());
                return ResponseEntity.badRequest().body(response);
            }

            this.companyService.deleteCompany(id);

            response = new Response<>(null, HttpStatus.OK, CompanyMessages.COMPANY_DELETED_SUCCESSFULLY.getMessage());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }

    @GetMapping("get-reservations")
    public ResponseEntity<Response<?>> getAllReservations() {
        Response<?> response;

        try{
            var data = this.reservationService.getAllReservations();
            response = new Response<>(data, HttpStatus.OK, "Reservation fetch successful");
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response = new Response<>(e.getMessage());
            return ResponseEntity.internalServerError().body(response);
        }
    }
}
