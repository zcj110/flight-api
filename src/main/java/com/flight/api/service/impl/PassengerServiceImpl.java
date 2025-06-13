package com.flight.api.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight.api.entity.Passenger;
import com.flight.api.repository.PassengerRepository;
import com.flight.api.service.PassengerService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional
public class PassengerServiceImpl implements PassengerService {

    @Autowired
    private  PassengerRepository passengerRepository;

    @Override
    public Passenger createPassenger(Passenger passenger) {
        return passengerRepository.save(passenger);
    }

    @Override
    @Transactional(readOnly = true)
    public Passenger getPassengerById(Long passengerId) {
        return passengerRepository.findById(passengerId)
                .orElseThrow(() -> new RuntimeException("Passenger not found with id: " + passengerId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Passenger> getPassengersByBookingId(Long bookingId) {
        return passengerRepository.findByBookingBookingId(bookingId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Passenger> getPassengersByPassportNumber(String passportNumber) {
        return passengerRepository.findByPassportNumber(passportNumber);
    }

    @Override
    public Passenger updatePassenger(Long passengerId, Passenger passenger) {
        Passenger existingPassenger = getPassengerById(passengerId);
        existingPassenger.setFirstName(passenger.getFirstName());
        existingPassenger.setLastName(passenger.getLastName());
        existingPassenger.setPassportNumber(passenger.getPassportNumber());
        existingPassenger.setEmail(passenger.getEmail());
        existingPassenger.setType(passenger.getType());
        return passengerRepository.save(existingPassenger);
    }

    @Override
    public void deletePassenger(Long passengerId) {
        if (!passengerRepository.existsById(passengerId)) {
            throw new RuntimeException("Passenger not found with id: " + passengerId);
        }
        passengerRepository.deleteById(passengerId);
    }
} 