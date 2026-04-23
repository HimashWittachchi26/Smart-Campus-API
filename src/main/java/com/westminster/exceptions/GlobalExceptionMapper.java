package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;
import java.util.logging.Logger;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOG = Logger.getLogger(GlobalExceptionMapper.class.getName());

    @Override
    public Response toResponse(Throwable ex) {
        LOG.severe("Unexpected server error: " + ex.getClass().getName() + " - " + ex.getMessage());
        return Response.status(500)
                .entity(Map.of(
                    "error",   "Internal Server Error",
                    "message", "An unexpected error occurred. Please try again later."
                ))
                .type(MediaType.APPLICATION_JSON).build();
    }
}