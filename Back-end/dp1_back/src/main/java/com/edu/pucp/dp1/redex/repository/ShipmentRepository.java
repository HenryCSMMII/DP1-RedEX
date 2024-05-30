package com.edu.pucp.dp1.redex.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.edu.pucp.dp1.redex.model.Shipment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface ShipmentRepository extends JpaRepository<Shipment, Integer> {
    
    public List<Shipment> findAll();
    public Shipment findShipmentById(int id);

    @Query("SELECT s FROM Shipment s WHERE s.id BETWEEN :idInicio and :idFinal")
    public List<Shipment> findShipmentByIds(int idInicio, int idFinal);
}
