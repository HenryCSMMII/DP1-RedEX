package com.edu.pucp.dp1.redex.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.edu.pucp.dp1.redex.dto.ItinerarioVuelosDTO;
import com.edu.pucp.dp1.redex.model.ItinerarioVuelos;
import com.edu.pucp.dp1.redex.services.ItinerarioVuelosService;

@RestController
@RequestMapping("/itinerarioVuelos")
@CrossOrigin
public class ItinerarioVuelosController {
    
    @Autowired
    private ItinerarioVuelosService itinerarioVuelosService;

    @GetMapping("/")
    public List<ItinerarioVuelosDTO> getAll() {
        return itinerarioVuelosService.getAll();
    }

    @GetMapping("/{id}")
    public ItinerarioVuelosDTO getById(@PathVariable int id) {
        return itinerarioVuelosService.getById(id);
    }

    @PostMapping(value = "/list", consumes = "multipart/form-data")
    public void registerList(@RequestParam("file") MultipartFile file) {
        List<ItinerarioVuelos> itinerarioVuelosList = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                ItinerarioVuelos itinerario = itinerarioVuelosService.parseItinerarioVuelosFromLine(line);
                itinerarioVuelosList.add(itinerario);
            }
            itinerarioVuelosService.registerListItinerarioVuelos(itinerarioVuelosList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
