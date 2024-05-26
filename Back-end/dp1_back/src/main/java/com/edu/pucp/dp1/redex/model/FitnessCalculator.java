package com.edu.pucp.dp1.redex.model;

import java.util.List;

public class FitnessCalculator {

    public double calculateFitness(Individual individual, List<Flight> flights, List

            <Paquete> packages) {
        double fitness = 0;

        // Cumplimiento de plazos
        for (Paquete pkg : packages) {
            Flight assignedFlight = findFlightForPackage(individual, pkg, flights);
            if (assignedFlight != null && isOnTime(pkg, assignedFlight)) {
                fitness += 10; // Suma puntos si el paquete llega a tiempo
            } else {
                fitness -= 5; // Penalización si el paquete llega tarde
            }
        }

        // Optimización de la capacidad de carga
        for (Flight flight : flights) {
            int totalPackagesOnFlight = countPackagesOnFlight(individual, flight, packages);
            if (totalPackagesOnFlight <= flight.getCapacity()) {
                fitness += 5; // Suma puntos si el vuelo utiliza su capacidad eficientemente
            } else {
                fitness -= 10; // Penalización severa si se excede la capacidad
            }
        }

        // Minimización del tiempo de tránsito
        double totalTransitTime = calculateTotalTransitTime(individual, packages, flights);
        fitness -= totalTransitTime; // Menos tiempo de tránsito incrementa la aptitud

        return fitness;
    }

    private boolean isOnTime(Paquete pkg, Flight flight) {
        // Implementar lógica para determinar si el paquete llega a su destino a tiempo
        return true; // Simplificación para el ejemplo
    }

    private Flight findFlightForPackage(Individual individual, Paquete pkg, List<Flight> flights) {
        // Implementar lógica para encontrar el vuelo asignado a un paquete según el individuo
        return flights.get(0); // Simplificación para el ejemplo
    }

    private int countPackagesOnFlight(Individual individual, Flight flight, List<Paquete> packages) {
        // Implementar lógica para contar cuántos paquetes están asignados a un vuelo
        return 0; // Simplificación para el ejemplo
    }

    private double calculateTotalTransitTime(Individual individual, List<Paquete> packages, List<Flight> flights) {
        // Implementar lógica para calcular el tiempo total de tránsito de todos los paquetes
        return 0.0; // Simplificación para el ejemplo
    }

}
