package com.flight.api.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.flight.api.dto.AirportDTO;
import com.flight.api.dto.ApiResponse;
import com.flight.api.dto.FlightDTO;
import com.flight.api.dto.FlightSearchRequest;
import com.flight.api.dto.FlightSearchResponse;
import com.flight.api.service.FlightService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/api/flights")
@RequiredArgsConstructor
public class FlightController {

    private final FlightService flightService;

    @PostMapping("/search")
    public ResponseEntity<ApiResponse<FlightSearchResponse>> searchFlights(@RequestBody FlightSearchRequest request) {
        try {
            log.info("Received flight search request: {}", request);
            
            Sort sort = Sort.by(Sort.Direction.fromString(request.getSortOrder()), request.getSortBy());
            Pageable pageable = PageRequest.of(request.getPage(), request.getSize(), sort);
            
            FlightSearchResponse response = new FlightSearchResponse();
            
            // 搜索出发航班
            Page<FlightDTO> departureFlights = flightService.searchFlights(
                request.getFrom(), request.getTo(), request.getDepartureDate(), 
                request.getPassengers(), request.getCabinClass(), pageable);
            
            response.setDepartureFlights(departureFlights);
            
            // 如果是往返航班，搜索返程航班
            if (request.getReturnDate() != null) {
                log.info("Searching return flights for date: {}", request.getReturnDate());
                Page<FlightDTO> returnFlights = flightService.searchFlights(
                    request.getTo(), request.getFrom(), request.getReturnDate(), 
                    request.getPassengers(), request.getCabinClass(), pageable);
                
                response.setReturnFlights(returnFlights);
                response.setRoundTrip(true);
                log.info("Found {} return flights", returnFlights.getTotalElements());
            } else {
                response.setRoundTrip(false);
            }
            
            if (departureFlights.isEmpty()) {
                log.info("No departure flights found");
                return ResponseEntity.ok(ApiResponse.success("No flights found for your search criteria", response));
            }
            
            String message = response.isRoundTrip() 
                ? String.format("Found %d departure flights and %d return flights", 
                    departureFlights.getTotalElements(), 
                    response.getReturnFlights().getTotalElements())
                : String.format("Found %d flights matching your criteria", 
                    departureFlights.getTotalElements());
            
            log.info(message);
            return ResponseEntity.ok(ApiResponse.success(message, response));
        } catch (Exception e) {
            log.error("Error searching flights", e);
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Error searching flights: " + e.getMessage()));
        }
    }

    @GetMapping("/airports")
    public ResponseEntity<ApiResponse<List<AirportDTO>>> getAirports() {
        try {
            List<AirportDTO> airports = flightService.getAllAirports();
            return ResponseEntity.ok(ApiResponse.success("Airports retrieved successfully", airports));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Error retrieving airports: " + e.getMessage()));
        }
    }

    @GetMapping("/cabin-classes")
    public ResponseEntity<ApiResponse<List<String>>> getCabinClasses() {
        try {
            List<String> cabinClasses = List.of("economy", "business", "firstClass");
            return ResponseEntity.ok(ApiResponse.success("Cabin classes retrieved successfully", cabinClasses));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(ApiResponse.error(400, "Error retrieving cabin classes: " + e.getMessage()));
        }
    }
} 