package com.edu.pucp.dp1.redex.model;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

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
        List<Flight> flights = null;
        List<Paquete> packages = null;
        List<Airport> airports = null;
        Map<Flight, Integer> flightCapacitiesUsed = new HashMap<>(); // Mapa para rastrear la capacidad usada de cada vuelo

        try {
            flights = DataLoader.loadFlights("C:/Users/henry/OneDrive/Escritorio/DP1/DP1-RedEx/Back-end/dp1_back/src/main/resources/input/planes_vuelo.v3.txt");
            for (Flight flight : flights) {
                flightCapacitiesUsed.put(flight, 0); // Inicializa la capacidad usada de cada vuelo como 0
            }
            packages = DataLoader.loadPackages("C:/Users/henry/OneDrive/Escritorio/DP1/DP1-RedEx/Back-end/dp1_back/src/main/resources/input/pack_enviado_SKBO.txt");
            airports = DataLoader.loadAirports("C:/Users/henry/OneDrive/Escritorio/DP1/DP1-RedEx/Back-end/dp1_back/src/main/resources/input/Aeropuerto.husos.v2.txt");
        } catch (IOException e) {
            System.out.println("Error al leer los archivos: " + e.getMessage());
            e.printStackTrace();
            return; // Salir del programa si hay un error
        }
        Map<String, List<Flight>> airportToFlights = buildAirportToFlightsMap(flights);
        for (Flight flight : flights) {
            flightCapacitiesUsed.put(flight, 0);
            //       System.out.println("Flight from " + flight.getOrigin() + " to " + flight.getDestination() + " initialized with capacity: " + flight.getCapacity() + " and current load set to 0.");
        }

        // Crear mapa para almacenar la relación aeropuerto-continente
        Map<String, String> airportToContinentData = new HashMap<>();
        final int MAX_STOPS = 5; // Puedes ajustar este valor según sea necesario

        // Generar posibles rutas para cada paquete
        Map<Paquete, List<List<Flight>>> packageRoutes = generatePossibleRoutesForPackages(packages, airportToFlights, MAX_STOPS);
        //    printPackageRoutes(packageRoutes);
        Map<String, List<Flight>> packageFlights = selectRoutesForPackages(packageRoutes, flightCapacitiesUsed);

        assignPackagesToFlights(packages, packageFlights, flightCapacitiesUsed);
        //    printAssignedRoutes(packageFlights,flightCapacitiesUsed);

        Map<String, List<Paquete>> packagesByEnvioCode = new HashMap<>();

        assignPackagesToFlights(packages, packageFlights, flightCapacitiesUsed);


        for (Paquete p : packages) {
            packagesByEnvioCode.computeIfAbsent(p.getPackageId(), k -> new ArrayList<>()).add(p);
            //  System.out.println("Agregando paquete " + p.getPackageId() + " al grupo de envío.");
        }

        Map<String, AirportData> airportToContinentTimezoneData = new HashMap<>();

        // Llenar el mapa con datos de aeropuertos
        for (Airport airport : airports) {
            // Obtener datos del aeropuerto
            String continent = airport.getCity().getCountry().getContinent().getName();
            int timezoneOffset = airport.getCity().getZonahoraria();

            // Almacenar los datos en el mapa
            airportToContinentTimezoneData.put(airport.getCodigoIATA(), new AirportData(continent, timezoneOffset));
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
        for (Paquete pkg : packages) {
            List<Flight> bestRoute = packageFlights.get(pkg.getPackageId());
            System.out.println("Mejor ruta para el paquete " + pkg.getPackageId() + ":");
            if (bestRoute != null && !bestRoute.isEmpty()) {
                for (Flight flight : bestRoute) {
                    System.out.println("   " + flight.getOrigin() + " -> " + flight.getDestination() +
                            " (Salida: " + flight.getDepartureTime() + " - Llegada: " + flight.getArrivalTime() + ")");
                }
            } else {
                System.out.println("   No se encontró una ruta adecuada para este paquete.");
            }
        }

        //   ga.reportFinalBestIndividual(flights, flightCapacitiesUsed);




    }

    private static void reportBestRouteForPackage(String packageId, List<Flight> route) {
        System.out.println("Mejor ruta para el paquete " + packageId + ":");

        if (route == null) {
            System.out.println("No se encontró una ruta para este paquete.");
        } else {
            if (route.isEmpty()) {
                System.out.println("La ruta está vacía.");
            } else {
                System.out.println("La ruta es:");
                for (Flight flight : route) {
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
        List<Flight> chromosome = individual.getChromosome();  // Obtén la lista de vuelos (la ruta del individuo)
        if (chromosome.isEmpty()) {
            System.out.println("  No route available.");
        } else {
            System.out.println("  Route:");
            for (Flight flight : chromosome) {
                System.out.println("    " + flight.getOrigin() + " -> " + flight.getDestination() +
                        " (Departure: " + flight.getDepartureTime() + " - Arrival: " + flight.getArrivalTime() + ")");
            }
        }
    }

    private static long calculateFlightDuration(Flight flight) {
        LocalTime departureTime = flight.getDepartureTime();
        LocalTime arrivalTime = flight.getArrivalTime();
        long duration = Duration.between(departureTime, arrivalTime).toMinutes();
        if (duration < 0) {
            duration += 1440; // Agrega 24 horas en minutos si el tiempo es negativo
        }
        return duration;
    }

    private static long calculateRouteDurationMinutes(List<Flight> flightRoute) {
        long totalDuration = 0;
        for (Flight flight : flightRoute) {
            totalDuration += calculateFlightDuration(flight);
        }
        return totalDuration;
    }

    private static Map<String, List<Flight>> selectRoutesForPackages(Map<Paquete, List<List<Flight>>> packageRoutes, Map<Flight, Integer> flightCapacitiesUsed) {
        Map<String, List<Flight>> selectedRoutes = new HashMap<>();
        for (Map.Entry<Paquete, List<List<Flight>>> entry : packageRoutes.entrySet()) {
            Paquete pack = entry.getKey();
            List<List<Flight>> routes = entry.getValue();

            List<Flight> selectedRoute = selectOptimalRoute(routes, flightCapacitiesUsed, pack);
            if (selectedRoute != null) {
                selectedRoutes.put(pack.getPackageId(), selectedRoute);
            } else {
                System.out.println("No hay rutas disponibles para el paquete " + pack.getPackageId());
                // También puedes agregar lógica adicional aquí si es necesario
            }
        }
        return selectedRoutes;
    }

    private static List<Flight> selectOptimalRoute(List<List<Flight>> routes, Map<Flight, Integer> flightCapacitiesUsed, Paquete pack) {
        List<Flight> validRoute = null;
        int minimumCapacityIncrease = Integer.MAX_VALUE;
        boolean routeFound = false;

        for (List<Flight> route : routes) {
            if (route.isEmpty() || !route.get(route.size() - 1).getDestination().equals(pack.getDestination())) {
                continue; // Ignorar rutas que no terminan en el destino correcto
            }

            boolean hasEnoughCapacity = true;
            int totalCapacityNeeded = 0;

            for (Flight flight : route) {
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
            for (Flight flight : validRoute) {
                int currentCapacity = flightCapacitiesUsed.getOrDefault(flight, 0);
                flightCapacitiesUsed.put(flight, currentCapacity + pack.getQuantity());
            }
        }

        return validRoute;
    }

    private static int calculateTotalDuration(List<Flight> route) {
        return route.stream().mapToInt(Flight::getDuration).sum();
    }

    private static void printPackageRoutes(Map<Paquete, List<List<Flight>>> packageRoutes) {
        for (Map.Entry<Paquete, List<List<Flight>>> entry : packageRoutes.entrySet()) {
            Paquete pack = entry.getKey();
            List<List<Flight>> routes = entry.getValue();
            System.out.println("Rutas para el paquete " + pack.getPackageId() + " desde " + pack.getOrigin() + " a " + pack.getDestination() + ":");

            if (routes.isEmpty()) {
                System.out.println("  No hay rutas disponibles.");
            } else {
                for (List<Flight> route : routes) {
                    System.out.print("  Ruta: ");
                    for (Flight flight : route) {
                        long duration = calculateFlightDuration(flight);
                        System.out.print(flight.getOrigin() + " -> " + flight.getDestination() + " (" + flight.getDepartureTime() + " - " + flight.getArrivalTime() + ", " + "Duración: " + duration + " mins, Capacidad: " + flight.getCapacity() + "), ");
                    }
                    System.out.println(); // Nueva línea al final de cada ruta
                }
            }
            System.out.println(); // Espacio extra entre paquetes
        }
    }


    private static Map<String, List<Flight>> buildAirportToFlightsMap(List<Flight> flights) {
        Map<String, List<Flight>> airportToFlights = new HashMap<>();
        for (Flight flight : flights) {
            airportToFlights.computeIfAbsent(flight.getOrigin(), k -> new ArrayList<>()).add(flight);
        }
        return airportToFlights;
    }

    private static Map<Paquete, List<List<Flight>>> generatePossibleRoutesForPackages(List<Paquete> packages, Map<String, List<Flight>> airportToFlights, int maxStops) {
        Map<Paquete, List<List<Flight>>> packageRoutes = new HashMap<>();

        for (Paquete pack : packages) {
            List<List<Flight>> possibleRoutes = findRoutes(pack.getOrigin().getCity().getNombre(), pack.getDestination().getCity().getNombre(), airportToFlights, maxStops);

            // Verificar si se encontraron rutas posibles
            if (!possibleRoutes.isEmpty()) {
                packageRoutes.put(pack, possibleRoutes);
            } else {
                System.out.println("No se encontraron rutas posibles para el paquete con ID: " + pack.getPackageId());
            }

        }
        return packageRoutes;
    }


    private static List<List<Flight>> findRoutes(String origin, String destination, Map<String, List<Flight>> airportToFlights, int maxStops) {
        List<List<Flight>> routes = new ArrayList<>();
        Set<String> visitedAirports = new HashSet<>();  // Conjunto para mantener registro de aeropuertos visitados
        exploreRoutes(origin, destination, airportToFlights, new ArrayList<>(), routes, visitedAirports);

        // Verificar si hay rutas disponibles
        if (routes.isEmpty()) {
            System.out.println("No hay rutas disponibles de " + origin + " a " + destination);
        }

        return routes;
    }


    private static void exploreRoutes(String current, String destination, Map<String, List<Flight>> airportToFlights, List<Flight> currentRoute, List<List<Flight>> routes, Set<String> visitedAirports) {
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

        for (Flight flight : airportToFlights.get(current)) {
            if (!visitedAirports.contains(flight.getDestination()) && !flight.getDestination().equals(current)) {
                currentRoute.add(flight);
                exploreRoutes(flight.getDestination(), destination, airportToFlights, currentRoute, routes, visitedAirports);
                currentRoute.remove(currentRoute.size() - 1);
            }
        }

        visitedAirports.remove(current);
    }
    private static void assignPackagesToFlights(List<Paquete> packages, Map<String, List<Flight>> packageFlights, Map<Flight, Integer> flightCapacitiesUsed) {
        for (Paquete pkg : packages) {
            List<Flight> assignedFlights = packageFlights.get(pkg.getPackageId());
            if (assignedFlights != null && !assignedFlights.isEmpty()) {
                // Obtener la capacidad del paquete (se mantiene constante para cada vuelo)
                int packageCapacity = pkg.getQuantity();

                for (Flight flight : assignedFlights) {
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
