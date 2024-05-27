package com.edu.pucp.dp1.redex.repository;

import com.edu.pucp.dp1.redex.model.City;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
@Transactional
public interface CityRepository extends JpaRepository<City, Integer> {
    public City findCityById(int id);
}
