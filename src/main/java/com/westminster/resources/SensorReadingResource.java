package com.westminster.resources;

import com.westminster.exceptions.SensorUnavailableException;
import com.westminster.models.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;

public class SensorReadingResource {

    private final String sensorId;

    // Shared readings storage: sensorId -> list of readings
    private static final Map<String, List<SensorReading>> data = new HashMap<>();

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        if (!SensorResource.sensors.containsKey(sensorId)) {
            return Response.status(404)
                    .entity(Map.of("error", "Sensor not found: " + sensorId))
                    .type(MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(data.getOrDefault(sensorId, new ArrayList<>())).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(SensorReading reading) {
        Sensor sensor = SensorResource.sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Sensor not found: " + sensorId))
                    .type(MediaType.APPLICATION_JSON).build();
        }

        // 403 if sensor is under maintenance - triggers SensorUnavailableMapper
        if ("MAINTENANCE".equalsIgnoreCase(sensor.getStatus())) {
            throw new SensorUnavailableException(
                "Sensor '" + sensorId + "' is currently under MAINTENANCE " +
                "and cannot accept new readings."
            );
        }

        // Auto-stamp UUID and timestamp
        reading.initialise();

        // Store the reading
        data.computeIfAbsent(sensorId, k -> new ArrayList<>()).add(reading);

        // Side effect: update sensor's currentValue
        sensor.setCurrentValue(reading.getValue());

        return Response.status(201).entity(reading).build();
    }
}