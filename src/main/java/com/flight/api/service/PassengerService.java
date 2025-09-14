package com.flight.api.service;

import java.util.List;

import com.flight.api.entity.Passenger;

public interface PassengerService {
    Passenger createPassenger(Passenger passenger);
    Passenger getPassengerById(Long passengerId);
    List<Passenger> getPassengersByBookingId(Long bookingId);
    List<Passenger> getPassengersByPassportNumber(String passportNumber);
    Passenger updatePassenger(Long passengerId, Passenger passenger);
    void deletePassenger(Long passengerId);
} 