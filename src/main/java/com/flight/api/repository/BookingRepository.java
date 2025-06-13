package com.flight.api.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flight.api.entity.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    Optional<Booking> findByReference(String reference);
    Page<Booking> findByUserUserId(Long userId, Pageable pageable);
    boolean existsByReference(String reference);
} 