package com.edu.pucp.dp1.redex.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.edu.pucp.dp1.redex.model.Shipment;

public class Population {
	private Individual[] individuals;
	
	public Population(int num_individuals){
		this.individuals = new Individual[num_individuals];
	}

	public Individual[] getIndividuals() {
		return individuals;
	}

	public void setIndividuals(Individual[] individuals) {
		this.individuals = individuals;
	}
	
	public void initialize(List<Shipment> originalList) {
		
		System.out.println(originalList.size());
		for(int i=0; i<this.individuals.length;i++) {
			
			List<Shipment> shipments = new ArrayList<Shipment>();
			for(int k=0; k<originalList.size();k++) {
				shipments.add(new Shipment(originalList.get(k)));
			}
			this.individuals[i] = new Individual();
			this.individuals[i].setList_shipments(shipments);
			this.individuals[i].initialize();
		}
		
	}


	public void initializePEDRO(List<Shipment> originalList) {
		
		System.out.println(originalList.size());
		for(int i=0; i<this.individuals.length;i++) {
			
			List<Shipment> shipments = new ArrayList<Shipment>();
			for(int k=0; k<originalList.size();k++) {
				shipments.add(new Shipment(originalList.get(k)));
			}
			this.individuals[i] = new Individual();
			this.individuals[i].setList_shipments(shipments);
			this.individuals[i].initialize();
		}
		
	}

	public Individual[] selectionRoullete(Population population){
		Individual[] parents = new Individual[2];
		int indexP1 = 0;
		int indexP2 = 0;
		
		double sumFit = 0;
		double fitness, bestFitness;
		
		Random random = new Random();
		
		// Primer padre
		for (int i = 0; i < population.getIndividuals().length; i++) {
			sumFit += population.getIndividuals()[i].getFitness(i);
			
			System.out.println("Fitness individual = " + population.getIndividuals()[i].getFitness(i));
		}
		
		System.out.println("Suma = " + sumFit);
		
		fitness = 0;
		bestFitness = random.nextDouble()*sumFit;
		
		for (int i = 0; i < population.getIndividuals().length; i++) {
			fitness += population.getIndividuals()[i].getFitness(i);
			
			if(fitness > bestFitness) {
				indexP1 = i;
				break;
			}			
		}
		
		// segundo padre
		sumFit -= population.getIndividuals()[indexP1].getFitness(indexP1);
		bestFitness = random.nextDouble()*sumFit;
		fitness = 0;
		
		for (int i = 0; i < population.getIndividuals().length; i++) {
			if(i != indexP1) {
				fitness += population.getIndividuals()[i].getFitness(i);
				
				if(fitness > bestFitness) {
					indexP2 = i;
					break;
				}
			}
		}
		
		parents[0] = population.getIndividuals()[indexP1];
		parents[1] = population.getIndividuals()[indexP2];
		
		return parents;
	}
	
	public double[] evaluate() {
		
		double[] population_fitness = new double[BD.POPULATION_NUM_INDIVIDUALS*2];
		
		this.initialize_flight_capacity();
		for(int i=0;i<this.individuals.length;i++) {
			this.individuals[i].calculate_flights_capacity(i);
			population_fitness[i] = this.individuals[i].getFitness(i);
		}
		
		return population_fitness;
	}
	
	public void initialize_flight_capacity() {
		
		for(int k=0;k<2;k++) {
			for(int n=0;n<=365;n++) {
				for(int m=0;m<BD.flights[k][n].size();m++) {
					for(int a=0;a<BD.flights[k][n].get(m).getUsed_capacity().length;a++) {
						BD.flights[k][n].get(m).getUsed_capacity()[a]=0;
					}
				}
			}
		}
		
	}
	
}
