package com.edu.pucp.dp1.redex.model;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

import com.edu.pucp.dp1.redex.dto.AirportDTO;
import com.edu.pucp.dp1.redex.dto.CityDTO;
import com.edu.pucp.dp1.redex.dto.CountryDTO;
import com.edu.pucp.dp1.redex.dto.FlightDTO;
import com.edu.pucp.dp1.redex.dto.PaqueteDTO;
import com.edu.pucp.dp1.redex.services.AirportService;
import com.edu.pucp.dp1.redex.services.CityService;
import com.edu.pucp.dp1.redex.services.ContinentService;
import com.edu.pucp.dp1.redex.services.CountryService;

public class Main {

    public static void main(String[] args) {
        LocalTime time = LocalTime.now();
        LocalDate date = LocalDate.now();
        LocalDateTime dateTime = LocalDateTime.of(date, time);

        LocalDateTime newDateTime = dateTime.plusDays(1);
        System.out.println(newDateTime);

        // Configuración del algoritmo genético
        int populationSize = 5; // Tamaño de la población
        double mutationRate = 1; // Tasa de mutación
        double crossoverRate = 0.9; // Tasa de cruce
        int elitismCount = 2; // Número de soluciones élites a conservar
        int tournamentSize = 5; // Tamaño del torneo para la selección
        int maxGenerations = 5; // Define el número máximo de generaciones

        // Inicializar el algoritmo con la configuración
        AlgoritmoGenetico ga = new AlgoritmoGenetico(populationSize, mutationRate, crossoverRate, elitismCount, tournamentSize);

        // Cargar datos
        List<FlightDTO> flights = null;
        List<PaqueteDTO> packages = null;
        List<AirportDTO> airports = null;
        Map<FlightDTO, Integer> flightCapacitiesUsed = new HashMap<>(); // Mapa para rastrear la capacidad usada de cada vuelo

        try {
            airports = DataLoader.loadAirports("Back-end/dp1_back/src/main/resources/input/Aeropuerto.husos.v2.txt");
            //packages = DataLoader.loadPackages("Back-end/dp1_back/src/main/resources/input/pack_enviado_SKBO.txt");
            /*flights = DataLoader.loadFlights("Back-end/dp1_back/src/main/resources/input/planes_vuelo.v3.txt");
            for (FlightDTO flight : flights) {
                flightCapacitiesUsed.put(flight, 0); // Inicializa la capacidad usada de cada vuelo como 0
            }*/
        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
            e.printStackTrace();
            return; // Salir del programa si hay un error
        }

        /*Map<String, List<FlightDTO>> airportToFlights = buildAirportToFlightsMap(flights);
        for (FlightDTO flight : flights) {
            flightCapacitiesUsed.put(flight, 0);
            //       System.out.println("Flight from " + flight.getOrigin() + " to " + flight.getDestination() + " initialized with capacity: " + flight.getCapacity() + " and current load set to 0.");
        }

        // Crear mapa para almacenar la relación aeropuerto-continente
        Map<String, String> airportToContinentData = new HashMap<>();
        final int MAX_STOPS = 5; // Puedes ajustar este valor según sea necesario

        // Generar posibles rutas para cada paquete
        Map<PaqueteDTO, List<List<FlightDTO>>> packageRoutes = generatePossibleRoutesForPackages(packages, airportToFlights, MAX_STOPS);
        //    printPackageRoutes(packageRoutes);
        Map<String, List<FlightDTO>> packageFlights = selectRoutesForPackages(packageRoutes, flightCapacitiesUsed);

        assignPackagesToFlights(packages, packageFlights, flightCapacitiesUsed);
        //    printAssignedRoutes(packageFlights,flightCapacitiesUsed);

        Map<String, List<PaqueteDTO>> packagesByEnvioCode = new HashMap<>();

        assignPackagesToFlights(packages, packageFlights, flightCapacitiesUsed);


        for (PaqueteDTO p : packages) {
            packagesByEnvioCode.computeIfAbsent(p.getPackageId(), k -> new ArrayList<>()).add(p);
            //  System.out.println("Agregando paquete " + p.getPackageId() + " al grupo de envío.");
        }

        Map<String, AirportData> airportToContinentTimezoneData = new HashMap<>();

        // Llenar el mapa con datos de aeropuertos
        for (AirportDTO airport : airports) {
            // Obtener datos del aeropuerto
            int cityid = airport.getCityId();
            CityService cityService = new CityService();
            CityDTO citydto = cityService.get(cityid);
            CountryService countryService = new CountryService();
            CountryDTO countrydto = countryService.get(citydto.getCountryId());
            ContinentService continentService = new ContinentService();
            Continent continent = continentService.get(countrydto.getContinentId());

            //CityDTO city = cityService.findById(cityid);
            
            String continentName = continent.getName();
            
            int timezoneOffset = citydto.getZonahoraria();

            // Almacenar los datos en el mapa
            airportToContinentTimezoneData.put(airport.getCodigoIATA(), new AirportData(continentName, timezoneOffset));
            // System.out.println("Agregando aeropuerto " + airport.getCode() + ": continente=" + continent + ", uso horario=" + timezoneOffset);
        }

        // Pasar los datos del aeropuerto a FlightUtils
        FlightUtils.setAirportToContinentData(airportToContinentData);

        // Inicializar la longitud del cromosoma como el número de paquetes
        int chromosomeLength = packages.size();


        Population population = ga.initPopulation(chromosomeLength, packageRoutes);
        System.out.println("Población inicial:");
        //    population.printPopulationState();
        //System.out.println(flights.size() + " flights populated.");
        ga.evalPopulation(population, flights, packages, airportToFlights, packageFlights,airportToContinentTimezoneData);

        int generation = 1;
        while (!ga.isTerminationConditionMet(generation, maxGenerations)) {
            System.out.println("Generation: " + generation + " Best fitness: " + population.getFittest(0).getFitness());
            if (generation == 5) {

                System.out.println("hola");
            }
            population = ga.crossoverPopulation(population);
            //     printPopulationDetails(population);
            population = ga.mutatePopulation(population,airportToFlights);
            //    System.out.println("Post-mutation population state:");
            //        printPopulationState(population);
//            printPopulationDetails(population);

            ga.evalPopulation(population, flights, packages, airportToFlights, packageFlights,airportToContinentTimezoneData);

            generation++;

        }

        // Reportar las mejores rutas para cada paquete
        System.out.println("\nMejores rutas para cada paquete:");
        for (PaqueteDTO pkg : packages) {
            List<FlightDTO> bestRoute = packageFlights.get(pkg.getPackageId());
            System.out.println("Mejor ruta para el paquete " + pkg.getPackageId() + ":");
            if (bestRoute != null && !bestRoute.isEmpty()) {
                for (FlightDTO flight : bestRoute) {
                    System.out.println("   " + flight.getOrigin() + " -> " + flight.getDestination() +
                            " (Salida: " + flight.getDepartureTime() + " - Llegada: " + flight.getArrivalTime() + ")");
                }
            } else {
                System.out.println("   No se encontró una ruta adecuada para este paquete.");
            }
        }

        //   ga.reportFinalBestIndividual(flights, flightCapacitiesUsed);



        */
    }

    private static void reportBestRouteForPackage(String packageId, List<FlightDTO> route) {
        System.out.println("Mejor ruta para el paquete " + packageId + ":");

        if (route == null) {
            System.out.println("No se encontró una ruta para este paquete.");
        } else {
            if (route.isEmpty()) {
                System.out.println("La ruta está vacía.");
            } else {
                System.out.println("La ruta es:");
                for (FlightDTO flight : route) {
                    System.out.println(flight.getOrigin() + " -> " + flight.getDestination() +
                            " (Salida: " + flight.getDepartureTime() + " - Llegada: " + flight.getArrivalTime() + ")");
                }
            }
        }
    }

    public  static void printPopulationDetails(Population population) {
        //   System.out.println("Current Population State:");
        for (int i = 0; i < population.size(); i++) {
            //  Individual individual = population.getIndividual(i);
            //  System.out.println("Individual " + (i + 1) + ":");
            //  printIndividualRoute(individual);
            //  System.out.println("Fitness: " + individual.getFitness());
            //  System.out.println("--------------------------------");
        }
    }

    private static void printIndividualRoute(Individual individual) {
        List<FlightDTO> chromosome = individual.getChromosome();  // Obtén la lista de vuelos (la ruta del individuo)
        if (chromosome.isEmpty()) {
            System.out.println("  No route available.");
        } else {
            System.out.println("  Route:");
            for (FlightDTO flight : chromosome) {
                System.out.println("    " + flight.getOrigin() + " -> " + flight.getDestination() +
                        " (Departure: " + flight.getDepartureTime() + " - Arrival: " + flight.getArrivalTime() + ")");
            }
        }
    }

    private static long calculateFlightDuration(FlightDTO flight) {
        LocalTime departureTime = flight.getDepartureTime();
        LocalTime arrivalTime = flight.getArrivalTime();
        long duration = Duration.between(departureTime, arrivalTime).toMinutes();
        if (duration < 0) {
            duration += 1440; // Agrega 24 horas en minutos si el tiempo es negativo
        }
        return duration;
    }

    private static long calculateRouteDurationMinutes(List<FlightDTO> flightRoute) {
        long totalDuration = 0;
        for (FlightDTO flight : flightRoute) {
            totalDuration += calculateFlightDuration(flight);
        }
        return totalDuration;
    }

    private static Map<String, List<FlightDTO>> selectRoutesForPackages(Map<PaqueteDTO, List<List<FlightDTO>>> packageRoutes, Map<FlightDTO, Integer> flightCapacitiesUsed) {
        Map<String, List<FlightDTO>> selectedRoutes = new HashMap<>();
        for (Map.Entry<PaqueteDTO, List<List<FlightDTO>>> entry : packageRoutes.entrySet()) {
            PaqueteDTO pack = entry.getKey();
            List<List<FlightDTO>> routes = entry.getValue();

            List<FlightDTO> selectedRoute = selectOptimalRoute(routes, flightCapacitiesUsed, pack);
            if (selectedRoute != null) {
                selectedRoutes.put(pack.getPackageId(), selectedRoute);
            } else {
                System.out.println("No hay rutas disponibles para el paquete " + pack.getPackageId());
                // También puedes agregar lógica adicional aquí si es necesario
            }
        }
        return selectedRoutes;
    }

    private static List<FlightDTO> selectOptimalRoute(List<List<FlightDTO>> routes, Map<FlightDTO, Integer> flightCapacitiesUsed, PaqueteDTO pack) {
        List<FlightDTO> validRoute = null;
        int minimumCapacityIncrease = Integer.MAX_VALUE;
        boolean routeFound = false;

        for (List<FlightDTO> route : routes) {
            AirportService airportService = new AirportService();
            AirportDTO destination = airportService.get(pack.getDestinationId());
            CityService cityService = new CityService();
            CityDTO city = cityService.get(destination.getCityId());
            
            if (route.isEmpty() || !route.get(route.size() - 1).getDestination().equals(city.getNombre())) {
                continue; // Ignorar rutas que no terminan en el destino correcto
            }

            boolean hasEnoughCapacity = true;
            int totalCapacityNeeded = 0;

            for (FlightDTO flight : route) {
                int currentCapacity = flightCapacitiesUsed.getOrDefault(flight, 0);
                int additionalCapacityNeeded = pack.getQuantity();

                if (currentCapacity + additionalCapacityNeeded > flight.getCapacity()) {
                    hasEnoughCapacity = false;
                    break;
                }
            }

            if (hasEnoughCapacity && (validRoute == null || route.size() < validRoute.size())) {
                validRoute = route; // Seleccionar la ruta válida con menos vuelos
                routeFound = true;
            }
        }

        if (!routeFound) {
            System.out.println("No se encontraron rutas disponibles para el paquete " + pack.getPackageId());
        }

        if (validRoute != null) {
            for (FlightDTO flight : validRoute) {
                int currentCapacity = flightCapacitiesUsed.getOrDefault(flight, 0);
                flightCapacitiesUsed.put(flight, currentCapacity + pack.getQuantity());
            }
        }

        return validRoute;
    }

    private static int calculateTotalDuration(List<FlightDTO> route) {
        return route.stream().mapToInt(FlightDTO::getDuration).sum();
    }

    private static void printPackageRoutes(Map<PaqueteDTO, List<List<FlightDTO>>> packageRoutes) {
        for (Map.Entry<PaqueteDTO, List<List<FlightDTO>>> entry : packageRoutes.entrySet()) {
            PaqueteDTO pack = entry.getKey();
            List<List<FlightDTO>> routes = entry.getValue();

            AirportService airportService = new AirportService();
            AirportDTO origin = airportService.get(pack.getOriginId());
            CityService cityService = new CityService();
            CityDTO cityOrigin = cityService.get(origin.getCityId());

            AirportDTO destination = airportService.get(pack.getDestinationId());
            CityDTO cityDestination = cityService.get(destination.getCityId());
                        
            System.out.println("Rutas para el paquete " + pack.getPackageId() + " desde " + cityOrigin.getNombre() + " a " + cityDestination.getNombre() + ":");

            if (routes.isEmpty()) {
                System.out.println("  No hay rutas disponibles.");
            } else {
                for (List<FlightDTO> route : routes) {
                    System.out.print("  Ruta: ");
                    for (FlightDTO flight : route) {
                        long duration = calculateFlightDuration(flight);
                        System.out.print(flight.getOrigin() + " -> " + flight.getDestination() + " (" + flight.getDepartureTime() + " - " + flight.getArrivalTime() + ", " + "Duración: " + duration + " mins, Capacidad: " + flight.getCapacity() + "), ");
                    }
                    System.out.println(); // Nueva línea al final de cada ruta
                }
            }
            System.out.println(); // Espacio extra entre paquetes
        }
    }


    private static Map<String, List<FlightDTO>> buildAirportToFlightsMap(List<FlightDTO> flights) {
        Map<String, List<FlightDTO>> airportToFlights = new HashMap<>();
        for (FlightDTO flight : flights) {
            airportToFlights.computeIfAbsent(flight.getOrigin(), k -> new ArrayList<>()).add(flight);
        }
        return airportToFlights;
    }

    private static Map<PaqueteDTO, List<List<FlightDTO>>> generatePossibleRoutesForPackages(List<PaqueteDTO> packages, Map<String, List<FlightDTO>> airportToFlights, int maxStops) {
        Map<PaqueteDTO, List<List<FlightDTO>>> packageRoutes = new HashMap<>();

        for (PaqueteDTO pack : packages) {
            AirportService airportService = new AirportService();
            AirportDTO origin = airportService.get(pack.getOriginId());
            CityService cityService = new CityService();
            CityDTO cityOrigin = cityService.get(origin.getCityId());

            AirportDTO destination = airportService.get(pack.getDestinationId());
            CityDTO cityDestination = cityService.get(destination.getCityId());
             

            List<List<FlightDTO>> possibleRoutes = findRoutes(cityOrigin.getNombre(),cityDestination.getNombre(), airportToFlights, maxStops);

            // Verificar si se encontraron rutas posibles
            if (!possibleRoutes.isEmpty()) {
                packageRoutes.put(pack, possibleRoutes);
            } else {
                System.out.println("No se encontraron rutas posibles para el paquete con ID: " + pack.getPackageId());
            }

        }
        return packageRoutes;
    }


    private static List<List<FlightDTO>> findRoutes(String origin, String destination, Map<String, List<FlightDTO>> airportToFlights, int maxStops) {
        List<List<FlightDTO>> routes = new ArrayList<>();
        Set<String> visitedAirports = new HashSet<>();  // Conjunto para mantener registro de aeropuertos visitados
        exploreRoutes(origin, destination, airportToFlights, new ArrayList<>(), routes, visitedAirports);

        // Verificar si hay rutas disponibles
        if (routes.isEmpty()) {
            System.out.println("No hay rutas disponibles de " + origin + " a " + destination);
        }

        return routes;
    }


    private static void exploreRoutes(String current, String destination, Map<String, List<FlightDTO>> airportToFlights, List<FlightDTO> currentRoute, List<List<FlightDTO>> routes, Set<String> visitedAirports) {
        if (current.equals(destination)) {
            if (!currentRoute.isEmpty() && currentRoute.get(currentRoute.size() - 1).getDestination().equals(destination)) {
                routes.add(new ArrayList<>(currentRoute));  // Asegura que la última parada sea el destino
            }
            return;
        }

        if (!airportToFlights.containsKey(current) || visitedAirports.contains(current)) {
            return;
        }

        visitedAirports.add(current);

        for (FlightDTO flight : airportToFlights.get(current)) {
            if (!visitedAirports.contains(flight.getDestination()) && !flight.getDestination().equals(current)) {
                currentRoute.add(flight);
                exploreRoutes(flight.getDestination(), destination, airportToFlights, currentRoute, routes, visitedAirports);
                currentRoute.remove(currentRoute.size() - 1);
            }
        }

        visitedAirports.remove(current);
    }
    private static void assignPackagesToFlights(List<PaqueteDTO> packages, Map<String, List<FlightDTO>> packageFlights, Map<FlightDTO, Integer> flightCapacitiesUsed) {
        for (PaqueteDTO pkg : packages) {
            List<FlightDTO> assignedFlights = packageFlights.get(pkg.getPackageId());
            if (assignedFlights != null && !assignedFlights.isEmpty()) {
                // Obtener la capacidad del paquete (se mantiene constante para cada vuelo)
                int packageCapacity = pkg.getQuantity();

                for (FlightDTO flight : assignedFlights) {
                    // Verificar si hay capacidad disponible en este vuelo
                    int currentCapacity = flightCapacitiesUsed.getOrDefault(flight, 0);
                    int remainingCapacity = flight.getCapacity() - currentCapacity;

                    // Asignar la capacidad del paquete a este vuelo o la capacidad restante, lo que sea menor
                    int packagesToAssign = Math.min(remainingCapacity, packageCapacity);

                    // Asignar la capacidad del paquete a este vuelo
                    flightCapacitiesUsed.put(flight, packagesToAssign);

                    // Reducir la capacidad del paquete en función de la cantidad asignada a este vuelo
                    packageCapacity -= packagesToAssign;

                    // Detener el bucle si la capacidad del paquete llega a cero
                    if (packageCapacity == 0) {
                        break;
                    }
                }
            }
        }
    }


}
