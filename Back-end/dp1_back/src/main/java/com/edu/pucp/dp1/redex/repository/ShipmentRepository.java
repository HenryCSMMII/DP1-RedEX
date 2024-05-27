package com.edu.pucp.dp1.redex.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.edu.pucp.dp1.redex.model.Shipment;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    
    public List<Shipment> findAll();
    public Shipment findShipmentById(int id);
}
