package com.westminster.exceptions;

import jakarta.ws.rs.core.*;
import jakarta.ws.rs.ext.*;
import java.util.Map;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        return Response.status(409)
                .entity(Map.of(
                    "error",   "Conflict",
                    "message", ex.getMessage()
                ))
                .type(MediaType.APPLICATION_JSON).build();
    }
}