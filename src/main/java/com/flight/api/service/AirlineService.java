package com.flight.api.service;

import com.flight.api.entity.Airline;
import java.util.List;

public interface AirlineService {
    Airline createAirline(Airline airline);
    Airline getAirlineById(Long id);
    Airline getAirlineByCode(String code);
    List<Airline> getAllAirlines();
    Airline updateAirline(Long id, Airline airline);
    void deleteAirline(Long id);
    boolean existsByCode(String code);
} 