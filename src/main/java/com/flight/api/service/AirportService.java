package com.flight.api.service;

import java.util.List;

import com.flight.api.entity.Airport;

public interface AirportService {
    Airport createAirport(Airport airport);
    Airport getAirportById(Long airportId);
    Airport getAirportByCode(String code);
    List<Airport> getAllAirports();
    Airport updateAirport(Long airportId, Airport airport);
    void deleteAirport(Long airportId);
    boolean existsByCode(String code);
} 