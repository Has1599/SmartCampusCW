/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package uk.ac.westminster.w2115918.smartcampuscw;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;
import java.util.ArrayList;
import java.util.List;

@Path("/sensors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorResource {

    static final Map<String, Sensor> sensors = new LinkedHashMap<>();
    
    @GET
    public Collection<Sensor> getAllSensors(@QueryParam("type") String type) {

        if (type == null || type.isEmpty()) {
            return sensors.values();
        }

        List<Sensor> filtered = new ArrayList<>();
        for (Sensor sensor : sensors.values()) {
            if (sensor.getType().equalsIgnoreCase(type)) {
                filtered.add(sensor);
            }
        }
        return filtered;
    }

    @POST
    public Response createSensor(Sensor sensor) {
        if (sensor.getType() == null || sensor.getType().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Sensor type is required")
                    .build();
        }
        if (sensor.getStatus() == null || sensor.getStatus().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Sensor status is required")
                    .build();
        }
        if (sensor.getRoomId() == null || sensor.getRoomId().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("roomId is required")
                    .build();
        }

        if (!SensorRoomResource.rooms.containsKey(sensor.getRoomId())) {
            throw new LinkedResourceNotFoundException("Room with ID: " + sensor.getRoomId() + " does not exist in the system");
        }

        String id = UUID.randomUUID().toString();
        sensor.setId(id);
        sensors.put(id, sensor);

        SensorRoomResource.rooms.get(sensor.getRoomId()).getSensorIds().add(id);

        return Response.status(Response.Status.CREATED)
                .entity(sensor)
                .build();
    }

    @GET
    @Path("/{sensorId}")
    public Response getSensorById(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found with ID: " + sensorId)
                    .build();
        }

        return Response.ok(sensor).build();
    }

    @DELETE
    @Path("/{sensorId}")
    public Response deleteSensor(@PathParam("sensorId") String sensorId) {
        Sensor sensor = sensors.get(sensorId);

        if (sensor == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Sensor not found with ID: " + sensorId)
                    .build();
        }

        // Remove sensorId from the room's list
        Room room = SensorRoomResource.rooms.get(sensor.getRoomId());
        if (room != null) {
            room.getSensorIds().remove(sensorId);
        }

        sensors.remove(sensorId);
        return Response.ok("Sensor deleted successfully").build();
    }
    @Path("/{sensorId}/readings")
    public SensorReadingResource getReadingResource(@PathParam("sensorId") String sensorId) {
    
    // Check sensor exists first
        if (!sensors.containsKey(sensorId)) {
            throw new NotFoundException("Sensor not found with ID: " + sensorId);
        }
    
        return new SensorReadingResource(sensorId);
    }
}