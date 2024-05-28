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

import com.edu.pucp.dp1.redex.model.Continent;
import com.edu.pucp.dp1.redex.model.Country;
import com.edu.pucp.dp1.redex.services.CountryService;

@RestController
@RequestMapping("/country")
@CrossOrigin
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping(value = "/")
    List<Country> getAll(){
        return countryService.getAll();
    }

    @GetMapping(value = "/{id}")
    Country get(@PathVariable int id){
        return countryService.get(id);
    }

    @PostMapping(value = "/")
    Country register(@RequestBody Country country) throws SQLException{
        return countryService.register(country);
    }

    @PutMapping(value = "/")
    Country update(@RequestBody Country country) throws SQLException{
        return countryService.update(country);
    }
    
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        countryService.delete(id);
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    List<Country> listaCountriesPorIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return countryService.listCountryByIds(idInicio, idFinal);
    }
}
