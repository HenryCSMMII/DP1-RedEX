package com.edu.pucp.dp1.redex.model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataLoader {

    public static List<Flight> loadFlights(String filePath) throws IOException {
        List<Flight> flights = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.trim().split("-");
                if (parts.length == 5) {

                    String origin = parts[0];
                    String destination = parts[1];
                    String departureTime = parts[2]; // Assuming date-time format is split by a '-'
                    String arrivalTime = parts[3] ;
                    int capacity = Integer.parseInt(parts[4].trim()); // Ensure there are no trailing spaces
                    String flightnumber = "prueba";
                    int currentLoad = 0,duration = 0; 


                    //flights.add(new Flight(origin, destination, departureTime, arrivalTime, capacity,flightnumber,currentLoad, duration));
                } else {
                    System.err.println("Incorrect line format: " + line);
                }
            }
        } catch (NumberFormatException e) {
            System.err.println("Error parsing integer value: " + e.getMessage());
        }
        for(Flight flight : flights){
            System.out.println(flight.toString());
        }
        return flights;
    }


    public static List<Package> loadPackages(String filePath) throws IOException {
        List<Package> packages = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("-");
                if (parts.length == 5) {
                    String origin = parts[0]; // EBCI es el origen
                    String envioCode = parts[1]; // 000000002 es parte del identificador de envío
                    String departureTime = parts[2]; // 20240103-01:48 es la fecha y hora de salida
                    String[] destQuantity = parts[4].split(":");
                    String destination = destQuantity[0]; // RPLL es el destino
                    int quantity = Integer.parseInt(destQuantity[1]); // 02 es la cantidad de paquetes

                    // Combinamos la ciudad de origen con el código de envío para formar un identificador único para este paquete
                    String packageId = origin + "-" + envioCode;

                    Package pack = new Package(origin, destination, departureTime, quantity, packageId);
                    packages.add(pack);
                }
            }

            reader.close();
            return packages;
        }
    }
    public static List<Airport> loadAirports(String filePath) throws IOException {
        List<Airport> airports = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) {
                    String code = parts[0].trim();
                    String name = parts[1].trim();
                    String city = parts[2].trim();
                    String country = parts[3].trim();
                    String continent = parts[4].trim();
                    int timezone = Integer.parseInt(parts[5].trim());
                    int capacity = Integer.parseInt(parts[6].trim());

                    airports.add(new Airport(code, name, city, country, continent, timezone, capacity));
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