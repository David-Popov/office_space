package javawizzards.officespace.service.RequestAndResponse;

import javawizzards.officespace.dto.Request.Request;
import javawizzards.officespace.dto.Response.Response;
import javawizzards.officespace.entity.RequestAndResponse;

import java.util.List;

public interface RequestAndResponseService {
    void CreateRequestAndResponse(Request<?> request, Response<?> response, String controllerName ,String methodName);
    <T> void CreateRequestAndResponse(T data, Response<?> response, String controllerName ,String methodName);
    List<RequestAndResponse> GetRequestsAndResponses();
    RequestAndResponse FindByRequestId(String requestId);
    RequestAndResponse FindByResponseId(String responseId);
}
