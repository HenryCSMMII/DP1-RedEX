package com.edu.pucp.dp1.redex.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import com.edu.pucp.dp1.redex.model.PackageFlight;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface PackageFlightRepository extends JpaRepository<PackageFlight, Integer> {
    
    public List<PackageFlight> findAll();
    public PackageFlight findPackageFlightById(int id);

    public List<PackageFlight> findByPaqueteId(int paqueteId);
    public List<PackageFlight> findByFlightId(int flightId);

    @Query("SELECT f FROM PackageFlight f WHERE f.id BETWEEN :idInicio and :idFinal")
    public List<PackageFlight> findPackageFlightByIds(int idInicio, int idFinal);    
}
