package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.Country;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface CountryRepository extends JpaRepository<Country, Integer> {
    public Country findCountryById(int id);
}
