package com.edu.pucp.dp1.redex.services;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edu.pucp.dp1.redex.dto.ItinerarioVuelosDTO;
import com.edu.pucp.dp1.redex.model.ItinerarioVuelos;
import com.edu.pucp.dp1.redex.repository.ItinerarioVuelosRepository;

@Service
public class ItinerarioVuelosService {

    @Autowired
    private ItinerarioVuelosRepository itinerarioVuelosRepository;

    public List<ItinerarioVuelosDTO> getAll() {
        return itinerarioVuelosRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ItinerarioVuelosDTO getById(int id) {
        ItinerarioVuelos itinerario = itinerarioVuelosRepository.findById(id).orElse(null);
        return convertToDTO(itinerario);
    }

    public void registerListItinerarioVuelos(List<ItinerarioVuelos> itinerarioVuelosList) {
        itinerarioVuelosRepository.saveAll(itinerarioVuelosList);
    }

    public ItinerarioVuelos parseItinerarioVuelosFromLine(String line) {
        String[] fields = line.split("-");
        ItinerarioVuelos itinerario = new ItinerarioVuelos();
        itinerario.setOrigin(fields[0]);
        itinerario.setDestination(fields[1]);
        itinerario.setDepartureTime(LocalTime.parse(fields[2]));
        itinerario.setArrivalTime(LocalTime.parse(fields[3]));
        itinerario.setCapacity(Integer.parseInt(fields[4]));
        return itinerario;
    }

    private ItinerarioVuelosDTO convertToDTO(ItinerarioVuelos itinerario) {
        if (itinerario == null) return null;
        return new ItinerarioVuelosDTO(
                itinerario.getId(),
                itinerario.getOrigin(),
                itinerario.getDestination(),
                itinerario.getDepartureTime(),
                itinerario.getArrivalTime(),
                itinerario.getCapacity()
        );
    }

    private ItinerarioVuelos convertToEntity(ItinerarioVuelosDTO dto) {
        return new ItinerarioVuelos(
                dto.getId(),
                dto.getOrigin(),
                dto.getDestination(),
                dto.getDepartureTime(),
                dto.getArrivalTime(),
                dto.getCapacity()
        );
    }
}
