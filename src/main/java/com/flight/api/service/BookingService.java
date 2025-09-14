package com.flight.api.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.flight.api.entity.Booking;
import com.flight.api.entity.Passenger;

public interface BookingService {
    Booking createBooking(Booking booking, List<Passenger> passengers);
    Booking getBookingById(Long bookingId);
    Booking getBookingByReference(String reference);
    Page<Booking> getUserBookings(Long userId, Pageable pageable);
    Booking updateBookingStatus(Long bookingId, Booking.BookingStatus status);
    void cancelBooking(Long bookingId);
    void deleteBooking(Long bookingId);
} 