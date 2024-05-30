package com.edu.pucp.dp1.redex.repository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.edu.pucp.dp1.redex.model.EstadoTrazabilidad;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface EstadoTrazabilidadRepository extends JpaRepository<EstadoTrazabilidad, Integer> {
    
    public List<EstadoTrazabilidad> findAll();
    public EstadoTrazabilidad findEstadoTrazabilidadById(int id);
}
