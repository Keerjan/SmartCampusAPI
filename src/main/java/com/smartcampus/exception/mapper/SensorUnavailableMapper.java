/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception.mapper;

import com.smartcampus.exception.SensorUnavailableException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Provider
public class SensorUnavailableMapper
        implements ExceptionMapper<SensorUnavailableException> {

    @Override
    public Response toResponse(SensorUnavailableException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "403 Forbidden");
        error.put("message", ex.getMessage());
        error.put("hint", "Sensor must be ACTIVE to accept new readings");
        return Response.status(Response.Status.FORBIDDEN)
                .entity(error)
                .type("application/json")
                .build();
    }
}