package com.flight.api.dto;

import org.springframework.data.domain.Page;

import lombok.Data;

@Data
public class FlightSearchResponse {
    private Page<FlightDTO> departureFlights;
    private Page<FlightDTO> returnFlights;
    private boolean isRoundTrip;
} 