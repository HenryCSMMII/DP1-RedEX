package com.edu.pucp.dp1.redex.repository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.edu.pucp.dp1.redex.model.EstadoVuelo;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional

public interface EstadoVueloRepository extends JpaRepository<EstadoVuelo, Integer>{
    
    public List<EstadoVuelo> findAll();
    public EstadoVuelo findEstadoVueloById(int id);
}
