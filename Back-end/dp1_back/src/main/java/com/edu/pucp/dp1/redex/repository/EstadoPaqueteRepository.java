package com.edu.pucp.dp1.redex.repository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.edu.pucp.dp1.redex.model.EstadoPaquete;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional

public interface EstadoPaqueteRepository extends JpaRepository<EstadoPaquete, Integer>{
    
    public List<EstadoPaquete> findAll();
    public EstadoPaquete findEstadoPaqueteById(int id);
}
