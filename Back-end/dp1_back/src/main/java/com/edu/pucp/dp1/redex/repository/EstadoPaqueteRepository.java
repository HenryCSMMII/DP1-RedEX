package com.edu.pucp.dp1.redex.repository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.edu.pucp.dp1.redex.model.EstadoPaquete;

import io.lettuce.core.dynamic.annotation.Param;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional

public interface EstadoPaqueteRepository extends JpaRepository<EstadoPaquete, Integer>{
    
    public List<EstadoPaquete> findAll();
    public EstadoPaquete findEstadoPaqueteById(int id);
}
