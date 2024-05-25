package com.edu.pucp.dp1.redex.model.Algorithm;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticAlgorithm {

    private double MUTATION_RATE=0.05;
    private static final long MINUTES_IN_DAY = 24 * 60;
    private double bestFitnessEver = Double.NEGATIVE_INFINITY;
    private Individual bestIndividualEver = null;
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;
    private int elitismCount;
    private int tournamentSize;

    public GeneticAlgorithm(int populationSize, double mutationRate, double crossoverRate, int elitismCount, int tournamentSize) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        this.elitismCount = elitismCount;
        this.tournamentSize = tournamentSize;
    }

    public boolean isTerminationConditionMet(int currentGeneration, int maxGenerations) {
        return currentGeneration > maxGenerations;
    }


    public Population initPopulation(int populationSize, Map<Package, List<List<Flight>>> packageRoutes) {
        Population population = new Population(populationSize);
        for (int i = 0; i < populationSize; i++) {
            Individual individual = new Individual();
            individual.randomizeChromosome(packageRoutes);  // Generar ruta completa para cada individuo
            population.addIndividual(individual);  // Agregar el individuo a la población
        }
        return population;
    }

    public void evalPopulation(Population population, List<Flight> flights, List<Package> packages, Map<String, List<Flight>> packageFlights, Map<String, List<Flight>> airportToFlights, Map<String, AirportData> airportToContinentTimezoneData) {
        double bestFitness = Double.NEGATIVE_INFINITY;
        Individual bestIndividual = null;

        for (int i = 0; i < population.size(); i++) {
            Individual individual = population.getIndividual(i);
            double fitness = calculateFitness(individual, airportToContinentTimezoneData);
            individual.setFitness(fitness);

            // Actualizar el mejor fitness de esta generación si es necesario
            if (fitness > bestFitness) {
                bestFitness = fitness;
                bestIndividual = individual;
            }

            // Verificar si este fitness es el mejor jamás encontrado
            if (fitness > bestFitnessEver) {
                bestFitnessEver = fitness;
                bestIndividualEver = individual.clone();
            }
        }

        population.updatePopulationFitness();

        // Imprimir información sobre el mejor individuo de la generación si es necesario
        if (bestIndividual != null) {
            //   System.out.println("Best individual in generation with fitness: " + bestIndividual.getFitness());
            //   printBestRoute(bestIndividual, packages, packageFlights);
        }

        // Imprimir información sobre el mejor individuo encontrado en todas las generaciones
        if (bestIndividualEver != null) {
            // System.out.println("Best individual ever with fitness: " + bestFitnessEver);
            //  printBestRoute(bestIndividualEver, packages, packageFlights);
        }
    }

    private void printBestRoute(Individual bestIndividual, List<Package> packages, Map<String, List<Flight>> packageFlights) {
        List<Flight> route = bestIndividual.getChromosome();
        if (route.isEmpty()) {
            System.out.println("No available route for the individual.");
        } else {
            System.out.println("Best route for the individual:");
            for (Flight flight : route) {
                System.out.println("   " + flight.getOrigin() + " -> " + flight.getDestination() +
                        " (Departure: " + flight.getDepartureTime() +
                        " - Arrival: " + flight.getArrivalTime() + ")");
            }
        }
        System.out.println("Total Fitness: " + bestIndividual.getFitness());
    }


    private double calculateFitness(Individual individual, Map<String, AirportData> airportToContinentTimezoneData) {
        double fitness = 1000.0;  // Iniciar con un valor alto de fitness.
        long totalTravelTime = calculateTotalTravelTime(individual.getChromosome(), airportToContinentTimezoneData);

        boolean isIntercontinental = isIntercontinental(individual.getChromosome(), airportToContinentTimezoneData);
        long maxDurationAllowed = isIntercontinental ? 2 * 24 * 60 : 24 * 60;  // 2 días para intercontinental, 1 día para intracontinental

        if (!individual.getChromosome().isEmpty()) {
            if (totalTravelTime > maxDurationAllowed) {
                fitness -= (totalTravelTime - maxDurationAllowed) / 60;  // Penalizar por cada hora de retraso sobre el máximo
            }

            fitness -= totalTravelTime / 60;  // Penalizar por la duración del viaje para favorecer rutas más rápidas
        } else {
            fitness -= 1000;  // Penalización grande si no hay rutas disponibles
        }

        return fitness;
    }


    private long calculateTotalTravelTime(List<Flight> route, Map<String, AirportData> airportToContinentTimezoneData) {
        long totalMinutes = 0;
        LocalDateTime previousArrival = null;

        for (Flight flight : route) {
            AirportData originData = airportToContinentTimezoneData.get(flight.getOrigin());
            AirportData destinationData = airportToContinentTimezoneData.get(flight.getDestination());

            // Convertir los tiempos de vuelo a LocalDateTime considerando las zonas horarias
            LocalDateTime departureTime = LocalDateTime.of(LocalDate.now(), flight.getDepartureTime())
                    .plusHours(originData.getTimezoneOffset());
            LocalDateTime arrivalTime = LocalDateTime.of(LocalDate.now(), flight.getArrivalTime())
                    .plusHours(destinationData.getTimezoneOffset());

            // Ajustar la fecha de llegada si el vuelo es nocturno
            if (arrivalTime.isBefore(departureTime)) {
                arrivalTime = arrivalTime.plusDays(1);
            }

            // Agregar el tiempo de espera entre vuelos si es aplicable
            if (previousArrival != null && departureTime.isAfter(previousArrival)) {
                totalMinutes += Duration.between(previousArrival, departureTime).toMinutes();
            }

            // Agregar la duración del vuelo
            totalMinutes += Duration.between(departureTime, arrivalTime).toMinutes();
            previousArrival = arrivalTime;
        }

        return totalMinutes;
    }

    private boolean isIntercontinental(List<Flight> route, Map<String, AirportData> airportToContinentTimezoneData) {
        if (route.isEmpty()) return false;

        String startContinent = airportToContinentTimezoneData.get(route.get(0).getOrigin()).getContinent();
        String endContinent = airportToContinentTimezoneData.get(route.get(route.size() - 1).getDestination()).getContinent();

        return !startContinent.equals(endContinent);
    }

    public void reportFinalBestIndividual(List<Flight> flights, Map<Flight, Integer> flightCapacitiesUsed) {
        System.out.println("Detalles de las rutas del mejor individuo:");
        if (bestIndividualEver != null) {
            System.out.println("Mejor individuo con puntaje de ajuste: " + bestFitnessEver);

            List<Flight> route = bestIndividualEver.getChromosome();
            if (!route.isEmpty()) {
                long totalDuration = calculateTotalRouteDuration(route);
                System.out.printf("Duración total de la ruta: %d mins%n", totalDuration);

                int routeIndex = 0;
                for (Flight flight : route) {
                    long duration = calculateFlightDuration(flight);
                    int usedCapacity = flightCapacitiesUsed.getOrDefault(flight, 0);
                    System.out.printf("Ruta %d: Desde %s hasta %s (%s - %s), Duración: %d mins, Capacidad utilizada: %d/%d%n",
                            ++routeIndex, flight.getOrigin(), flight.getDestination(),
                            flight.getDepartureTime(), flight.getArrivalTime(),
                            duration, usedCapacity, flight.getCapacity());
                }
            } else {
                System.out.println("No hay ruta disponible para el mejor individuo.");
            }
        } else {
            System.out.println("No se encontró el mejor individuo.");
        }
    }

    private int getTotalCapacity(List<Flight> route) {
        int totalCapacity = 0;
        for (Flight flight : route) {
            totalCapacity += flight.getCapacity();
        }
        return totalCapacity;
    }

    private long calculateTotalRouteDuration(List<Flight> route) {
        long totalDuration = 0;
        for (Flight flight : route) {
            totalDuration += calculateFlightDuration(flight);
        }
        return totalDuration;
    }

    private int calculateTotalRemainingCapacity(List<Flight> route, Map<Flight, Integer> flightCapacitiesUsed) {
        int totalRemainingCapacity = 0;
        for (Flight flight : route) {
            int usedCapacity = flightCapacitiesUsed.getOrDefault(flight, 0);
            totalRemainingCapacity += (flight.getCapacity() - usedCapacity);
        }
        return totalRemainingCapacity;
    }



    private static long calculateFlightDuration(Flight flight) {
        LocalTime departureTime = flight.getDepartureTime();
        LocalTime arrivalTime = flight.getArrivalTime();
        long duration = Duration.between(departureTime, arrivalTime).toMinutes();
        if (arrivalTime.isBefore(departureTime)) {
            duration += 1440; // Agregar 24 horas si el vuelo pasa de medianoche
        }
        return duration;
    }

    public Population crossoverPopulation(Population population) {
        Population newPopulation = new Population(population.size());

        for (int i = 0; i < population.size(); i++) {
            Individual parent1 = population.getIndividual(i);

            if (crossoverRate > Math.random() && i >= elitismCount) {
                int partnerIndex = (int) (Math.random() * population.size());
                Individual parent2 = population.getIndividual(partnerIndex);
                Individual offspring = crossoverIndividuals(parent1, parent2);
                newPopulation.setIndividual(i, offspring);
            } else {
                newPopulation.setIndividual(i, parent1.clone()); // Mantener los individuos élite sin cambios
            }
        }

        return newPopulation;
    }

    private Individual crossoverIndividuals(Individual parent1, Individual parent2) {
        // Se asume que cada individuo tiene una lista de vuelos (ruta)
        List<Flight> parent1Chromosome = new ArrayList<>(parent1.getChromosome());
        List<Flight> parent2Chromosome = new ArrayList<>(parent2.getChromosome());

        if (parent1Chromosome.isEmpty() || parent2Chromosome.isEmpty()) {
            return new Individual(); // Si alguno de los padres no tiene ruta, devuelve un individuo vacío
        }

        // Determinar un punto de cruce aleatorio para el cruce
        int crossoverPoint = (int) (Math.random() * Math.min(parent1Chromosome.size(), parent2Chromosome.size()));
        List<Flight> offspringChromosome = new ArrayList<>();

        // Mezcla las rutas de los dos padres
        offspringChromosome.addAll(parent1Chromosome.subList(0, crossoverPoint));
        offspringChromosome.addAll(parent2Chromosome.subList(crossoverPoint, parent2Chromosome.size()));

        // Crear un nuevo individuo con el cromosoma resultante
        Individual offspring = new Individual();
        offspring.setChromosome(offspringChromosome);
        return offspring;
    }


    public Population mutatePopulation(Population originalPopulation, Map<String, List<Flight>> airportToFlights) {
        Population newPopulation = new Population(originalPopulation.size());
        Random rand = new Random();

        for (int i = 0; i < originalPopulation.size(); i++) {
            Individual originalIndividual = originalPopulation.getIndividual(i);
            Individual mutatedIndividual = originalIndividual.clone();
            List<Flight> route = mutatedIndividual.getChromosome();

            if (Math.random() < mutationRate && !route.isEmpty()) {
                int flightIndex = rand.nextInt(route.size());
                Flight currentFlight = route.get(flightIndex);
                List<Flight> possibleFlights = airportToFlights.get(currentFlight.getOrigin());

                if (possibleFlights != null && !possibleFlights.isEmpty()) {
                    Flight newFlight = possibleFlights.get(rand.nextInt(possibleFlights.size()));
                    route.set(flightIndex, newFlight);  // Reemplaza el vuelo en el índice seleccionado

                    // Asegurar que el vuelo nuevo conecte correctamente con el siguiente vuelo
                    if (flightIndex < route.size() - 1) {
                        Flight nextFlight = route.get(flightIndex + 1);
                        if (!newFlight.getDestination().equals(nextFlight.getOrigin())) {
                            Flight connectingFlight = findConnectingFlight(newFlight.getDestination(), nextFlight.getOrigin(), possibleFlights);
                            if (connectingFlight != null) {
                                route.set(flightIndex + 1, connectingFlight); // Establecer el vuelo de conexión
                            }
                        }
                    }
                }
            }
            newPopulation.addIndividual(mutatedIndividual);
        }
        return newPopulation;
    }

    private boolean isValidFlightReplacement(Flight newFlight, List<Flight> currentRoute, int flightIndex, String finalDestination) {
        // Verificar si el nuevo vuelo conecta correctamente con el vuelo anterior y siguiente, y si es el último vuelo en la ruta
        if (flightIndex > 0 && !newFlight.getOrigin().equals(currentRoute.get(flightIndex - 1).getDestination())) {
            return false;
        }
        if (flightIndex < currentRoute.size() - 1 && !newFlight.getDestination().equals(currentRoute.get(flightIndex + 1).getOrigin())) {
            return false;
        }
        if (flightIndex == currentRoute.size() - 1 && !newFlight.getDestination().equals(finalDestination)) {
            return false;
        }
        return true;
    }



    private Flight findConnectingFlight(String currentDestination, String nextOrigin, List<Flight> availableFlights) {
        return availableFlights.stream()
                .filter(flight -> flight.getOrigin().equals(currentDestination) && flight.getDestination().equals(nextOrigin))
                .findFirst()
                .orElse(null);  // Retorna null si no encuentra un vuelo adecuado
    }

    private void printMutationDetails(List<Flight> route, int index, Flight oldFlight, Flight newFlight) {
        System.out.println("Mutation at index " + index + ": " + oldFlight.getOrigin() + "->" + oldFlight.getDestination() +
                " changed to " + newFlight.getOrigin() + "->" + newFlight.getDestination());
    }


}
