package com.grupo1.PROYECTOFINALEGG.Entity;

import java.util.List;

import lombok.Data;

@Data
public class BookingRequest {
    private String id;
    private String entryDate;
    private List<String> servicios;
}