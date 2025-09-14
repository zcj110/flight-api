package com.flight.api.service.impl;

import com.flight.api.entity.Airport;
import com.flight.api.repository.AirportRepository;
import com.flight.api.service.AirportService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirportServiceImpl implements AirportService {

    private final AirportRepository airportRepository;

    @Override
    public Airport createAirport(Airport airport) {
        if (airportRepository.existsByCode(airport.getCode())) {
            throw new RuntimeException("Airport code already exists");
        }
        return airportRepository.save(airport);
    }

    @Override
    @Transactional(readOnly = true)
    public Airport getAirportById(Long id) {
        return airportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airport not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Airport getAirportByCode(String code) {
        return airportRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Airport not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Airport> getAllAirports() {
        return airportRepository.findAll();
    }

    @Override
    public Airport updateAirport(Long id, Airport airport) {
        Airport existingAirport = getAirportById(id);
        existingAirport.setCode(airport.getCode());
        existingAirport.setName(airport.getName());
        existingAirport.setCity(airport.getCity());
        existingAirport.setCountry(airport.getCountry());
        return airportRepository.save(existingAirport);
    }

    @Override
    public void deleteAirport(Long id) {
        if (!airportRepository.existsById(id)) {
            throw new RuntimeException("Airport not found with id: " + id);
        }
        airportRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return airportRepository.existsByCode(code);
    }
} 