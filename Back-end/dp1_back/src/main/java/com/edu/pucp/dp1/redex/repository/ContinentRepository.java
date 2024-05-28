package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.Continent;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface ContinentRepository extends JpaRepository<Continent, Integer> {
    public Continent findContinentById(int id);

    @Query("SELECT c FROM Continent c WHERE c.id BETWEEN :idInicio and :idFinal")
    public List<Continent> findContinentByIds(int idInicio, int idFinal);
}
