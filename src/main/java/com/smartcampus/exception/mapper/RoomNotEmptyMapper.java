/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception.mapper;

import com.smartcampus.exception.RoomNotEmptyException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Provider
public class RoomNotEmptyMapper implements ExceptionMapper<RoomNotEmptyException> {

    @Override
    public Response toResponse(RoomNotEmptyException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "409 Conflict");
        error.put("message", ex.getMessage());
        error.put("hint", "Remove all sensors from this room before deleting it");
        return Response.status(Response.Status.CONFLICT)
                .entity(error)
                .type("application/json")
                .build();
    }
}