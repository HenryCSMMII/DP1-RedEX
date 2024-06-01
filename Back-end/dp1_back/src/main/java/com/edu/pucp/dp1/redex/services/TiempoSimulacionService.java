package com.edu.pucp.dp1.redex.services;

import com.edu.pucp.dp1.redex.dto.TiempoSimulacionDTO;
import com.edu.pucp.dp1.redex.model.TiempoSimulacion;
import com.edu.pucp.dp1.redex.repository.TiempoSimulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TiempoSimulacionService {

    @Autowired
    private TiempoSimulacionRepository tiempoSimulacionRepository;

    @Transactional
    public TiempoSimulacionDTO register(TiempoSimulacionDTO tiempoSimulacionDTO){
        TiempoSimulacion tiempoSimulacion = convertToEntity(tiempoSimulacionDTO);
        TiempoSimulacion savedTiempoSimulacion = tiempoSimulacionRepository.save(tiempoSimulacion);
        return convertToDTO(savedTiempoSimulacion);
    }

    public List<TiempoSimulacionDTO> getAll(){
        return tiempoSimulacionRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public TiempoSimulacionDTO get(int id){
        TiempoSimulacion tiempoSimulacion = tiempoSimulacionRepository.findById(id).orElse(null);
        return convertToDTO(tiempoSimulacion);
    }

    public TiempoSimulacionDTO update(TiempoSimulacionDTO tiempoSimulacionDTO){
        TiempoSimulacion tiempoSimulacion = convertToEntity(tiempoSimulacionDTO);
        TiempoSimulacion updatedTiempoSimulacion = tiempoSimulacionRepository.save(tiempoSimulacion);
        return convertToDTO(updatedTiempoSimulacion);
    }

    public void delete(int id){
        tiempoSimulacionRepository.deleteById(id);
    }

    private TiempoSimulacionDTO convertToDTO(TiempoSimulacion tiempoSimulacion) {
        if (tiempoSimulacion == null) {
            return null;
        }
        return new TiempoSimulacionDTO(
                tiempoSimulacion.getId(),
                tiempoSimulacion.getDiaActual(),
                tiempoSimulacion.getTiempoActual()
        );
    }

    private TiempoSimulacion convertToEntity(TiempoSimulacionDTO tiempoSimulacionDTO) {
        return new TiempoSimulacion(
                tiempoSimulacionDTO.getId(),
                tiempoSimulacionDTO.getDiaActual(),
                tiempoSimulacionDTO.getTiempoActual()
        );
    }
}
