package com.westminster.resources;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.LinkedHashMap;
import java.util.Map;

@Path("/discovery")
public class DiscoveryResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response discover() {

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("api", "Smart Campus Sensor & Room Management API");
        response.put("version", "1.0");
        response.put("status", "operational");
        response.put("description", "RESTful API for managing campus rooms and IoT sensors. " +
                "Provides full CRUD operations with nested sub-resource support for sensor readings.");

        // Contact info
        Map<String, String> contact = new LinkedHashMap<>();
        contact.put("name", "Campus Facilities Admin");
        contact.put("email", "facilities@smartcampus.ac.uk");
        response.put("contact", contact);

        // Primary resource links (HATEOAS)
        Map<String, String> resources = new LinkedHashMap<>();
        resources.put("rooms", "/api/v1/rooms");
        resources.put("sensors", "/api/v1/sensors");
        resources.put("sensorReadings", "/api/v1/sensors/{sensorId}/readings");
        response.put("resources", resources);

        // Available actions
        Map<String, String> actions = new LinkedHashMap<>();
        actions.put("listRooms",         "GET /api/v1/rooms");
        actions.put("createRoom",        "POST /api/v1/rooms");
        actions.put("getRoomById",       "GET /api/v1/rooms/{roomId}");
        actions.put("deleteRoom",        "DELETE /api/v1/rooms/{roomId}");
        actions.put("listSensors",       "GET /api/v1/sensors?type={optional}");
        actions.put("createSensor",      "POST /api/v1/sensors");
        actions.put("getSensorById",     "GET /api/v1/sensors/{sensorId}");
        actions.put("getSensorReadings", "GET /api/v1/sensors/{sensorId}/readings");
        actions.put("postSensorReading", "POST /api/v1/sensors/{sensorId}/readings");
        response.put("availableActions", actions);

        return Response.ok(response).build();
    }
}