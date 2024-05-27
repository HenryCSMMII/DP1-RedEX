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

import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.services.ShipmentService;

@RestController
@RequestMapping("/shipment")
@CrossOrigin
public class ShipmentController {
    
    @Autowired
    private ShipmentService shipmentService;

    @GetMapping(value = "/")
    List<Shipment> getAll(){
        return shipmentService.getAll();
    }

    @GetMapping(value = "/{id}")
    Shipment get(@PathVariable int id){
        return shipmentService.get(id);
    }

    @PostMapping(value = "/")
    Shipment register(@RequestBody Shipment shipment) throws SQLException{
        return shipmentService.register(shipment);
    }

    @PutMapping(value = "/")
    Shipment update(@RequestBody Shipment shipment) throws SQLException{
        return shipmentService.update(shipment);
    }

    @DeleteMapping(value = "/{id}")
    void delete(@PathVariable int id){
        shipmentService.delete(id);
    }
}
