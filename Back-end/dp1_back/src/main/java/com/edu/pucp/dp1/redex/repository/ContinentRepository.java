package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.Continent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface ContinentRepository extends JpaRepository<Continent, Integer> {
    public Continent findContinentById(int id);
}
