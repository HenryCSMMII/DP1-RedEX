package com.edu.pucp.dp1.redex.model;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.edu.pucp.dp1.redex.dto.AirportDTO;
import com.edu.pucp.dp1.redex.dto.CityDTO;
import com.edu.pucp.dp1.redex.dto.FlightDTO;
import com.edu.pucp.dp1.redex.dto.PaqueteDTO;
import com.edu.pucp.dp1.redex.services.AirportService;
import com.edu.pucp.dp1.redex.services.CityService;

public class DataLoader {

    @Autowired
    private AirportService airportService;

    public static List<FlightDTO> loadFlights(String filePath) throws IOException {
        List<FlightDTO> flights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("-");
                if (parts.length == 5) {
                    try {
                        String origin = parts[0].trim();
                        String destination = parts[1].trim();
                        String departureTime = parts[2].trim();
                        String arrivalTime = parts[3].trim();
                        int capacity = Integer.parseInt(parts[4].trim());

                        flights.add(new FlightDTO(origin, destination, departureTime, arrivalTime, capacity));                        
                    } catch (NumberFormatException e) {
                        System.err.println("Error parsing integer for flight capacity: " + e.getMessage());
                    }
                } else {
                    System.err.println("Incorrect line format for flights: " + line);
                }
                // for(FlightDTO flight : flights){
                //     System.out.println(flight.toString());
                // }
            }
        }
        // for(FlightDTO flight : flights){
        //     System.out.println(flight.toString());
        // }
        System.out.println("funciona");
        return flights;
    }
    
    public static List<PaqueteDTO> loadPackages(String filePath) throws IOException {
        List<PaqueteDTO> packages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 5) {
                    String origin = parts[0]; // EBCI es el origen
                    String envioCode = parts[1]; // 000000002 es parte del identificador de envío

                    String departureTime = parts[2]+" "+parts[3]; // 20240103-01:48 es la fecha y hora de salida
                    
                    String[] destQuantity = parts[4].split(":");
                    String destination = destQuantity[0]; // RPLL es el destino
                    int quantity = Integer.parseInt(destQuantity[1]); // 02 es la cantidad de paquetes

                    // Combinamos la ciudad de origen con el código de envío para formar un identificador único para este paquete
                    String packageId = origin + "-" + envioCode;

                    AirportService airportService = new AirportService();
                    AirportDTO originAirport = airportService.getByCode(origin), destinationAirport = airportService.getByCode(destination);
                    int originId = originAirport.getId(), destinationId = destinationAirport.getId();
                    

                    //OJO CON ESTA FUNCIÓN
                    // AirportService airportService = new AirportService();
                    // AirportDTO originAirport = airportService.getByCityId(originCity.getId());
                    // AirportDTO destinationAirport = airportService.getByCityId(destinationCity.getId());
                    // int originId = originAirport.getId(), destinationId = destinationAirport.getId();

                    PaqueteDTO pack = new PaqueteDTO(originId, destinationId, departureTime, quantity, packageId);
                    packages.add(pack);
                }
            }

            // for(PaqueteDTO pack : packages){
            //     System.out.println(pack.toString());
            // }
            reader.close();
            return packages;
        }
    }
    public static List<AirportDTO> loadAirports(String filePath) throws IOException {
        List<AirportDTO> airports = new ArrayList<>();
        airports = airportService.getAll();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String code = parts[0].trim();
                    System.out.println("code:"+code);
                    String name = parts[1].trim();
                    System.out.println(name);
                    String city = parts[2].trim();
                    System.out.println(city);
                    String country = parts[3].trim();
                    System.out.println(country);
                    String continent = parts[4].trim();
                    System.out.println(continent);
                    System.out.println("'"+parts[5].trim()+"'");
                    int timezone = Integer.parseInt(parts[5].trim());
                    int capacity = Integer.parseInt(parts[6].trim());

                    CityService cityService = new CityService();
                    CityDTO cityDTO = cityService.getByName(city);

                    airports.add(new AirportDTO(code, cityDTO.getId(), capacity));
                } else {
                    System.err.println("Incorrect line format for airports: " + line);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing integer for airport timezone or capacity: " + e.getMessage());
        }
        return airports;
    }
}