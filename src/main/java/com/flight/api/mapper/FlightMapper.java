package com.flight.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.flight.api.dto.FlightDTO;
import com.flight.api.entity.Flight;

@Mapper(componentModel = "spring")
public interface FlightMapper {
    
    @Mapping(target = "id", source = "flightId")
    @Mapping(target = "airlineName", source = "airline.name")
    @Mapping(target = "departureAirport", source = "departureAirport.code")
    @Mapping(target = "destinationAirport", source = "destinationAirport.code")
    @Mapping(target = "arrivalTime", expression = "java(flight.getArrivalTime().toLocalTime())")
    @Mapping(target = "cabinClass", constant = "economy")
    FlightDTO toDTO(Flight flight);
} 