package com.edu.pucp.dp1.redex.repository;

import org.springframework.transaction.annotation.Transactional;

import com.edu.pucp.dp1.redex.model.Flight;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
@Transactional
public interface FlightRepository extends JpaRepository<Flight, Integer>{
    
    public List<Flight> findAll();
    public Flight findFlightById(int id);
}
