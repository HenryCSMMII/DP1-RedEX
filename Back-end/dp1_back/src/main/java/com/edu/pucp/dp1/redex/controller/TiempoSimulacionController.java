package com.edu.pucp.dp1.redex.controller;

import com.edu.pucp.dp1.redex.dto.TiempoSimulacionDTO;
import com.edu.pucp.dp1.redex.services.TiempoSimulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tiempoSimulacion")
@CrossOrigin
public class TiempoSimulacionController {

    @Autowired
    private TiempoSimulacionService tiempoSimulacionService;

    @GetMapping("/")
    public List<TiempoSimulacionDTO> getAll() {
        return tiempoSimulacionService.getAll();
    }

    @GetMapping("/{id}")
    public TiempoSimulacionDTO get(@PathVariable int id) {
        return tiempoSimulacionService.get(id);
    }

    @PostMapping("/")
    public TiempoSimulacionDTO register(@RequestBody TiempoSimulacionDTO tiempoSimulacionDTO) {
        return tiempoSimulacionService.register(tiempoSimulacionDTO);
    }

    @PutMapping("/")
    public TiempoSimulacionDTO update(@RequestBody TiempoSimulacionDTO tiempoSimulacionDTO) {
        return tiempoSimulacionService.update(tiempoSimulacionDTO);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        tiempoSimulacionService.delete(id);
    }
}
