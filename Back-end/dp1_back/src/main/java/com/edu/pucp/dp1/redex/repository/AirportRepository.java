package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface AirportRepository extends JpaRepository<Airport, Integer> {

    public List<Airport> findAll();
    public Airport findAirportById(int id);
    public Airport findAirportByCityId(int cityId);

    @Query("SELECT a FROM Airport a WHERE a.id BETWEEN :idInicio and :idFinal")
    public List<Airport> findAirportByIds(int idInicio, int idFinal);

}
