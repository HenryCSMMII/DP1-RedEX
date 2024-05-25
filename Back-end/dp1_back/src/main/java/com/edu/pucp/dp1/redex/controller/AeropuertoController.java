package com.edu.pucp.dp1.redex.controller;

import com.edu.pucp.dp1.redex.model.Algorithm.Airport;
import com.edu.pucp.dp1.redex.services.AeropuertoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/ubicacion")
@CrossOrigin
public class AeropuertoController {

    @Autowired
    AeropuertoService aeropuertoService;

    @GetMapping(value = "/get/aeropuerto")
    @ResponseBody
    public List<Airport> listarAeropuertos(){
        List<Airport> aeropuertos;

        aeropuertos = aeropuertoService.listarAeropuertos();
        return aeropuertos;
    }

}
