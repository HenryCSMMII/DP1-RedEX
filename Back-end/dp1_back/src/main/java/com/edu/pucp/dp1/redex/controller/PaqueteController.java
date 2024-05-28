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

    @GetMapping(value = "/")
    List<Paquete> getAll(){
        return paqueteService.getAll();
    }

    @GetMapping(value = "/{id}")
    Paquete get(@PathVariable int id){
        return paqueteService.get(id);
    }

    @PostMapping(value = "/")
    Paquete register(@RequestBody Paquete paquete) throws SQLException{
        return paqueteService.register(paquete);
    }

    @PutMapping(value = "/")
    Paquete update(@RequestBody Paquete paquete) throws SQLException{
        return paqueteService.update(paquete);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        paqueteService.delete(id);
    }

    @GetMapping(value = "/listarPaquetesPorAeropuerto/{idAeropuerto}")
    List<Paquete> listarPaquetesPorAeropuerto(@PathVariable int idAeropuerto){
        return paqueteService.listarPaquetesPorAeropuerto(idAeropuerto);
    }

    @GetMapping(value = "/listarPaquetesPorEstado/{idEstado}")
    List<Paquete> listarPaquetesPorEstado(@PathVariable int idEstado){
        return paqueteService.listarPaquetesPorEstado(idEstado);
    }
}
