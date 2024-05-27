package com.edu.pucp.dp1.redex.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.edu.pucp.dp1.redex.model.Paquete;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.EstadoPaquete;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface PaqueteRepository extends JpaRepository<Paquete, Integer>{

    public List<Paquete> findAll();
    public Paquete findPackageById(int id);

    public List<Paquete> findPackageByAirport(Airport aeropuerto);
    public List<Paquete> findPackageByEstadoPaquete(EstadoPaquete estado);
}
