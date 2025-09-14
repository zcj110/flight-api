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
import com.flight.api.mapper.FlightMapper;
import com.flight.api.repository.AirportRepository;
import com.flight.api.repository.FlightRepository;
import com.flight.api.service.FlightService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class FlightServiceImpl implements FlightService {

    @Autowired
    private FlightRepository flightRepository;

    @Autowired
    private AirportRepository airportRepository;

    private final FlightMapper flightMapper;

    @Override
    public Flight createFlight(Flight flight) {
        Flight savedFlight = flightRepository.save(flight);
        log.info("Created flight with ID: {}, flight number: {}", savedFlight.getFlightId(), savedFlight.getFlightNumber());
        return savedFlight;
    }

    @Override
    @Transactional(readOnly = true)
    public Flight getFlightById(Long flightId) {
        Flight flight = flightRepository.findById(flightId)
                .orElseThrow(() -> new RuntimeException("Flight not found with id: " + flightId));
        log.info("Retrieved flight with ID: {}, flight number: {}", flight.getFlightId(), flight.getFlightNumber());
        return flight;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<FlightDTO> searchFlights(Long from, Long to, LocalDate departureDate,
                                       Integer passengers, String cabinClass, Pageable pageable) {
        Page<Flight> flights = flightRepository.searchFlights(from, to, departureDate, passengers, pageable);
        Page<FlightDTO> flightDTOs = flights.map(flightMapper::toDTO);
        log.info("Search flights returned {} results for date: {}, from: {}, to: {}", 
                flightDTOs.getTotalElements(), departureDate, from, to);
        return flightDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public List<String> getAllAirportCodes() {
        List<String> airportCodes = flightRepository.findAll().stream()
            .flatMap(flight -> List.of(
                flight.getDepartureAirport().getCode(),
                flight.getDestinationAirport().getCode()
            ).stream())
            .distinct()
            .collect(Collectors.toList());
        log.info("Retrieved {} unique airport codes", airportCodes.size());
        return airportCodes;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByAirline(Long airlineId) {
        List<Flight> flights = flightRepository.findByAirlineId(airlineId);
        log.info("Retrieved {} flights for airline ID: {}", flights.size(), airlineId);
        return flights;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByDepartureAirport(Long airportId) {
        List<Flight> flights = flightRepository.findByDepartureAirportAirportId(airportId);
        log.info("Retrieved {} flights for departure airport ID: {}", flights.size(), airportId);
        return flights;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Flight> getFlightsByDestinationAirport(Long airportId) {
        List<Flight> flights = flightRepository.findByDestinationAirportAirportId(airportId);
        log.info("Retrieved {} flights for destination airport ID: {}", flights.size(), airportId);
        return flights;
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
        Flight updatedFlight = flightRepository.save(existingFlight);
        log.info("Updated flight with ID: {}, flight number: {}", updatedFlight.getFlightId(), updatedFlight.getFlightNumber());
        return updatedFlight;
    }

    @Override
    public void deleteFlight(Long flightId) {
        if (!flightRepository.existsById(flightId)) {
            throw new RuntimeException("Flight not found with id: " + flightId);
        }
        flightRepository.deleteById(flightId);
        log.info("Deleted flight with ID: {}", flightId);
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
        log.info("Updated available seats for flight ID: {} from {} to {}", 
                flightId, flight.getAvailableSeats() + seatsToReserve, newAvailableSeats);
    }

    @Override
    public List<AirportDTO> getAllAirports() {
        List<Airport> airports = airportRepository.findAll();
        List<AirportDTO> airportDTOs = airports.stream()
            .map(airport -> {
                AirportDTO dto = new AirportDTO();
                dto.setId(airport.getAirportId());
                dto.setCode(airport.getCode());
                dto.setCity(airport.getCity());
                dto.setCountry(airport.getCountry());
                return dto;
            })
            .collect(Collectors.toList());
        log.info("Retrieved {} airports", airportDTOs.size());
        return airportDTOs;
    }
} 