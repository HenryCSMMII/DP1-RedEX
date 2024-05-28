package com.edu.pucp.dp1.redex.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.edu.pucp.dp1.redex.model.ItinerarioVuelos;
import com.edu.pucp.dp1.redex.repository.ItinerarioVuelosRepository;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

@Service
public class ItinerarioVuelosService {

    @Autowired
    private ItinerarioVuelosRepository itinerarioVuelosRepository;

    public void registerListItinerarioVuelos(List<ItinerarioVuelos> itinerarioVuelosList) {
        itinerarioVuelosRepository.saveAll(itinerarioVuelosList);
    }

    public ItinerarioVuelos parseItinerarioVuelosFromLine(String line) {
        String[] fields = line.split("-");
        ItinerarioVuelos itinerario = new ItinerarioVuelos();
        itinerario.setOrigin(fields[0]);
        itinerario.setDestination(fields[1]);
        itinerario.setDepartureTime(fields[2]);
        itinerario.setArrivalTime(fields[3]);
        itinerario.setCapacity(Integer.parseInt(fields[4]));
        return itinerario;
    }
}
