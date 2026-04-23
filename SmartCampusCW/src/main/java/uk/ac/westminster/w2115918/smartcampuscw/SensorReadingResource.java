/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.westminster.w2115918.smartcampuscw;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Collection;

@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorReadingResource {

    static final Map<String, SensorReading> readings = new LinkedHashMap<>();

    private final String sensorId;

    public SensorReadingResource(String sensorId) {
        this.sensorId = sensorId;
    }

    @GET
    public Collection<SensorReading> getAllReadings() {
        List<SensorReading> result = new ArrayList<>();
        for (SensorReading reading : readings.values()) {
            if (reading.getSensorId().equals(sensorId)) {
                result.add(reading);
            }
        }
        return result;
    }

    @POST
    public Response addReading(SensorReading reading) {

        Sensor sensor = SensorResource.sensors.get(sensorId);
        
        if (!sensor.getStatus().equalsIgnoreCase("active")) {
            throw new SensorUnavailableException("Sensor with ID: " + sensorId + " is unavailable and cannot accept new readings");
        }

        if (reading.getValue() == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Reading value is required")
                    .build();
        }

        String id = UUID.randomUUID().toString();
        reading.setId(id);
        reading.setSensorId(sensorId);
        readings.put(id, reading);

        return Response.status(Response.Status.CREATED)
                .entity(reading)
                .build();
    }

    @GET
    @Path("/{readingId}")
    public Response getReadingById(@PathParam("readingId") String readingId) {
        SensorReading reading = readings.get(readingId);

        if (reading == null || !reading.getSensorId().equals(sensorId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Reading not found with ID: " + readingId)
                    .build();
        }

        return Response.ok(reading).build();
    }
}
