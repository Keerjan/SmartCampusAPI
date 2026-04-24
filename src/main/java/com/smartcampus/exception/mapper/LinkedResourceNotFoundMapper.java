/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.smartcampus.exception.mapper;

import com.smartcampus.exception.LinkedResourceNotFoundException;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import jakarta.ws.rs.core.Response;
import java.util.HashMap;
import java.util.Map;

@Provider
public class LinkedResourceNotFoundMapper
        implements ExceptionMapper<LinkedResourceNotFoundException> {

    @Override
    public Response toResponse(LinkedResourceNotFoundException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "422 Unprocessable Entity");
        error.put("message", ex.getMessage());
        error.put("hint", "Ensure the roomId references an existing room");
        return Response.status(422)
                .entity(error)
                .type("application/json")
                .build();
    }
}