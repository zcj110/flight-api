package com.flight.api.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flight.api.dto.AirportDTO;
import com.flight.api.dto.FlightDTO;
import com.flight.api.entity.Flight;

public interface FlightService {
    Flight createFlight(Flight flight);
    Flight getFlightById(Long flightId);
    Page<FlightDTO> searchFlights(
        Long from,
        Long to,
        LocalDate departureDate,
        Integer passengers,
        String cabinClass,
        Pageable pageable
    );
    List<Flight> getFlightsByAirline(Long airlineId);
    List<Flight> getFlightsByDepartureAirport(Long airportId);
    List<Flight> getFlightsByDestinationAirport(Long airportId);
    Flight updateFlight(Long flightId, Flight flight);
    void deleteFlight(Long flightId);
    void updateAvailableSeats(Long flightId, int seatsToReserve);
    List<String> getAllAirportCodes();
    List<AirportDTO> getAllAirports();
} 