package com.edu.pucp.dp1.redex.Algorithm;

import com.edu.pucp.dp1.redex.model.*;
import com.edu.pucp.dp1.redex.utils.TimeZoneAirport;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class BD {
    public static List<Airport> airports;
    public static List<Flight> flightsTemp;
    public static List<Shipment> shipmentsTemp = new ArrayList<>();
    public static List<Flight>[][] flights;
    public static List<Shipment> shipmentsUnresolved = new ArrayList<>();
    public static List<Continent> continents = new ArrayList<>();
    public static List<Country> countries = new ArrayList<>();
    public static List<City> cities = new ArrayList<>();
    public static List<Shipment> shipmentsWaitingList = new ArrayList<>();
    public static List<Shipment> shipmentsResolved = new ArrayList<>();
    public static List<Flight> flightsResolved = new ArrayList<>();
    public static Set<String> processedShipments = new HashSet<>();
    public static Set<Integer> processedFlights = new HashSet<>();

    public static int numero_Run_Semanal = 0;

    // Constants
    public static final int MIN_INITIAL_CHROMOSOME_LENGTH = 2;
    public static final int MAX_INITIAL_CHROMOSOME_LENGTH = 5;
    public static final int MAX_STOPOVERS = 20;
    public static final int POPULATION_NUM_INDIVIDUALS = 10;
    public static final int NUM_GENERATIONS = 10;

    public static final long ONE_DAY_MS = 86400000;
    public static final long SEVEN_DAYS_MS = 604800000;
    public static final int SEMANAL_DAYS = 4;
    public static final int SHIPMENTS_PER_ITERATION = 1000000;

    public static void readContinents() throws IOException {
        continents = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File( "/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/continentes.txt"))) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] parts = data.split(";");
                if (parts.length < 2) continue;
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                continents.add(new Continent(id, name));
            }
        } catch (IOException e) {
            System.out.println("EXCEPTION CONTINENTS: " + e.getMessage());
            throw e;
        }
    }

    public static void readCountries() throws IOException {
        countries = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/paises.txt"))) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] parts = data.split(";");
                if (parts.length < 5) continue;
                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String abbrev = parts[2];
                String continentName = parts[3];
                String city = parts[4];
                Continent continent = continents.stream()
                        .filter(c -> c.getName().equalsIgnoreCase(continentName))
                        .findFirst()
                        .orElseGet(() -> {
                            Continent newContinent = new Continent(continents.size() + 1, continentName);
                            continents.add(newContinent);
                            return newContinent;
                        });
                countries.add(new Country(id, name, abbrev, continent, city));
            }
        } catch (IOException e) {
            System.out.println("EXCEPTION COUNTRIES: " + e.getMessage());
            throw e;
        }
    }

    public static void readCities() throws IOException {
        cities = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/ciudades.txt"))) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] parts = data.split(",");
                int id = Integer.parseInt(parts[0]);
                int countryId = Integer.parseInt(parts[1]);
                String name = parts[2];
                cities.add(new City(id, countryId, name));
            }
        } catch (IOException e) {
            System.out.println("EXCEPTION CITIES: " + e.getMessage());
            throw e;
        }
    }

    public static void readAirports() {
        airports = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File( "/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/aeropuertos.txt"))) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] split = data.split(",");
    
                Airport airport = new Airport();
                airport.setId(Integer.valueOf(split[0]));
                airport.setCode(split[1].trim());
    
                // Extraer el continente y el país de split[6]
                String[] continentParts = split[6].trim().split("/");
                String continentName = continentParts[0];
                String countryNamePart = continentParts.length > 1 ? continentParts[1] : "";
    
                // Buscar o crear Continent
                Continent continent = null;
                for (Continent c : continents) {
                    if (c.getName().trim().equals(continentName)) {
                        continent = c;
                        break;
                    }
                }
                if (continent == null) {
                    continent = new Continent();
                    continent.setName(continentName);
                    continents.add(continent);
                }
    
                // Buscar o crear Country
                Country country = null;
                for (Country c : countries) {
                    if (c.getName().trim().equals(split[3].trim())) {
                        country = c;
                        break;
                    }
                }
                if (country == null) {
                    country = new Country();
                    country.setCity(split[2].trim());
                    country.setName(split[3].trim());
                    country.setAbbrev(split[4].trim());
                    country.setContinent(continent);
                    countries.add(country);
                    System.out.println("problema id: ");
                }
    
                airport.setCountry(country);
                airport.setTime_zone(split[7].trim());
                airport.setLatitude(split[8].trim());
                airport.setLongitude(split[9].trim());
                airport.setMax_capacity(Integer.valueOf(split[10].trim()));
                airport.setStorage(new ArrayList<>());
                airports.add(airport);
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION AIRPORTS: " + e.getMessage());
        }
    }
    
    
    

    public static void readFlights() {
        flightsTemp = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File("/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/itinerario.txt"))) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] split = data.split("-");
                Flight flight = new Flight();
                flight.setCode(data);

                Airport airport_1;
                for (int i = 0; i < airports.size(); i++) {
					if(airports.get(i).getCode().equals(split[0])) {
						airport_1 = airports.get(i);
						flight.setDeparture_airport(airport_1);
						break;
					}
                }
                //flight.setDeparture_airport(airport_1);
                
				Airport airport_2;// = new Airport();
                //airport_2.setCode(split[1]);
                for (int i = 0; i < airports.size(); i++) {
					if(airports.get(i).getCode().equals(split[1])) {
						airport_2 = airports.get(i);
						flight.setArrival_airport(airport_2);
						break;
					}
                }
                flight.setMax_capacity(Integer.valueOf(split[4]));
				
                SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                Date departure_time = formatter.parse(split[2]);
                Date arrival_time = formatter.parse(split[3]);

                if (Integer.parseInt(split[2].substring(0, 2)) > Integer.parseInt(split[3].substring(0, 2))) {
                    Calendar c = Calendar.getInstance();
                    c.setTime(arrival_time);
                    c.add(Calendar.DATE, 1);
                    arrival_time = c.getTime();
                }
                flight.setDeparture_date_time(departure_time);
                flight.setArrival_date_time(arrival_time);
                flight.calcEstimatedTime();
                flight.setDifference_system(TimeZoneAirport.calc_difference(flight.getDeparture_airport(), airports.get(0)) * 60 * 1000);
                flightsTemp.add(flight);
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION FLIGHTS: " + e.getMessage());
        }
    }

    public static void readShipments(String filePath) {
        shipmentsTemp = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                String[] split = data.split("-");
                
				Shipment shipment = new Shipment();
				shipment.setId(shipmentsTemp.size());
                shipment.setCode(split[0]);
                
				Airport departure;
                for (int i = 0; i < airports.size(); i++) {
					if(airports.get(i).getCode().equals(split[0])){
						departure = airports.get(i);
						shipment.setDeparturAirport(departure);
						break;
					}	
                }
                
		        String dateTime = split[2].substring(6) + '/' + split[2].substring(4, 6) + '/' + split[2].substring(0, 4) + " " + split[3];
		        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		        Date register_date_time = formatter_date.parse(dateTime);
				shipment.setRegisterDateTime(register_date_time);
                
				Airport arrival;
                for (int i = 0; i < airports.size(); i++) {
					if(airports.get(i).getCode().equals(split[4].substring(0, 4))){
						arrival = airports.get(i);
						shipment.setArrivalAirport(arrival);
						break;
					}
                }
                shipment.setPackageQuantity(Integer.parseInt(split[4].substring(5)));
                shipment.setDepartureTime(null);
                shipment.setArrivalTime(null);
                shipment.setFlightSchedule(null);
                shipment.setClient(null);
                shipment.setOperator(null);
                shipment.setState(State.Creado);
                shipmentsTemp.add(shipment);
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION SHIPMENTS SIN PARAMETRO: " + e.getMessage());
        }
    }

    public static int read_list_shipment_with_date(long date_simulation, int type_simulation) {
        long limit_date_data = 0;
        shipmentsTemp = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatter_date_limit = new SimpleDateFormat("dd/MM/yyyy");
        if (type_simulation == 1) {
            limit_date_data = date_simulation + ONE_DAY_MS + ONE_DAY_MS;
        } else if (type_simulation == 7) {
            limit_date_data = date_simulation + SEVEN_DAYS_MS + ONE_DAY_MS;
        }
        List<String> pack_files = new ArrayList<>();
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/pack/"))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName().toString());
                }
            }
            pack_files = new ArrayList<>(fileSet);
        } catch (Exception e) {
            System.out.println("EXCEPCION FILES:" + e.getMessage());
        }
        try {
            for (int j = 0; j < pack_files.size(); j++) {
                File file = new File("/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/pack/" + pack_files.get(j));
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String data = scanner.nextLine();
                        String[] split = data.split("-");
						String dateTime = split[2].substring(6) + '/' + split[2].substring(4, 6) + '/' + split[2].substring(0, 4) + " " + split[3];
                        Date register_date_time = formatter_date.parse(dateTime);
                        String actual_date_format = split[2].substring(6) + '/' + split[2].substring(4, 6) + '/' + split[2].substring(0, 4);
                        Date actual_date = formatter_date_limit.parse(actual_date_format);
                        if (actual_date.getTime() >= limit_date_data) {
                            break;
                        }
                        if (register_date_time.getTime() >= date_simulation && register_date_time.getTime() <= limit_date_data && !processedShipments.contains(split[0]+"-"+split[1])) {
                            Shipment shipment = new Shipment();
                            shipment.setCode(split[0]+"-"+split[1]);
                            
							Airport departure;// = new Airport();
                            for (int i = 0; i < airports.size(); i++) {
								if(airports.get(i).getCode().equals(split[0])){
									departure = airports.get(i);
									shipment.setDeparturAirport(departure);
									break;
								}
                            }
                            //shipment.setDeparturAirport(departure);
                            shipment.setRegisterDateTime(register_date_time);
                            
							Airport arrival;// = new Airport();
                            //arrival.setCode(split[3].substring(0, 4));
                            for (int i = 0; i < airports.size(); i++) {
								if(airports.get(i).getCode().equals(split[4].substring(0, 4))){
									arrival = airports.get(i);
									shipment.setArrivalAirport(arrival);
									break;
								}
                            }
                            
							shipment.setPackageQuantity(Integer.parseInt(split[4].substring(5)));

							shipment.setDepartureTime(null);
                            shipment.setArrivalTime(null);
                            shipment.setFlightSchedule(null);
                            shipment.setClient(null);
                            shipment.setOperator(null);
                            shipment.setState(null);
                            
							shipmentsTemp.add(shipment);
                            processedShipments.add(split[0]+"-"+split[1]);
                        }
                    }
                    System.out.println("ARCHIVO NUMERO: " + j);
                    System.out.println("TAMANO DE LA LISTA DE ENVIOS: " + shipmentsTemp.size());
                }
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION SHIPMENTS CON PARAMETRO: " + e.getMessage());
        }
        int tamanio = shipmentsTemp.size();
        System.out.println("TAMANO DE LA LISTA DE ENVIOS EN TOTAL: " + tamanio);
        return tamanio;
    }

    public static int read_list_shipment_with_date_REPLANIFICACION(long date_simulation) {
        long limit_date_data = 0;
        Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatter_date_limit = new SimpleDateFormat("dd/MM/yyyy");
        limit_date_data = date_simulation + ONE_DAY_MS + ONE_DAY_MS;
        List<String> pack_files = new ArrayList<>();
        Set<String> fileSet = new HashSet<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/pack/"))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    fileSet.add(path.getFileName().toString());
                }
            }
            pack_files = new ArrayList<>(fileSet);
        } catch (Exception e) {
            System.out.println("EXCEPCION FILES:" + e.getMessage());
        }
        try {
            for (int j = 0; j < pack_files.size(); j++) {
                File file = new File("/home/inf226.982.5e/DP1/DP1-RedEX/Back-end/dp1_back/src/main/resources/input/pack/" + pack_files.get(j));
                try (Scanner scanner = new Scanner(file)) {
                    while (scanner.hasNextLine()) {
                        String data = scanner.nextLine();
                        String[] split = data.split("-");
                        String dateTime = split[2].substring(6) + '/' + split[2].substring(4, 6) + '/' + split[2].substring(0, 4) + " " + split[3];
                        Date register_date_time = formatter_date.parse(dateTime);
                        String actual_date_format = split[2].substring(6) + '/' + split[2].substring(4, 6) + '/' + split[2].substring(0, 4);
                        Date actual_date = formatter_date_limit.parse(actual_date_format);
                        if (actual_date.getTime() >= limit_date_data) {
                            break;
                        }
                        if (register_date_time.getTime() >= date_simulation && register_date_time.getTime() <= limit_date_data && !processedShipments.contains(split[0]+"-"+split[1])) {
                            Shipment shipment = new Shipment();
                            shipment.setCode(split[0]+"-"+split[1]);
                            
							Airport departure = new Airport();
                            for (int i = 0; i < airports.size(); i++) {
								if(airports.get(i).getCode().equals(split[0])){
									departure = airports.get(i);
									shipment.setDeparturAirport(departure);
									break;
								}
                            }
                            shipment.setRegisterDateTime(register_date_time);
                            
							Airport arrival;
                            //arrival.setCode(split[3].substring(0, 4));
                            for (int i = 0; i < airports.size(); i++) {
								if(airports.get(i).getCode().equals(split[4].substring(0, 4))){
									arrival = airports.get(i);
									shipment.setArrivalAirport(arrival);
									break;
								}
                            }
                            //shipment.setArrivalAirport(arrival);
                            
							shipment.setPackageQuantity(Integer.parseInt(split[4].substring(5)));
                            
							shipment.setDepartureTime(null);
                            shipment.setArrivalTime(null);
                            shipment.setFlightSchedule(null);
                            shipment.setClient(null);
                            shipment.setOperator(null);
                            shipment.setState(null);
                            shipmentsWaitingList.add(shipment);
                            processedShipments.add(split[0]+"-"+split[1]);
                        }
                    }
                    System.out.println("ARCHIVO NUMERO: " + j);
                    System.out.println("TAMANO DE LA LISTA DE ENVIOS: " + shipmentsWaitingList.size());
                }
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION SHIPMENTS CON PARAMETRO: " + e.getMessage());
        }
        int tamanio = shipmentsTemp.size();
        System.out.println("TAMANO DE LA LISTA DE ENVIOS EN TOTAL: " + tamanio);
        return tamanio;
    }
}
