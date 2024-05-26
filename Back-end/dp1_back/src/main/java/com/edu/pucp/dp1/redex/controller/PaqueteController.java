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

import com.edu.pucp.dp1.redex.services.PackageService;

@RestController
@RequestMapping("/package")
@CrossOrigin
public class PaqueteController {
    
    @Autowired
    private PackageService paqueteService;

    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/")
    List<Package> getAll(){
        return paqueteService.getAll();
    }

    //Trae a un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/{id}")
    Package get(@PathVariable int id){
        return paqueteService.get(id);
    }

    //Registra un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PostMapping(value = "/")
    Package register(@RequestBody Package paquete) throws SQLException{
         
        // if(flightService.duplicadoPropio(flight.getId())!=null){
        //     throw new SQLException();
        // }
        return paqueteService.register(paquete);
    }

    //Actualiza un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PutMapping(value = "/")
    Package update(@RequestBody Package paquete) throws SQLException{
        return paqueteService.update(paquete);
    }
    
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        paqueteService.delete(id);
    }    

}
