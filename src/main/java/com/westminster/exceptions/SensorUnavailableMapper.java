package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;

@Provider
public class SensorUnavailableMapper implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        return Response.status(403)
                .entity(Map.of(
                    "error",   "Forbidden",
                    "message", ex.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON).build();
    }
}