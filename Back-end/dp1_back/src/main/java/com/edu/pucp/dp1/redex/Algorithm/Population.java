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
	
	public void initialize(List<Shipment> list_of_shipments_original) {
		
		System.out.println(list_of_shipments_original.size());
		for(int i=0; i<this.individuals.length;i++) {
			
			List<Shipment> list_of_shipments = new ArrayList<Shipment>();
			for(int k=0; k<list_of_shipments_original.size();k++) {
				list_of_shipments.add(new Shipment(list_of_shipments_original.get(k)));
			}
			this.individuals[i] = new Individual();
			this.individuals[i].setList_shipments(list_of_shipments);
			this.individuals[i].initialize();
		}
		
	}
	
	// ---------------------- PARENT SELECTION ---------------------- //
	public Individual[] selection_parents_roulette(Population population){
		Individual[] parents = new Individual[2];
		int indexP1 = 0;
		int indexP2 = 0;
		
		double sum_fitness = 0;
		double cum_fitness, pick_fitness;
		
		Random random = new Random();
		
		// Selection first parent
		for (int i = 0; i < population.getIndividuals().length; i++) {
			sum_fitness += population.getIndividuals()[i].getFitness(i);
			
			System.out.println("fitness individual = " + population.getIndividuals()[i].getFitness(i));
		}
		
		System.out.println("sum_fitness = " + sum_fitness);
		
		cum_fitness = 0;
		pick_fitness = random.nextDouble()*sum_fitness;
		
		for (int i = 0; i < population.getIndividuals().length; i++) {
			cum_fitness += population.getIndividuals()[i].getFitness(i);
			
			if(cum_fitness > pick_fitness) {
				indexP1 = i;
				break;
			}			
		}
		
		// Selection second parent
		sum_fitness = sum_fitness - population.getIndividuals()[indexP1].getFitness(indexP1);
		pick_fitness = random.nextDouble()*sum_fitness;
		cum_fitness = 0;
		
		for (int i = 0; i < population.getIndividuals().length; i++) {
			if(i != indexP1) {
				cum_fitness += population.getIndividuals()[i].getFitness(i);
				
				if(cum_fitness > pick_fitness) {
					indexP2 = i;
					break;
				}
			}
		}
		
		parents[0] = population.getIndividuals()[indexP1];
		parents[1] = population.getIndividuals()[indexP2];
		
		// ----------- POR SI ACASO ----------- //
		//parents[0] = new Individual(population.getIndividuals()[indexP1]);
		//parents[1] = new Individual(population.getIndividuals()[indexP2]);
		
		return parents;
	}
	
	public Individual[] selection_parents_tournament(Population population, double percentage_size) {
		Individual[] parents = new Individual[2];		
		int indexP1 = 0;
		int indexP2 = 0;
		
		int population_size, tournament_size;
		double max_fitness;
		
		Random random = new Random();
		
		population_size = population.getIndividuals().length;
		tournament_size = (int) (percentage_size*population_size/100);
		
		// Select first parent
		Individual[] tournament_pool = new Individual[tournament_size];
		
		for (int i = 0; i < tournament_size; i++) {
			int random_index = random.nextInt(population_size);
			tournament_pool[i] = population.getIndividuals()[random_index];
		}
		
		double sum_fitness = 0;
		for (int i = 0; i < tournament_pool.length; i++) {
			sum_fitness += tournament_pool[i].getFitness(i);
			
			System.out.println("fitness individual = " + tournament_pool[i].getFitness(i));
		}
		System.out.println("sum_fitness = " + sum_fitness);
		
		max_fitness = -1;
		
		for (int i = 0; i < tournament_pool.length; i++) {
			if (tournament_pool[i].getFitness(i) > max_fitness) {
				max_fitness = tournament_pool[i].getFitness(i);
				indexP1 = i;
			}
		}
		
		// Select second parent
		for (int i = 0; i < tournament_size; i++) {
			int random_index = random.nextInt(population_size);
			tournament_pool[i] = population.getIndividuals()[random_index];
		}
		
		max_fitness = -1;
		
		for (int i = 0; i < tournament_pool.length; i++) {
			if (i != indexP1 && tournament_pool[i].getFitness(i) > max_fitness) {
				max_fitness = tournament_pool[i].getFitness(i);
				indexP2 = i;
			}
		}
		
		parents[0] = population.getIndividuals()[indexP1];
		parents[1] = population.getIndividuals()[indexP2];
		
		// ----------- POR SI ACASO ----------- //
		//parents[0] = new Individual(population.getIndividuals()[indexP1]);
		//parents[1] = new Individual(population.getIndividuals()[indexP2]);
		
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
