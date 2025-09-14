package com.flight.api.service.impl;

import com.flight.api.entity.Airline;
import com.flight.api.repository.AirlineRepository;
import com.flight.api.service.AirlineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AirlineServiceImpl implements AirlineService {

    private final AirlineRepository airlineRepository;

    @Override
    public Airline createAirline(Airline airline) {
        if (airlineRepository.existsByCode(airline.getCode())) {
            throw new RuntimeException("Airline code already exists");
        }
        return airlineRepository.save(airline);
    }

    @Override
    @Transactional(readOnly = true)
    public Airline getAirlineById(Long id) {
        return airlineRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Airline not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public Airline getAirlineByCode(String code) {
        return airlineRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Airline not found with code: " + code));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Airline> getAllAirlines() {
        return airlineRepository.findAll();
    }

    @Override
    public Airline updateAirline(Long id, Airline airline) {
        Airline existingAirline = getAirlineById(id);
        existingAirline.setCode(airline.getCode());
        existingAirline.setName(airline.getName());
        existingAirline.setLogoUrl(airline.getLogoUrl());
        return airlineRepository.save(existingAirline);
    }

    @Override
    public void deleteAirline(Long id) {
        if (!airlineRepository.existsById(id)) {
            throw new RuntimeException("Airline not found with id: " + id);
        }
        airlineRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByCode(String code) {
        return airlineRepository.existsByCode(code);
    }
} 