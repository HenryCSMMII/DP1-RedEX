package com.edu.pucp.dp1.redex.controller;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.City;
import com.edu.pucp.dp1.redex.services.CityService;

@RestController
@RequestMapping("/city")
@CrossOrigin
public class CityController {

    @Autowired
    private CityService cityService;

    @GetMapping(value = "/")
    List<City> getAll(){
        return cityService.getAll();
    }

    @GetMapping(value = "/{id}")
    City get(@PathVariable int id){
        return cityService.get(id);
    }

    @PostMapping(value = "/")
    City register(@RequestBody City city) throws SQLException{
        return cityService.register(city);
    }

    @PutMapping(value = "/")
    City update(@RequestBody City city) throws SQLException{
        return cityService.update(city);
    }
    
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        cityService.delete(id);
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    List<City> listaCitiesPorIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return cityService.listCityByIds(idInicio, idFinal);
    }    
}
