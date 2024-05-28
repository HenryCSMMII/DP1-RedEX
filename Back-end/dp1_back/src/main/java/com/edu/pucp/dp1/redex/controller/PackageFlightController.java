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

import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.PackageFlight;
import com.edu.pucp.dp1.redex.services.PackageFlightService;

@RestController
@RequestMapping("/packageFlight")
@CrossOrigin
public class PackageFlightController {
    
    @Autowired
    private PackageFlightService packageFlightService;

    @GetMapping(value = "/")
    List<PackageFlight> getAll(){
        return packageFlightService.getAll();
    }

    @GetMapping(value = "/{id}")
    PackageFlight get(@PathVariable int id){
        return packageFlightService.get(id);
    }

    @GetMapping(value = "/package/{paqueteId}")
    List<PackageFlight> getByPaqueteId(@PathVariable int paqueteId){
        return packageFlightService.getByPaqueteId(paqueteId);
    }

    @GetMapping(value = "/flight/{flightId}")
    List<PackageFlight> getByFlightId(@PathVariable int flightId){
        return packageFlightService.getByFlightId(flightId);
    }

    @PostMapping(value = "/")
    PackageFlight register(@RequestBody PackageFlight packageFlight) throws SQLException{
        return packageFlightService.register(packageFlight);
    }

    @PutMapping(value = "/")
    PackageFlight update(@RequestBody PackageFlight packageFlight) throws SQLException{
        return packageFlightService.update(packageFlight);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        packageFlightService.delete(id);
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    List<PackageFlight> listarPackageFlightsByIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return packageFlightService.listFlightByIds(idInicio, idFinal);
    }    
}
