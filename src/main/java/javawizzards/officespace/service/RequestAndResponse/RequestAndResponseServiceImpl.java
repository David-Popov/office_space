package javawizzards.officespace.service.RequestAndResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.entity.RequestAndResponse;
import javawizzards.officespace.repository.RequestAndResponseRepository;
import javawizzards.officespace.utility.LoggingUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Service
public class RequestAndResponseServiceImpl implements RequestAndResponseService{

    private final RequestAndResponseRepository requestAndResponseRepository;
    private final ObjectMapper objectMapper;
    private final Validator validator;

    public RequestAndResponseServiceImpl(RequestAndResponseRepository requestAndResponseRepository, ObjectMapper objectMapper, Validator validator) {
        this.requestAndResponseRepository = requestAndResponseRepository;
        this.objectMapper = objectMapper;
        this.validator = validator;
    }

    @Override
    public void CreateRequestAndResponse(Request<?> request, Response<?> response, String controllerName ,String methodName) {
        try{
            RequestAndResponse requestAndResponse = new RequestAndResponse();
            requestAndResponse.setDate(LocalDateTime.now());
            requestAndResponse.setControllerName(controllerName);
            requestAndResponse.setMethodName(methodName);
            requestAndResponse.setRequestId(request.getRequestId());
            requestAndResponse.setRequestData(this.objectMapper.writeValueAsString(request.getData()));
            requestAndResponse.setResponseId(response.getResponseId());
            requestAndResponse.setResponseData(this.objectMapper.writeValueAsString(response.getData()));
            requestAndResponse.setResponseData(response.getErrorDescription());


            this.requestAndResponseRepository.save(requestAndResponse);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<RequestAndResponse> GetRequestsAndResponses() {
        try{
            return this.requestAndResponseRepository.findAll();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestAndResponse FindByRequestId(String requestId) {
        try{
            if (requestId.isEmpty()){
                return null;
            }

            return this.requestAndResponseRepository.findByRequestId(requestId).orElse(null);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public RequestAndResponse FindByResponseId(String responseId) {
        try{
            if (responseId.isEmpty()){
                return null;
            }

            return this.requestAndResponseRepository.findByResponseId(responseId).orElse(null);
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
