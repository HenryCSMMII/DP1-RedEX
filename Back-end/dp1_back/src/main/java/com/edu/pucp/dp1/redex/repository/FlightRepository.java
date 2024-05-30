package com.edu.pucp.dp1.redex.repository;
import com.edu.pucp.dp1.redex.model.EstadoVuelo;
import com.edu.pucp.dp1.redex.model.Flight;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface FlightRepository extends JpaRepository<Flight, Integer>{
    
    public List<Flight> findAll();
    public Flight findFlightById(int id);

    public List<Flight> findFlightByEstadoVuelo(EstadoVuelo estado);

    @Query("SELECT f FROM Flight f WHERE f.id BETWEEN :idInicio and :idFinal")
    public List<Flight> findFlightByIds(int idInicio, int idFinal);
}
