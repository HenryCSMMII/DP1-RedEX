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
import com.edu.pucp.dp1.redex.services.ContinentService;

@RestController
@RequestMapping("/continent")
@CrossOrigin
public class ContinentController {

    @Autowired
    private ContinentService continentService;

    @GetMapping(value = "/")
    List<Continent> getAll(){
        return continentService.getAll();
    }

    @GetMapping(value = "/{id}")
    Continent get(@PathVariable int id){
        return continentService.get(id);
    }

    @PostMapping(value = "/")
    Continent register(@RequestBody Continent continent) throws SQLException{
        return continentService.register(continent);
    }

    @PutMapping(value = "/")
    Continent update(@RequestBody Continent continent) throws SQLException{
        return continentService.update(continent);
    }
    
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        continentService.delete(id);
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    List<Continent> listaContinentsPorIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return continentService.listContinentByIds(idInicio, idFinal);
    }
}
