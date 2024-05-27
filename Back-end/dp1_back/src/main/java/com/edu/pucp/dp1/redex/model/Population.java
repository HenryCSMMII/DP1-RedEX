package com.edu.pucp.dp1.redex.model;

import java.util.ArrayList;
//import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Population<T> {

    public List<Individual> getIndividuals() {
        return individuals;
    }

    public void setIndividuals(List<Individual> individuals) {
        this.individuals = individuals;
    }

    public double getPopulationFitness() {
        return populationFitness;
    }

    public void setPopulationFitness(double populationFitness) {
        this.populationFitness = populationFitness;
    }

    private List<Individual> individuals;
    private double populationFitness = -1;

    public Population(int populationSize) {
        this.individuals = new ArrayList<>(populationSize);
        for (int i = 0; i < populationSize; i++) {
            individuals.add(new Individual());
        }
    }



    public void printPopulationState() {
        System.out.println("Current Population State:");
        for (int i = 0; i < individuals.size(); i++) {
            Individual individual = individuals.get(i);
            //    System.out.println("Individual " + (i + 1) + ":");
            //    printIndividualRoute(individual);
            //    System.out.println("Fitness: " + individual.getFitness());
            //    System.out.println("--------------------------------");
        }
    }

    private void printIndividualRoute(Individual individual) {
        List<Flight> chromosome = individual.getChromosome();
        if (chromosome.isEmpty()) {
            System.out.println("  No route available.");
        } else {
            for (Flight flight : chromosome) {
                System.out.println("  " + flight.getOrigin() + " -> " + flight.getDestination() +
                        " (Departure: " + flight.getDepartureTime() +
                        " - Arrival: " + flight.getArrivalTime() + ")");
            }
        }
    }

    public void addIndividual(Individual individual) {
        individuals.add(individual);
    }

    public void updatePopulationFitness() {
        double totalFitness = 0;
        for (Individual individual : this.individuals) {
            totalFitness += individual.getFitness();
        }
        this.populationFitness = totalFitness;
    }
    public Individual getIndividual(int index) {
        return individuals.get(index);
    }


    public void setIndividual(int index, Individual individual) {
        individuals.set(index, individual);
    }


    public int size() {
        return individuals.size();
    }

    public void shuffle() {
        //Collections.shuffle(individuals);
    }
    public Individual getFittest(int numFittest) {
        // Ordenar la lista de individuos en orden descendente según su fitness
        //Collections.sort(individuals, Comparator.comparingDouble(Individual::getFitness).reversed());

        // Devolver el i-ésimo individuo, donde i es el valor de numFittest
        return individuals.get(numFittest);
    }


}
