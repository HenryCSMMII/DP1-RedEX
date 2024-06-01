package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.TiempoSimulacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TiempoSimulacionRepository extends JpaRepository<TiempoSimulacion, Integer> {
}
