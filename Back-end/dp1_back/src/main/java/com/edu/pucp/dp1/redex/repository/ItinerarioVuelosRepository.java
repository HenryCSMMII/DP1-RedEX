package com.edu.pucp.dp1.redex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.edu.pucp.dp1.redex.model.ItinerarioVuelos;

@Repository
public interface ItinerarioVuelosRepository extends JpaRepository<ItinerarioVuelos, Integer> {
}
