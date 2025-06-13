package com.flight.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import lombok.Data;

@Data
public class FlightDTO {
    private Long id;
    private String flightNumber;
    private String airlineName;
    private String departureAirport;
    private String destinationAirport;
    private LocalDate departureDate;
    private LocalTime departureTime;
    private LocalTime arrivalTime;
    private Integer availableSeats;
    private BigDecimal price;
    private String status;
    private String cabinClass;
} 