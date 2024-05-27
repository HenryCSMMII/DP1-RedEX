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

import com.edu.pucp.dp1.redex.model.EstadoVuelo;
import com.edu.pucp.dp1.redex.services.EstadoVueloService;

@RestController
@RequestMapping("/estadoVuelo")
@CrossOrigin
public class EstadoVueloController {
    
    @Autowired
    private EstadoVueloService estadoVueloService;

    @GetMapping(value = "/")
    List<EstadoVuelo> getAll(){
        return estadoVueloService.getAll();
    }

    @GetMapping(value = "/{id}")
    EstadoVuelo get(@PathVariable int id){
        return estadoVueloService.get(id);
    }

    @PostMapping(value = "/")
    EstadoVuelo register(@RequestBody EstadoVuelo estadoVuelo) throws SQLException{
        return estadoVueloService.register(estadoVuelo);
    }

    @PutMapping(value = "/")
    EstadoVuelo update(@RequestBody EstadoVuelo estadoVuelo) throws SQLException{
        return estadoVueloService.update(estadoVuelo);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        estadoVueloService.delete(id);
    }

}
