package com.flight.api.dto;

import lombok.Data;

@Data
public class AirportDTO {
    private Long id;
    private String code;
    private String city;
    private String country;
} 