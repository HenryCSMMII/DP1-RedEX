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

import com.edu.pucp.dp1.redex.model.Paquete;
import com.edu.pucp.dp1.redex.services.PaqueteService;

@RestController
@RequestMapping("/package")
@CrossOrigin
public class PaqueteController {
    
    @Autowired
    private PaqueteService paqueteService;

    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/")
    List<Paquete> getAll(){
        return paqueteService.getAll();
    }

    //Trae a un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @GetMapping(value = "/{id}")
    Paquete get(@PathVariable int id){
        return paqueteService.get(id);
    }

    //Registra un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PostMapping(value = "/")
    Paquete register(@RequestBody Paquete paquete) throws SQLException{
         
        // if(flightService.duplicadoPropio(flight.getId())!=null){
        //     throw new SQLException();
        // }
        return paqueteService.register(paquete);
    }

    //Actualiza un vuelo
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @PutMapping(value = "/")
    Paquete update(@RequestBody Paquete paquete) throws SQLException{
        return paqueteService.update(paquete);
    }
    
    //@CrossOrigin(origins = "https://proyectaserver.inf.pucp.edu.pe")
    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        paqueteService.delete(id);
    }

    @GetMapping(value = "/listarPaquetesPorAeropuerto/{idAeropuerto}")
    List<Paquete> listarPaquetesPorAeropuerto(@PathVariable int idAeropuerto){
        return paqueteService.listarPaquetesPorAeropuerto(idAeropuerto);
    }

}
