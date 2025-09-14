package com.flight.api.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.flight.api.entity.Booking;
import com.flight.api.entity.Passenger;
import com.flight.api.repository.BookingRepository;
import com.flight.api.repository.PassengerRepository;
import com.flight.api.service.BookingService;
import com.flight.api.service.FlightService;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

    @Autowired
    private  BookingRepository bookingRepository;
    @Autowired
    private  PassengerRepository passengerRepository;
    @Autowired
    private  FlightService flightService;

    @Override
    public Booking createBooking(Booking booking, List<Passenger> passengers) {
        // Generate unique booking reference
        booking.setReference(generateBookingReference());
        booking.setStatus(Booking.BookingStatus.PENDING);
        booking.setBookingTime(LocalDateTime.now()); // Set booking time
        
        // Calculate total price
        booking.setTotalPrice(booking.getFlight().getPrice().multiply(new BigDecimal(passengers.size())));
        
        // Save booking
        Booking savedBooking = bookingRepository.save(booking);
        
        // Save passengers
        for (Passenger passenger : passengers) {
            passenger.setBooking(savedBooking);
            passengerRepository.save(passenger);
        }
        
        // Update available seats
        flightService.updateAvailableSeats(booking.getFlight().getFlightId(), passengers.size());
        
        return savedBooking;
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Booking not found with id: " + bookingId));
    }

    @Override
    @Transactional(readOnly = true)
    public Booking getBookingByReference(String reference) {
        return bookingRepository.findByReference(reference)
                .orElseThrow(() -> new RuntimeException("Booking not found with reference: " + reference));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Booking> getUserBookings(Long userId, Pageable pageable) {
        return bookingRepository.findByUserUserId(userId, pageable);
    }

    @Override
    public Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status) {
        Booking booking = getBookingById(bookingId);
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    @Override
    public void cancelBooking(Long bookingId) {
        Booking booking = getBookingById(bookingId);
        if (booking.getStatus() == Booking.BookingStatus.CANCELLED) {
            throw new RuntimeException("Booking is already cancelled");
        }
        
        // Update available seats
        int passengerCount = booking.getPassengers().size();
        flightService.updateAvailableSeats(booking.getFlight().getFlightId(), -passengerCount);
        
        // Update booking status
        booking.setStatus(Booking.BookingStatus.CANCELLED);
        bookingRepository.save(booking);
    }

    @Override
    public void deleteBooking(Long bookingId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new RuntimeException("Booking not found with id: " + bookingId);
        }
        bookingRepository.deleteById(bookingId);
    }

    private String generateBookingReference() {
        return UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
} 