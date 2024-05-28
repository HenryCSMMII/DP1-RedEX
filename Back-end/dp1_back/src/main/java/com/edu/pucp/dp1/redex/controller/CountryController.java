package com.edu.pucp.dp1.redex.controller;

import com.edu.pucp.dp1.redex.dto.CountryDTO;
import com.edu.pucp.dp1.redex.model.Country;
import com.edu.pucp.dp1.redex.services.CountryService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/country")
@CrossOrigin
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping(value = "/")
    List<CountryDTO> getAll(){
        return countryService.getAll();
    }

    @GetMapping(value = "/{id}")
    CountryDTO get(@PathVariable int id){
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
    List<CountryDTO> listaCountriesPorIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return countryService.listCountryByIds(idInicio, idFinal);
    }
}
