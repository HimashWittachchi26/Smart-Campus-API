package com.westminster.filters;

import jakarta.ws.rs.container.*;
import jakarta.ws.rs.ext.Provider;
import java.util.logging.Logger;

@Provider
public class CustomLogger implements ContainerRequestFilter, ContainerResponseFilter {

    private static final Logger LOG = Logger.getLogger(CustomLogger.class.getName());

    @Override
    public void filter(ContainerRequestContext req) {
        LOG.info("Incoming request:  " + req.getMethod() + " " + req.getUriInfo().getRequestUri());
    }

    @Override
    public void filter(ContainerRequestContext req, ContainerResponseContext res) {
        LOG.info("Outgoing response: HTTP " + res.getStatus());
    }
}