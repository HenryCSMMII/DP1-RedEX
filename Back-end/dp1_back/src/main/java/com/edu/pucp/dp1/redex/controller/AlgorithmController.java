package com.edu.pucp.dp1.redex.controller;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

//import org.json;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.yaml.snakeyaml.util.ArrayUtils;

import com.edu.pucp.dp1.redex.Algorithm.BD;
import com.edu.pucp.dp1.redex.Algorithm.Individual;
import com.edu.pucp.dp1.redex.Algorithm.Population;
import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.FlightSchedule;
import com.edu.pucp.dp1.redex.utils.CalendarFlightPool;
import com.edu.pucp.dp1.redex.utils.FileReader;
import com.edu.pucp.dp1.redex.utils.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
@RestController
@RequestMapping("/api/algorithm/")
public class AlgorithmController {

	@RequestMapping(value="read/", method = RequestMethod.GET)
	public String read() throws IOException{
		FileReader.read_list_airports();
		System.out.println("Acabo de leer lista de aeropuertos");
		return null;
	}
	
	@RequestMapping(value="run2/", method = RequestMethod.GET)
	public List<Flight> run2() throws IOException{
		FileReader.read_list_airports();
		FileReader.read_list_flights();
		return BD.flightsTemp;
	}
	
	@RequestMapping(value="read2/", method = RequestMethod.GET)
	public String read2() throws IOException{
		FileReader.read_list_flights();
		System.out.println("Acabo de leer los vuelos");
		return null;
	}
	
	@RequestMapping(value="read3/", method = RequestMethod.GET)
	public String read3() throws IOException{
		FileReader.read_list_shipment();
		System.out.println("Acabo de leer los envíos");
		return null;
	}
	
	@CrossOrigin
	@RequestMapping(value="run/", method = RequestMethod.GET)
	public List<Flight> genetic_algorithm(long date_simulation,int type_simulation) throws ParseException{
		//long date_simulation = 1659398400000L;
        //int type_simulation=1;
		FileReader.read_list_airports();
		FileReader.read_list_flights();
		FileReader.read_list_shipment_with_date(date_simulation, type_simulation);
		CalendarFlightPool.generate_calendar();
		
		Population population = new Population(BD.POPULATION_NUM_INDIVIDUALS);
		
		population.initialize(BD.shipmentsTemp);
		population.evaluate();

		int num_generations=0;
		int index_best_individual = 0;
		
		// while(num_generations != BD.NUM_GENERATIONS) {
		
		// 	List<Individual[]> new_parents = new ArrayList<Individual[]>();
			
		// 	//System.out.println("============== HOLA ==============");
			
		// 	for(int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS/2; i++) {
		// 		//System.out.println("============== ROULETTE ==============");
				
		// 		//Individual[] new_parents_group = population.selection_parents_tournament(population, 4);
		// 		Individual[] new_parents_group = population.selection_parents_roulette(population);
		// 		new_parents.add(new_parents_group);
		// 	}
			
		// 	List<Individual> new_offspring = new ArrayList<Individual>();
		// 	for(int i=0;i<new_parents.size();i++) {
		// 		//System.out.println("============== CROSSOVER ==============");
		// 		Individual[] children = new_parents.get(i)[0].crossover_uniform(new_parents.get(i)[1]);
		// 		new_offspring.add(children[0]);
		// 		new_offspring.add(children[1]);
		// 	}
			
		// 	for(int i=0;i<new_offspring.size();i++) {
		// 	//	//System.out.println("============== MUTATION 1 ==============");
		// 		new_offspring.get(i).mutation1(population);
		// 	}
			
		// 	Population population_temp = new Population(BD.POPULATION_NUM_INDIVIDUALS*2);
			
		// 	for(int i=0, k=0;i<new_parents.size();i++, k+=2) {
		// 		System.out.println("i :" + i + " k: " + k);
		// 		population_temp.getIndividuals()[k] = new_parents.get(i)[0];
		// 		population_temp.getIndividuals()[k+1] = new_parents.get(i)[1];
		// 	}
			
		// 	for(int i=0;i<new_offspring.size();i++) {
		// 		population_temp.getIndividuals()[i+BD.POPULATION_NUM_INDIVIDUALS] = new_offspring.get(i);
		// 	}
			
		// 	//Seleccionar los 10 mejores
		// 	double[] list_fitness = population_temp.evaluate();
			
		// 	System.out.println("==========================");
		// 	for(int i=0;i<list_fitness.length;i++) {
		// 		System.out.println(list_fitness[i]);
		// 	}
		// 	System.out.println("==========================");
			
			
		// 	double[] list_fitness_temp = Arrays.copyOf(list_fitness, list_fitness.length);

		// 	Arrays.sort(list_fitness_temp);
			
		// 	int[] list_index = new int[BD.POPULATION_NUM_INDIVIDUALS];
			
		// 	int j=0;
		// 	int flag_exist=0;
		// 	for(int i=list_fitness_temp.length-1;i>=0;i--) {
		// 		if(i ==  list_fitness_temp.length-1-BD.POPULATION_NUM_INDIVIDUALS) break;
		// 		flag_exist = 0;
		// 		for(int k=0;k<list_fitness.length;k++) {
		// 			if(list_fitness[k] == list_fitness_temp[i]) {
		// 				for(int m=0;m<list_index.length;m++) {
		// 					if(list_index[m] == k) {
		// 						flag_exist = 1; //significa que este indice ya existe uwu
		// 					}							
		// 				}
		// 				if(flag_exist == 1) {
		// 					flag_exist = 0;
		// 					continue;
		// 				}
		// 				list_index[j] = k;
		// 				break;
		// 			}
		// 		}
		// 		j+=1;
		// 	}
			
		// 	//Population population_selected = new Population(geneticParameters.POPULATION_NUM_INDIVIDUALS);
		// 	//for(int i=0;i<10;i++) {
		// 	//	population_selected.getIndividuals() = population_temp.getIndividuals()
		// 	//}

		// 	population = new Population(BD.POPULATION_NUM_INDIVIDUALS);
		// 	for(int i=0;i<BD.POPULATION_NUM_INDIVIDUALS;i++) {
		// 		population.getIndividuals()[i] = population_temp.getIndividuals()[list_index[i]];
		// 	}
	
		// 	for(int k=0;k<2;k++) {
		// 		for(int n=0;n<=365;n++) {
		// 			for(int m=0;m<BD.flights[k][n].size();m++) {
		// 				for(int a=0;a<BD.flights[k][n].get(m).getUsed_capacity().length;a++) {
		// 					if(a==BD.POPULATION_NUM_INDIVIDUALS) break;
		// 					    BD.flights[k][n].get(m).getUsed_capacity()[a]= 
        //                         BD.flights[k][n].get(m).getUsed_capacity()[list_index[a]];
		// 				}
		// 			}
		// 		}
		// 	}

		// 	//los guardo en una nueva poblacion // los 10 mejores descendientes
 
		// 	num_generations+=1;

		// }
		// /*
		// int num=0;
		
		// for(int i=0;i<population.getIndividuals().length;i++) {
		// 	for(int k=0;k<population.getIndividuals()[i].getList_flight_schedule().size();k++) {
		// 		if(population.getIndividuals()[i].getList_flight_schedule().get(k).getFlights().size() > 1) {
		// 			System.out.println("SI TIENE ESCALASSSSSSS: " + population.getIndividuals()[i].getList_flight_schedule().get(k).getFlights().size());
		// 			num+=1;
		// 		}
		// 	}
		// }*/
		
		// Individual best_individual = population.getIndividuals()[0];
		
		// if(best_individual.getFitness(0) < 0.01) {
		// 	System.out.println("No hay solucion");
		// 	return null;
		// }
		
		// System.out.println("Total de envios sin solucion "+ BD.shipments.size());
		// for (int i = 0; i < BD.shipments.size(); i++) {
		// 	if(BD.shipments.get(i).getPackageQuantity() >= 25) {
		// 		System.out.println("SALIDA: " + BD.shipments.get(i).getDepartureAirport().getCode());
		// 		System.out.println("LLEGADA: " + BD.shipments.get(i).getArrivalAirport().getCode());
		// 	}
		// }
		
		// System.out.println("EL FITNESS MEJOR ES: " +  population.getIndividuals()[0].getFitness(0));
		//return population.getIndividuals()[0];
		//292-297
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][293]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][294]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][295]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][296]);
		//GeneralData.list_pool_fligths[0][292].addAll(GeneralData.list_pool_fligths[0][297]);
		//return GeneralData.list_pool_fligths[0][292];
		
		/********************************* DAYS OF THE YEAR - ARRAY ***************************************/
		DateFormat formater_date = new SimpleDateFormat("yyyy-MM-dd");
		LocalDate date_array = LocalDate.parse(formater_date.format(new Date(date_simulation)));
		System.out.println("FECHA: " + date_array);
		int day_of_year = date_array.getDayOfYear();
		System.out.println("DIA DEL AÑO: " + (day_of_year));
		
		int year_of_date;
		System.out.println("AÑO DE LA FECHA: " + date_array.getYear());
		
		if(date_array.getYear() == 2024) { year_of_date = 0; }
		else { year_of_date = 1; }
				
		if(type_simulation == 5) {
			for (int i = 0; i < type_simulation; i++) {
				System.out.println("DIA DEL AÑO: " + (day_of_year + i));
				// FIN DE AÑO -> verificar casos
				if(day_of_year + i >= 365) {
					BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date + 1][i]);
				}
				else{
					BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date][day_of_year + i]);
				}
			}
		}
		
        System.out.println("funciona");
		
        System.out.println("NUMERO DE ITINERARIOS: "+population.getIndividuals()[0].getList_flight_schedule().size());
        System.out.println("NUMERO DE PAQUETES: "+population.getIndividuals()[0].getList_shipments().size());
        
        return BD.flights[year_of_date][day_of_year - 1];
        //return population.getIndividuals()[0].getList_flight_schedule();
    }
	
	@RequestMapping(value="test/", method = RequestMethod.GET)
	public List<Flight> test(){
		FileReader.read_list_airports();
		FileReader.read_list_flights();
		CalendarFlightPool.generate_calendar();
		
		//System.out.println("WENAS: "+ GeneralData.list_pool_fligths[1][20].getArrival_date_time().getTime());
		
		return BD.flights[0][213];
	}
	
	
	
	@RequestMapping(value="read_files/{date_simulation}/{type_simulation}", method = RequestMethod.GET)
	public void read_files(@PathVariable("date_simulation") long date_simulation, 
							@PathVariable("type_simulation") int type_simulation) throws ParseException{
		FileReader.read_list_airports();
		FileReader.read_list_flights();
		//FileReader.read_list_shipment();
		FileReader.read_list_shipment_with_date(date_simulation, type_simulation);
		CalendarFlightPool.generate_calendar();
		return;
	}
	
	@CrossOrigin
	@RequestMapping(value="get_simulation_flights/{date_simulation}/{type_simulation}", method = RequestMethod.GET)
	public List<ObjectNode> get_simulation_fligths(@PathVariable("date_simulation") long date_simulation, 
													@PathVariable("type_simulation") int type_simulation) throws JsonProcessingException, ParseException{
		
		// Tiempo de ejecucion del algoritmo => TA = 5min
		// Salto de cada ejecucion => SA = 10min
		// Proporcionalidad de tiempo => PT
		// PT = 1 -> Operaciones diarias
		// PT = algo que debemos descubrir -> Simulacion 5 dias
		// PT = algo que debemos descubrir -> Simulacion colapso
		
		// FileReader.read_list_airports();
		// FileReader.read_list_flights();
		
		List<Flight> list_flights = genetic_algorithm(date_simulation, type_simulation);
			
		List<ObjectNode> result_api = JsonFormat.convertFlightToJSON(list_flights);
		System.out.println("ESTA ES LA FECHA EN MS POR PARAMETRO: " + date_simulation);
		
		return result_api;
	}
	
}
