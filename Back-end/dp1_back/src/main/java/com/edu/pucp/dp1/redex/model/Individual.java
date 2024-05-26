package com.edu.pucp.dp1.redex.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

public class Individual<T> {

    private List<T> chromosome;
    private static double fitness = 0.0;

    public Individual() {
        this.chromosome = new ArrayList<>();
    }

    public void randomizeChromosome(Map<Paquete, List<List<Flight>>> packageRoutes) {
        Random rand = new Random();
        List<Paquete> packages = new ArrayList<>(packageRoutes.keySet());
        Paquete pack = packages.get(rand.nextInt(packages.size()));
        List<List<Flight>> validRoutes = packageRoutes.get(pack).stream()
                .filter(route -> isValidRoute(route, pack.getOrigin(), pack.getDestination()))
                .collect(Collectors.toList());

        if (!validRoutes.isEmpty()) {
            List<Flight> selectedRoute = validRoutes.get(rand.nextInt(validRoutes.size()));
            this.setChromosome((List<T>) selectedRoute);
        } else {
            this.setChromosome(new ArrayList<>());  // Si no hay rutas válidas, asigna una lista vacía.
        }
    }
    private boolean isValidRoute(List<Flight> route, String origin, String destination) {
        if (route.isEmpty()) {
            return false;
        }
        return route.get(0).getOrigin().equals(origin) && route.get(route.size() - 1).getDestination().equals(destination);
    }


    public List<T> getChromosome() {
        return new ArrayList<>(chromosome);
    }

    public void setChromosome(List<T> chromosome) {
        if (chromosome == null) {
            throw new IllegalArgumentException("Chromosome cannot be null");
        }
        this.chromosome = new ArrayList<>(chromosome);
    }

    public static double getFitness() {
        return fitness;
    }

    public void setFitness(double fitness) {
        this.fitness = fitness;
    }

    public Individual<T> clone() {
        Individual<T> clone = new Individual<>();
        clone.setChromosome(this.getChromosome());
        clone.setFitness(this.fitness);
        return clone;
    }

    public int getChromosomeLength() {
        return chromosome.size();
    }

    public static double getFitness(Object o) {
        return fitness;
    }


}
