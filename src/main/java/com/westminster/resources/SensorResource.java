package com.westminster.resources;

import com.westminster.exceptions.LinkedResourceNotFoundException;
import com.westminster.models.*;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/sensors")
public class SensorResource {

    // Shared in-memory storage for all sensors
    public static final Map<String, Sensor> sensors = new ConcurrentHashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(@QueryParam("type") String type) {
        List<Sensor> list = new ArrayList<>(sensors.values());

        // Filter by type if query param is provided e.g. ?type=CO2
        if (type != null && !type.isBlank()) {
            list.removeIf(s -> !s.getType().equalsIgnoreCase(type));
        }

        return Response.ok(list).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Sensor sensor) {
        if (sensor.getId() == null || sensor.getId().isBlank()) {
            return Response.status(400)
                    .entity(Map.of("error", "Sensor 'id' field is required"))
                    .type(MediaType.APPLICATION_JSON).build();
        }

        // Validate the roomId actually exists - throws 422 if not
        Room room = RoomResource.rooms.get(sensor.getRoomId());
        if (room == null) {
            throw new LinkedResourceNotFoundException(
                "Room with id '" + sensor.getRoomId() + "' does not exist. " +
                "Create the room before assigning sensors to it."
            );
        }

        sensors.put(sensor.getId(), sensor);
        room.getSensorIds().add(sensor.getId()); // link sensor → room

        return Response.status(201).entity(sensor).build();
    }

    // Sub-resource locator: delegates /sensors/{id}/readings to dedicated class
    @Path("/{id}/readings")
    public SensorReadingResource getReadings(@PathParam("id") String id) {
        return new SensorReadingResource(id);
    }
}