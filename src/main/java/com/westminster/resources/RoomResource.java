package com.westminster.resources;

import com.westminster.exceptions.RoomNotEmptyException;
import com.westminster.models.Room;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Path("/rooms")
public class RoomResource {

    // Shared in-memory storage for all rooms
    public static final Map<String, Room> rooms = new ConcurrentHashMap<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        return Response.ok(new ArrayList<>(rooms.values())).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Room room) {
        if (room.getId() == null || room.getId().isBlank()) {
            return Response.status(400)
                    .entity(Map.of("error", "Room 'id' field is required"))
                    .type(MediaType.APPLICATION_JSON).build();
        }
        rooms.put(room.getId(), room);
        return Response.status(201).entity(room).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getById(@PathParam("id") String id) {
        Room room = rooms.get(id);
        if (room == null) {
            return Response.status(404)
                    .entity(Map.of("error", "Room not found: " + id))
                    .type(MediaType.APPLICATION_JSON).build();
        }
        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        Room room = rooms.get(id);

        // Idempotent: if room doesn't exist, still return 204 (no error)
        if (room == null) {
            return Response.noContent().build();
        }

        // Block deletion if room has sensors assigned
        if (!room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException(
                "Cannot delete room '" + id + "'. It still has " +
                room.getSensorIds().size() + " sensor(s) assigned to it. " +
                "Remove all sensors first."
            );
        }

        rooms.remove(id);
        return Response.noContent().build();
    }
}