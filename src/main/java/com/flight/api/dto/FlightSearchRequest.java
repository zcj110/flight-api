package com.flight.api.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class FlightSearchRequest {
    private Long from;
    private Long to;
    private LocalDate departureDate;
    private LocalDate returnDate;
    private Integer passengers = 1;
    private String cabinClass = "economy";
    private int page = 0;
    private int size = 10;
    private String sortBy = "price";
    private String sortOrder = "asc";
} 