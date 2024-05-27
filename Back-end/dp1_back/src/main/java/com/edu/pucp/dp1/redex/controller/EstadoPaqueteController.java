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

import com.edu.pucp.dp1.redex.model.EstadoPaquete;
import com.edu.pucp.dp1.redex.services.EstadoPaqueteService;

@RestController
@RequestMapping("/estadoPaquete")
@CrossOrigin
public class EstadoPaqueteController {
    
    @Autowired
    private EstadoPaqueteService estadoPaqueteService;

    @GetMapping(value = "/")
    List<EstadoPaquete> getAll(){
        return estadoPaqueteService.getAll();
    }

    @GetMapping(value = "/{id}")
    EstadoPaquete get(@PathVariable int id){
        return estadoPaqueteService.get(id);
    }

    @PostMapping(value = "/")
    EstadoPaquete register(@RequestBody EstadoPaquete estadoPaquete) throws SQLException{
        return estadoPaqueteService.register(estadoPaquete);
    }

    @PutMapping(value = "/")
    EstadoPaquete update(@RequestBody EstadoPaquete estadoPaquete) throws SQLException{
        return estadoPaqueteService.update(estadoPaquete);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        estadoPaqueteService.delete(id);
    }

}
