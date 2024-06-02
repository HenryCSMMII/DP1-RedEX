package com.edu.pucp.dp1.redex.model.Algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.Shipment;

public class Population {
    
    private Individual[] individuals;
	
	public Population(int numIndividuals){
		this.individuals = new Individual[numIndividuals];
	}

	public Individual[] getIndividuals() {
		return individuals;
	}

	public void setIndividuals(Individual[] individuals) {
		this.individuals = individuals;
	}
	
	public void initialize(List<Shipment> original) {

		for(int i=0; i<this.individuals.length;i++) {
			
			List<Shipment> listShipment = new ArrayList<Shipment>();

			for(int j=0; j<original.size();j++) {
				listShipment.add(new Shipment(original.get(j)));
			}
			this.individuals[i] = new Individual();
			this.individuals[i].setListShipments(listShipment);
			this.individuals[i].initialize();
		}
	}

	public Individual[] selectionRoulette(Population population){

		Individual[] parents = new Individual[2];
		int pos1 = 0;
		int pos2 = 0;
		double sum = 0;
		double fitness, best;
		Random random = new Random();
		
		for (int i = 0; i < population.getIndividuals().length; i++) {
			sum += population.getIndividuals()[i].getFitness(i);
		}
		fitness = 0;
		best = random.nextDouble()*sum;
		
		for (int i = 0; i < population.getIndividuals().length; i++) {
			fitness += population.getIndividuals()[i].getFitness(i);
			
			if(fitness > best) {
				pos1 = i;
				break;
			}			
		}
		
		sum -= population.getIndividuals()[pos1].getFitness(pos1);
		best = random.nextDouble()*sum;
		fitness = 0;
		
		for (int i = 0; i < population.getIndividuals().length; i++) {
			if(i != pos1) {
				fitness += population.getIndividuals()[i].getFitness(i);
				
				if(fitness > best) {
					pos2 = i;
					break;
				}
			}
		}
		parents[0] = population.getIndividuals()[pos1];
		parents[1] = population.getIndividuals()[pos2];
		
		return parents;
	}
	
	public Individual[] selectionTournment(Population population, double percentage) {

		Individual[] parents = new Individual[2];		
		int pos1 = 0;
		int pos2 = 0;
		int popSize, tournSize;
		double maxFit;
		Random random = new Random();
		
		popSize = population.getIndividuals().length;
		tournSize = (int) (percentage*popSize/100);
		
		Individual[] pool = new Individual[tournSize];
		
		for (int i = 0; i < tournSize; i++) {
			int random_index = random.nextInt(popSize);
			pool[i] = population.getIndividuals()[random_index];
		}
		
		double sum_fitness = 0;
		for (int i = 0; i < pool.length; i++) {
			sum_fitness += pool[i].getFitness(i);
		}
		maxFit = -1;
		
		for (int i = 0; i < pool.length; i++) {
			if (pool[i].getFitness(i) > maxFit) {
				maxFit = pool[i].getFitness(i);
				pos1 = i;
			}
		}
		
		for (int i = 0; i < tournSize; i++) {
			int random_index = random.nextInt(popSize);
			pool[i] = population.getIndividuals()[random_index];
		}
		maxFit = -1;
		
		for (int i = 0; i < pool.length; i++) {
			if (i != pos1 && pool[i].getFitness(i) > maxFit) {
				maxFit = pool[i].getFitness(i);
				pos2 = i;
			}
		}
		parents[0] = population.getIndividuals()[pos1];
		parents[1] = population.getIndividuals()[pos2];
		
		return parents;
	}
	
	public double[] evaluate() {
		
		double[] fitness = new double[20]; //el doble de la cant de indiv.
		this.initializeFlight();

		for(int i=0;i<this.individuals.length;i++) {
			this.individuals[i].calcFlightCap(i);
			fitness[i] = this.individuals[i].getFitness(i);
		}
		return fitness;
	}
	
	public void initializeFlight() {
		
        List <Flight> itinerario = new ArrayList<Flight>();

		for(int i=0;i<2;i++) {
			for(int j=0;j<=365;j++) {
				for(int k=0; k < BD.list_pool_fligths[i][j].size(); k++) {
					for(int l=0; l < BD.list_pool_fligths[i][j].get(k).getCurrentLoad().length; l++) {
						BD.list_pool_fligths[i][j].get(k).getCurrentLoad()[l] = 0;
					}
				}
			}
		}
	}
}