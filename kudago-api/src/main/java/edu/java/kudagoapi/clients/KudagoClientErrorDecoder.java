package edu.java.kudagoapi.clients;

import edu.java.kudagoapi.exceptions.ApiException;
import feign.Response;
import feign.codec.ErrorDecoder;

public class KudagoClientErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        int status = response.status();
        String message = String.format("%d: exception with body: %s is thrown by: %s",
                status,
                response.body().toString(),
                response.request().url());
        return new ApiException(message, status);
    }
}
