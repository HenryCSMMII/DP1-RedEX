package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.City;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Integer> {
    public City findCityById(int id);
    public City findCityByName(String name);

    @Query("SELECT c FROM City c WHERE c.id BETWEEN :idInicio and :idFinal")
    public List<City> findCityByIds(int idInicio, int idFinal);
}
