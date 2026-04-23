package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundMapper implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        return Response.status(422)
                .entity(Map.of(
                    "error",   "Unprocessable Entity",
                    "message", ex.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON).build();
    }
}