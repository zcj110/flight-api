package com.flight.api.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.flight.api.entity.Flight;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    
    @Query("SELECT f FROM Flight f WHERE f.departureAirport.airportId = :departureId " +
           "AND f.destinationAirport.airportId = :arrivalId " +
           "AND f.departureDate >= :departureDate " +
           "AND f.availableSeats >= :passengerCount " +
           "AND f.status = 'SCHEDULED'")
    Page<Flight> searchFlights(
            @Param("departureId") Long departureId,
            @Param("arrivalId") Long arrivalId,
            @Param("departureDate") LocalDate departureDate,
            @Param("passengerCount") Integer passengerCount,
            Pageable pageable);
    
    List<Flight> findByAirlineId(Long airlineId);
    
    List<Flight> findByDepartureAirportAirportId(Long airportId);
    
    List<Flight> findByDestinationAirportAirportId(Long airportId);
} 