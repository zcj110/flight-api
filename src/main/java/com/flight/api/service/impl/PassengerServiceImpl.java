package com.flight.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight.api.entity.Passenger;
import com.flight.api.repository.PassengerRepository;
import com.flight.api.service.PassengerService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private  PassengerRepository passengerRepository;

    @Override
    public Passenger createPassenger(Passenger passenger) {
        Passenger savedPassenger = passengerRepository.save(passenger);
        log.info("Created passenger with ID: {}, name: {} {}", 
                savedPassenger.getPassengerId(), savedPassenger.getFirstName(), savedPassenger.getLastName());
        return savedPassenger;
    }

    @Override
    @Transactional(readOnly = true)
    public Passenger getPassengerById(Long passengerId) {
        Passenger passenger = passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + passengerId));
        log.info("Retrieved passenger with ID: {}, name: {} {}", 
                passenger.getPassengerId(), passenger.getFirstName(), passenger.getLastName());
        return passenger;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Passenger> getPassengersByBookingId(Long bookingId) {
        List<Passenger> passengers = passengerRepository.findByBookingBookingId(bookingId);
        log.info("Retrieved {} passengers for booking ID: {}", passengers.size(), bookingId);
        return passengers;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Passenger> getPassengersByPassportNumber(String passportNumber) {
        List<Passenger> passengers = passengerRepository.findByPassportNumber(passportNumber);
        log.info("Retrieved {} passengers for passport number: {}", passengers.size(), passportNumber);
        return passengers;
    }

    @Override
    public Passenger updatePassenger(Long passengerId, Passenger passenger) {
        Passenger existingPassenger = getPassengerById(passengerId);
        existingPassenger.setFirstName(passenger.getFirstName());
        existingPassenger.setLastName(passenger.getLastName());
        existingPassenger.setPassportNumber(passenger.getPassportNumber());
        existingPassenger.setEmail(passenger.getEmail());
        existingPassenger.setType(passenger.getType());
        Passenger updatedPassenger = passengerRepository.save(existingPassenger);
        log.info("Updated passenger with ID: {}, name: {} {}", 
                updatedPassenger.getPassengerId(), updatedPassenger.getFirstName(), updatedPassenger.getLastName());
        return updatedPassenger;
    }

    @Override
    public void deletePassenger(Long passengerId) {
        if (!passengerRepository.existsById(passengerId)) {
            throw new RuntimeException("Passenger not found with id: " + passengerId);
        }
        passengerRepository.deleteById(passengerId);
        log.info("Deleted passenger with ID: {}", passengerId);
    }
} 