package com.edu.pucp.dp1.redex.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.edu.pucp.dp1.redex.model.PackageFlight;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface PackageFlightRepository extends JpaRepository<PackageFlight, Integer> {
    
    public List<PackageFlight> findAll();
    public PackageFlight findPackageFlightById(int id);
}
