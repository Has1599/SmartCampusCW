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

@Path("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SensorRoomResource {

    static final Map<String, Room> rooms = new LinkedHashMap<>();

    @GET
    public Collection<Room> getAllRooms() {
        return rooms.values();
    }

    @POST
    public Response createRoom(Room room) {
        if (room.getName() == null || room.getName().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Room name is required")
                    .build();
        }
        if (room.getCapacity() <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Capacity must be greater than 0")
                    .build();
        }

        String id = UUID.randomUUID().toString();
        room.setId(id);
        rooms.put(id, room);

        return Response.status(Response.Status.CREATED)
                .entity(room)
                .build();
    }

    @GET
    @Path("/{roomId}")
    public Response getRoomById(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found with ID: " + roomId)
                    .build();
        }

        return Response.ok(room).build();
    }

    @DELETE
    @Path("/{roomId}")
    public Response deleteRoom(@PathParam("roomId") String roomId) {
        Room room = rooms.get(roomId);

        if (room == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("Room not found with ID: " + roomId)
                    .build();
        }

        if (room.getSensorIds() != null && !room.getSensorIds().isEmpty()) {
            throw new RoomNotEmptyException("Room is currently occupied by active hardware and cannot be deleted");
        }

        rooms.remove(roomId);
        return Response.ok("Room deleted successfully").build();
    }
}