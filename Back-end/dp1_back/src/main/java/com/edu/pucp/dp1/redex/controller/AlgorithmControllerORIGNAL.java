/*package com.edu.pucp.dp1.redex.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.Algorithm.Individual;
import com.edu.pucp.dp1.redex.Algorithm.Population;
import com.edu.pucp.dp1.redex.dto.FlightDTO;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.services.AlgorithmService;
import com.edu.pucp.dp1.redex.utils.CalendarFlightPool;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/algorithm")
@CrossOrigin
public class AlgorithmController {

    /*@Autowired
    private AlgorithmService algorithmService;

    @PostMapping("/optimizeFlights")
    public List<FlightDTO> optimizeFlights(@RequestBody List<FlightDTO> flights) {
        return algorithmService.optimizeFlights(flights);
    }*QUITARTEXTO/
	@RequestMapping(value="read/", method = RequestMethod.GET)
	public String read() throws IOException{
		FileReader.read_list_airports();
		return null;
	}
	
	@RequestMapping(value="run2/", method = RequestMethod.GET)
	public List<Airport> run2() throws IOException{
		FileReader.read_list_airports();
		FileReader.read_list_flights();
		return BD.airports;
	}
	
	@RequestMapping(value="read2/", method = RequestMethod.GET)
	public String read2() throws IOException{
		FileReader.read_list_flights();
		return null;
	}
	
	@RequestMapping(value="read3/", method = RequestMethod.GET)
	public String read3() throws IOException{
		FileReader.read_list_shipment();
		return null;
	}
	
	@CrossOrigin
	@RequestMapping(value="run/", method = RequestMethod.GET)
	public List<Flight> genetic_algorithm(long date_simulation, int type_simulation){
		
		//FileReader.read_list_airports();
		//FileReader.read_list_flights();
		BD.read_list_shipment();
		CalendarFlightPool.generate_calendar();
		
		int iterGen=0;
		Population population = new Population(10); //cant de individuos
		
		population.initialize(BD.shipmentsTemp);
		population.evaluate();
		
		while(iterGen != 10) { //cant de individuos
		
			List<Individual[]> newParents = new ArrayList<Individual[]>();
			
			for(int i = 0; i < 5; i++) { //cant de individuos % 2
				
				//Individual[] newParent = population.selectionTournament(population,4);
				Individual[] newParent = population.selectionRoulette(population);
				newParents.add(newParent);
			}
			
			List<Individual> newOffspring = new ArrayList<Individual>();
			for(int i=0;i<newParents.size();i++) {
				Individual[] children = newParents.get(i)[0].crossover_uniform(newParents.get(i)[1]);
				newOffspring.add(children[0]);
				newOffspring.add(children[1]);
			}
			
			for(int i=0;i<newOffspring.size();i++) {
				newOffspring.get(i).mutation1(population);
			}
			
			Population tempPopulation = new Population(20); //cant de individuos * 2
			
			for(int i=0, k=0;i<newParents.size();i++, k+=2) {
				System.out.println("i :" + i + " k: " + k);
				tempPopulation.getIndividuals()[k] = newParents.get(i)[0];
				tempPopulation.getIndividuals()[k+1] = newParents.get(i)[1];
			}
			
			for(int i=0;i<newOffspring.size();i++) {
				tempPopulation.getIndividuals()[i+10] = newOffspring.get(i); //cant de individuos
			}

			double[] fitness = tempPopulation.evaluate();
			
			for(int i=0;i<fitness.length;i++) {
				System.out.println(fitness[i]);
			}
			
			double[] fitnessTemp = Arrays.copyOf(fitness, fitness.length);
			Arrays.sort(fitnessTemp);
			int[] listIndex = new int[10]; //cant de individuos
			int j=0;
			int flag=0;

			for(int i = fitnessTemp.length-1; i>=0; i--) {

				if(i == fitnessTemp.length-1-10) break; //cant de individuos
				flag = 0;

				for(int k=0;k<fitness.length;k++) {

					if(fitness[k] == fitnessTemp[i]) {
						for(int l=0;l<listIndex.length;l++) {
							if(listIndex[l] == k) {
								flag = 1;
							}							
						}
						
						if(flag == 1) {
							flag = 0;
							continue;
						}
						listIndex[j] = k;
						break;
					}
				}
				j++;
			}
			
			//Population population_selected = new Population(geneticParameters.POPULATION_NUM_INDIVIDUALS);
			//for(int i=0;i<10;i++) {
			//	population_selected.getIndividuals() = tempPopulation.getIndividuals()
			//}

			population = new Population(10); //cant de individuos
			for(int i=0;i<10;i++) { //cant de individuos
				population.getIndividuals()[i] = tempPopulation.getIndividuals()[listIndex[i]];
			}
	
			for(int k=0;k<2;k++) {
				for(int l=0;l<=365;l++) {
					for(int m=0;m<BD.flights[k][l].size();m++) {
						for(int n=0;n<BD.flights[k][l].get(m).getCurrentLoad().length;n++) {
							if(n==10) break; //cant de individuos
							BD.flights[k][l].get(m).getCurrentLoad()[n]= 
                                BD.flights[k][l].get(m).getCurrentLoad()[listIndex[n]];
						}
					}
				}
			}
			iterGen+=1;
		}
		/*
		int num=0;
		
		for(int i=0;i<population.getIndividuals().length;i++) {
			for(int k=0;k<population.getIndividuals()[i].getList_flight_schedule().size();k++) {
				if(population.getIndividuals()[i].getList_flight_schedule().get(k).getFlights().size() > 1) {
					System.out.println("SI TIENE ESCALASSSSSSS: " + population.getIndividuals()[i].getList_flight_schedule().get(k).getFlights().size());
					num+=1;
				}
			}
		}*QUITARTEXTO/
		
		Individual bestIndividual = population.getIndividuals()[0];
		
		if(bestIndividual.getFitness(0) < 0.01) {
			System.out.println("No se ha encontrado un plan de vuelos");
			return null;
		}
		
		System.out.println("Total de envios sin solucion "+ BD.shipments.size());
		for (int i = 0; i < BD.shipments.size(); i++) {
			if(BD.shipments.get(i).getCantidad() >= 25) {
				System.out.println("SALIDA: " + BD.shipments.get(i).getOrigen().getCodigoIATA());
				System.out.println("LLEGADA: " + BD.shipments.get(i).getDestino().getCodigoIATA());
			}
		}
		
		System.out.println("EL FITNESS MEJOR ES: " +  population.getIndividuals()[0].getFitness(0));
		//return population.getIndividuals()[0];
		//292-297
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][293]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][294]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][295]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][296]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][297]);
		//return GeneralData.list_pool_fligths[0][292];
		
		/********************************* DAYS OF THE YEAR - ARRAY ***************************************QUITARTEXTO/
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate arrDate = LocalDate.parse(LocalDate.ofEpochDay(dateSim / (24 * 60 * 60 * 1000)).toString(), formatter);
				
		int dd = arrDate.getDayOfYear();
		System.out.println("DIA DEL AÑO: " + (dd));
		
		int yy;
		System.out.println("AÑO DE LA FECHA: " + arrDate.getYear());
		
		if(arrDate.getYear() == 2022) { yy = 0; }
		else { yy = 1; }
				
		if(typeSim == 7) { // deberia ser 7 porque es semanal
			for (int i = 0; i < typeSim; i++) {
				// FIN DE AÑO -> verificar casos
				if(dd + i >= 365) {
					BD.flights[yy][dd - 1].addAll(BD.flights[yy + 1][i]);
				}
				else{
					BD.flights[yy][dd - 1].addAll(BD.flights[yy][dd + i]);
				}
			}
		}
		return BD.flights[yy][dd - 1];
	}
}*/