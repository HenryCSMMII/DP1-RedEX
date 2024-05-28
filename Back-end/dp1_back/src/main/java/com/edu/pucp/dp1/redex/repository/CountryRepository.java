package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.Country;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Country, Integer> {
    public Country findCountryById(int id);

    @Query("SELECT c FROM Country c WHERE c.id BETWEEN :idInicio and :idFinal")
    public List<Country> findCountryByIds(int idInicio, int idFinal);
}
