package com.flight.api.entity;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Table(name = "airports")
@Data
@EqualsAndHashCode(callSuper = true)
public class Airport extends BaseEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "airport_id")
    private Long airportId;
    
    @Column(nullable = false, unique = true, length = 10)
    private String code;
    
    @Column(nullable = false, length = 100)
    private String name;
    
    @Column(nullable = false, length = 100)
    private String city;
    
    @Column(nullable = false)
    private String country;
    
    @OneToMany(mappedBy = "departureAirport")
    private List<Flight> departureFlights = new ArrayList<>();
    
    @OneToMany(mappedBy = "destinationAirport")
    private List<Flight> arrivalFlights = new ArrayList<>();
} 