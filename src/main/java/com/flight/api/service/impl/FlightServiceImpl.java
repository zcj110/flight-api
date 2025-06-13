package com.flight.api.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight.api.dto.AirportDTO;
import com.flight.api.dto.FlightDTO;
import com.flight.api.entity.Airport;
import com.flight.api.entity.Flight;
import com.flight.api.repository.AirportRepository;
import com.flight.api.repository.FlightRepository;
import com.flight.api.service.FlightService;
import com.flight.api.mapper.FlightMapper;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    private final FlightMapper flightMapper;

    @Override
    public Flight createFlight(Flight flight) {
        return flightRepository.save(flight);
    }

    @Override
    @Transactional(readOnly = true)
    public Flight getFlightById(Long flightId) {
        return flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightDTO> searchFlights(Long from, Long to, LocalDate departureDate,
                                       Integer passengers, String cabinClass, Pageable pageable) {
        Page<Flight> flights = flightRepository.searchFlights(from, to, departureDate, passengers, pageable);
        return flights.map(flightMapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllAirportCodes() {
        return flightRepository.findAll().stream()
            .flatMap(flight -> List.of(
                flight.getDepartureAirport().getCode(),
                flight.getDestinationAirport().getCode()
            ).stream())
            .distinct()
            .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByAirline(Long airlineId) {
        return flightRepository.findByAirlineId(airlineId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByDepartureAirport(Long airportId) {
        return flightRepository.findByDepartureAirportAirportId(airportId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByDestinationAirport(Long airportId) {
        return flightRepository.findByDestinationAirportAirportId(airportId);
    }

    @Override
    public Flight updateFlight(Long flightId, Flight flight) {
        Flight existingFlight = getFlightById(flightId);
        existingFlight.setFlightNumber(flight.getFlightNumber());
        existingFlight.setAirline(flight.getAirline());
        existingFlight.setDepartureAirport(flight.getDepartureAirport());
        existingFlight.setDestinationAirport(flight.getDestinationAirport());
        existingFlight.setDepartureDate(flight.getDepartureDate());
        existingFlight.setDepartureTime(flight.getDepartureTime());
        existingFlight.setArrivalTime(flight.getArrivalTime());
        existingFlight.setTotalSeats(flight.getTotalSeats());
        existingFlight.setAvailableSeats(flight.getAvailableSeats());
        existingFlight.setPrice(flight.getPrice());
        existingFlight.setStatus(flight.getStatus());
        return flightRepository.save(existingFlight);
    }

    @Override
    public void deleteFlight(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new RuntimeException("Flight not found with id: " + flightId);
        }
        flightRepository.deleteById(flightId);
    }

    @Override
    @Transactional
    public void updateAvailableSeats(Long flightId, int seatsToReserve) {
        Flight flight = getFlightById(flightId);
        int newAvailableSeats = flight.getAvailableSeats() - seatsToReserve;
        if (newAvailableSeats < 0) {
            throw new RuntimeException("Not enough seats available");
        }
        flight.setAvailableSeats(newAvailableSeats);
        flightRepository.save(flight);
    }

    @Override
    public List<AirportDTO> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();
        return airports.stream()
            .map(airport -> {
                AirportDTO dto = new AirportDTO();
                dto.setId(airport.getAirportId());
                dto.setCode(airport.getCode());
                dto.setCity(airport.getCity());
                dto.setCountry(airport.getCountry());
                return dto;
            })
            .collect(Collectors.toList());
    }
} 