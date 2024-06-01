package com.edu.pucp.dp1.redex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.edu.pucp.dp1.redex.dto.FlightDTO;
import com.edu.pucp.dp1.redex.services.AlgorithmService;

import java.util.List;

@RestController
@RequestMapping("/algorithm")
@CrossOrigin
public class AlgorithmController {

    @Autowired
    private AlgorithmService algorithmService;

    @PostMapping("/optimizeFlights")
    public List<FlightDTO> optimizeFlights(@RequestBody List<FlightDTO> flights) {
        return algorithmService.optimizeFlights(flights);
    }
}
