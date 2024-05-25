package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.model.Algorithm.Airport;
import com.edu.pucp.dp1.redex.repository.AeropuertoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AeropuertoService {

    @Autowired
    AeropuertoRepository aeropuertoRepository;


    public List<Airport> listarAeropuertos() {
        List<Airport> lAeropuertos;
        lAeropuertos = aeropuertoRepository.listarAeropuertos();
        return lAeropuertos;
    }


}
