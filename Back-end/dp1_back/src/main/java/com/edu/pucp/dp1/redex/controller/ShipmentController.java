package com.edu.pucp.dp1.redex.controller;

import com.edu.pucp.dp1.redex.dto.ShipmentDTO;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.services.ShipmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/shipment")
@CrossOrigin
public class ShipmentController {

    @Autowired
    private ShipmentService shipmentService;

    @GetMapping(value = "/")
    List<ShipmentDTO> getAll(){
        return shipmentService.getAll();
    }

    @GetMapping(value = "/{id}")
    ShipmentDTO get(@PathVariable int id){
        return shipmentService.get(id);
    }

    @PostMapping(value = "/")
    Shipment register(@RequestBody ShipmentDTO shipmentDTO) throws SQLException{
        return shipmentService.register(shipmentDTO);
    }

    @PutMapping(value = "/")
    Shipment update(@RequestBody ShipmentDTO shipmentDTO) throws SQLException{
        return shipmentService.update(shipmentDTO);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        shipmentService.delete(id);
    }

    @GetMapping(value = "/{idInicio}/{idFinal}")
    List<ShipmentDTO> listarShipmentsByIds(@PathVariable int idInicio, @PathVariable int idFinal){
        return shipmentService.listShipmentsByIds(idInicio, idFinal);
    }
}
