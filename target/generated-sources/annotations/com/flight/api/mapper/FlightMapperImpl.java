package com.flight.api.mapper;

import com.flight.api.dto.FlightDTO;
import com.flight.api.entity.Airline;
import com.flight.api.entity.Airport;
import com.flight.api.entity.Flight;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-06-21T17:05:48+0800",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.6 (Oracle Corporation)"
)
@Component
public class FlightMapperImpl implements FlightMapper {

    @Override
    public FlightDTO toDTO(Flight flight) {
        if ( flight == null ) {
            return null;
        }

        FlightDTO flightDTO = new FlightDTO();

        flightDTO.setId( flight.getFlightId() );
        flightDTO.setAirlineName( flightAirlineName( flight ) );
        flightDTO.setDepartureAirport( flightDepartureAirportCode( flight ) );
        flightDTO.setDestinationAirport( flightDestinationAirportCode( flight ) );
        flightDTO.setFlightNumber( flight.getFlightNumber() );
        flightDTO.setDepartureDate( flight.getDepartureDate() );
        flightDTO.setDepartureTime( flight.getDepartureTime() );
        flightDTO.setAvailableSeats( flight.getAvailableSeats() );
        flightDTO.setPrice( flight.getPrice() );
        if ( flight.getStatus() != null ) {
            flightDTO.setStatus( flight.getStatus().name() );
        }

        flightDTO.setArrivalTime( flight.getArrivalTime().toLocalTime() );
        flightDTO.setCabinClass( "economy" );

        return flightDTO;
    }

    private String flightAirlineName(Flight flight) {
        if ( flight == null ) {
            return null;
        }
        Airline airline = flight.getAirline();
        if ( airline == null ) {
            return null;
        }
        String name = airline.getName();
        if ( name == null ) {
            return null;
        }
        return name;
    }

    private String flightDepartureAirportCode(Flight flight) {
        if ( flight == null ) {
            return null;
        }
        Airport departureAirport = flight.getDepartureAirport();
        if ( departureAirport == null ) {
            return null;
        }
        String code = departureAirport.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }

    private String flightDestinationAirportCode(Flight flight) {
        if ( flight == null ) {
            return null;
        }
        Airport destinationAirport = flight.getDestinationAirport();
        if ( destinationAirport == null ) {
            return null;
        }
        String code = destinationAirport.getCode();
        if ( code == null ) {
            return null;
        }
        return code;
    }
}
