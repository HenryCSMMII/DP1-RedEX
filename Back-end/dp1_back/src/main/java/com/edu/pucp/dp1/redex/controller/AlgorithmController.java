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
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.utils.CalendarFlightPool;
import com.edu.pucp.dp1.redex.utils.JsonFormat;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
@RestController
@RequestMapping("/api/algorithm/")
public class AlgorithmController {

	@RequestMapping(value="read/", method = RequestMethod.GET)
	public String read() throws IOException{
		BD.readContinents();
		BD.readCountries();
		BD.readCities();
		System.out.println("Acabo de leer continentes y paises");
		return null;
	}
	
	@RequestMapping(value="run2/", method = RequestMethod.GET)
	public List<Flight> run2() throws IOException{
		BD.readAirports();
		BD.readFlights();
		return BD.flightsTemp;
	}
	
	@RequestMapping(value="read2/", method = RequestMethod.GET)
	public String read2() throws IOException{
		BD.readFlights();
		System.out.println("Acabo de leer los vuelos");
		return null;
	}
	
	@RequestMapping(value="read3/", method = RequestMethod.GET)
	public String read3() throws IOException{
		long date_simulation = 1722470400000L;
        int type_simulation = 7;
        BD.readAirports();
        BD.readFlights();
        BD.read_list_shipment_with_date(date_simulation, type_simulation); 
		return null;
	}

	@CrossOrigin
@RequestMapping(value="runDiaDia/", method = RequestMethod.GET)
public List<Flight> genetic_algorithm(/*long date_simulation,int type_simulation*/){
    long date_simulation = 1722470400000L;
    int type_simulation = 1;
    int tamanio = 0;
    BD.readAirports();
    BD.readFlights();
    tamanio = BD.read_list_shipment_with_date(date_simulation, type_simulation);
    //CONSIDERAR EL CASO CUANDO SOLO EXISTE UN ARCHIVO EN PACK
	if(tamanio==0){
		System.out.println("No se encontraron nuevos envíos");
		return BD.flightsResolved;
	}
    CalendarFlightPool.generate_calendar();

    Population population = new Population(BD.POPULATION_NUM_INDIVIDUALS);

    population.initialize(BD.shipmentsTemp);
    population.evaluate();

    int num_generations = 0;
    int index_best_individual = 0;

    while (num_generations != BD.NUM_GENERATIONS) {
        System.out.println("Generación número: " + num_generations);

        List<Individual[]> new_parents = new ArrayList<>();

        for (int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS / 2; i++) {
            Individual[] new_parents_group = population.selection_parents_roulette(population);
            new_parents.add(new_parents_group);
        }

        List<Individual> new_offspring = new ArrayList<>();
        for (int i = 0; i < new_parents.size(); i++) {
            Individual[] children = new_parents.get(i)[0].crossover_uniform(new_parents.get(i)[1]);
            new_offspring.add(children[0]);
            new_offspring.add(children[1]);
        }

        for (int i = 0; i < new_offspring.size(); i++) {
            new_offspring.get(i).mutation1(population);
        }

        Population population_temp = new Population(BD.POPULATION_NUM_INDIVIDUALS * 2);

        for (int i = 0, k = 0; i < new_parents.size(); i++, k += 2) {
            System.out.println("i: " + i + " k: " + k);
            population_temp.getIndividuals()[k] = new_parents.get(i)[0];
            population_temp.getIndividuals()[k + 1] = new_parents.get(i)[1];
        }

        for (int i = 0; i < new_offspring.size(); i++) {
            population_temp.getIndividuals()[i + BD.POPULATION_NUM_INDIVIDUALS] = new_offspring.get(i);
        }

        double[] list_fitness = population_temp.evaluate();

        System.out.println("==========================");
        for (int i = 0; i < list_fitness.length; i++) {
            System.out.println("Fitness de individuo " + i + ": " + list_fitness[i]);
        }
        System.out.println("==========================");

        double[] list_fitness_temp = Arrays.copyOf(list_fitness, list_fitness.length);
        Arrays.sort(list_fitness_temp);

        int[] list_index = new int[BD.POPULATION_NUM_INDIVIDUALS];
        int j = 0;
        int flag_exist = 0;
        for (int i = list_fitness_temp.length - 1; i >= 0; i--) {
            if (i == list_fitness_temp.length - 1 - BD.POPULATION_NUM_INDIVIDUALS) break;
            flag_exist = 0;
            for (int k = 0; k < list_fitness.length; k++) {
                if (list_fitness[k] == list_fitness_temp[i]) {
                    for (int m = 0; m < list_index.length; m++) {
                        if (list_index[m] == k) {
                            flag_exist = 1;
                        }
                    }
                    if (flag_exist == 1) {
                        flag_exist = 0;
                        continue;
                    }
                    list_index[j] = k;
                    break;
                }
            }
            j += 1;
        }

        population = new Population(BD.POPULATION_NUM_INDIVIDUALS);
        for (int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS; i++) {
            population.getIndividuals()[i] = population_temp.getIndividuals()[list_index[i]];
        }

        for (int k = 0; k < 2; k++) {
            for (int n = 0; n <= 365; n++) {
                for (int m = 0; m < BD.flights[k][n].size(); m++) {
                    for (int a = 0; a < BD.flights[k][n].get(m).getUsed_capacity().length; a++) {
                        if (a == BD.POPULATION_NUM_INDIVIDUALS) break;
                        BD.flights[k][n].get(m).getUsed_capacity()[a] =
                                BD.flights[k][n].get(m).getUsed_capacity()[list_index[a]];
                    }
                }
            }
        }

        num_generations += 1;
    }

    Individual best_individual = population.getIndividuals()[0];

    if (best_individual.getFitness(0) < 0.01) {
        System.out.println("No hay solución");
        return null;
    }

    System.out.println("Total de envíos sin solución " + BD.shipmentsUnresolved.size());
    for (int i = 0; i < BD.shipmentsUnresolved.size(); i++) {
        if (BD.shipmentsUnresolved.get(i).getPackageQuantity() >= 25) {
            System.out.println("SALIDA: " + BD.shipmentsUnresolved.get(i).getDepartureAirport().getCode());
            System.out.println("LLEGADA: " + BD.shipmentsUnresolved.get(i).getArrivalAirport().getCode());
        }
    }

    System.out.println("EL FITNESS MEJOR ES: " + population.getIndividuals()[0].getFitness(0));

    DateFormat formater_date = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate date_array = LocalDate.parse(formater_date.format(new Date(date_simulation)));

    int day_of_year = date_array.getDayOfYear();
    System.out.println("DIA DEL AÑO: " + (day_of_year));

    int year_of_date;
    System.out.println("AÑO DE LA FECHA: 2024");

    if (date_array.getYear() == 2024) {
        year_of_date = 0;
    } else {
        year_of_date = 1;
    }

    if (type_simulation == 7) {
        for (int i = 0; i < type_simulation; i++) {
            System.out.println("DIA DEL AÑO: " + (day_of_year + i));
            if (day_of_year + i >= 365) {
                BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date + 1][i]);
            } else {
                BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date][day_of_year + i]);
            }
        }
    }

    System.out.println("funciona");

    System.out.println("NUMERO DE ITINERARIOS: " + population.getIndividuals()[0].getList_flight_schedule().size());
    System.out.println("NUMERO DE PAQUETES: " + population.getIndividuals()[0].getList_shipments().size());

    List<Integer> vuelos = new ArrayList<>();
    boolean noEncontrado = true;
    System.out.println("Tamaño: " + tamanio);
    for (int i = 0; i < tamanio; i++) {
        for (Flight flight : population.getIndividuals()[0].getList_flight_schedule().get(i).getFlights()) {
            Integer idVuelo = flight.getId();
            for (int j = 0; j < vuelos.size(); j++) {
                if (vuelos.get(j).equals(idVuelo)) {
                    BD.flightsResolved.get(j).getShipments().add(population.getIndividuals()[0].getList_shipments().get(i));
                    noEncontrado = false;
                    break;
                }
            }
            if (noEncontrado) {
                BD.flightsResolved.add(flight);
                BD.flightsResolved.get(vuelos.size()).setShipments(new ArrayList<>());
                BD.flightsResolved.get(vuelos.size()).getShipments().add(population.getIndividuals()[0].getList_shipments().get(i));
                vuelos.add(idVuelo);
            } else {
                noEncontrado = true;
            }
        }
    }
    System.out.println("ARREGLO VUELOS\n" + vuelos);

    return BD.flightsResolved;
}

@CrossOrigin
@RequestMapping(value="runSemanal/", method = RequestMethod.GET)
public List<Flight> weekly_genetic_algorithm(/*long date_simulation,int type_simulation*/){
    long date_simulation = 1722470400000L;
    int type_simulation = 7;
    int tamanio = 0;
    BD.readAirports();
    BD.readFlights();
    tamanio = BD.read_list_shipment_with_date(date_simulation, type_simulation);
	if(tamanio==0){
		System.out.println("No se encontraron nuevos envíos");
		return BD.flightsResolved;
	}
    CalendarFlightPool.generate_calendar();

    Population population = new Population(BD.POPULATION_NUM_INDIVIDUALS);

    population.initialize(BD.shipmentsTemp);
    population.evaluate();

    int num_generations = 0;
    int index_best_individual = 0;

    while (num_generations != BD.NUM_GENERATIONS) {
        System.out.println("Generación número: " + num_generations);

        List<Individual[]> new_parents = new ArrayList<>();

        for (int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS / 2; i++) {
            Individual[] new_parents_group = population.selection_parents_roulette(population);
            new_parents.add(new_parents_group);
        }

        List<Individual> new_offspring = new ArrayList<>();
        for (int i = 0; i < new_parents.size(); i++) {
            Individual[] children = new_parents.get(i)[0].crossover_uniform(new_parents.get(i)[1]);
            new_offspring.add(children[0]);
            new_offspring.add(children[1]);
        }

        for (int i = 0; i < new_offspring.size(); i++) {
            new_offspring.get(i).mutation1(population);
        }

        Population population_temp = new Population(BD.POPULATION_NUM_INDIVIDUALS * 2);

        for (int i = 0, k = 0; i < new_parents.size(); i++, k += 2) {
            System.out.println("i: " + i + " k: " + k);
            population_temp.getIndividuals()[k] = new_parents.get(i)[0];
            population_temp.getIndividuals()[k + 1] = new_parents.get(i)[1];
        }

        for (int i = 0; i < new_offspring.size(); i++) {
            population_temp.getIndividuals()[i + BD.POPULATION_NUM_INDIVIDUALS] = new_offspring.get(i);
        }

        double[] list_fitness = population_temp.evaluate();

        System.out.println("==========================");
        for (int i = 0; i < list_fitness.length; i++) {
            System.out.println("Fitness de individuo " + i + ": " + list_fitness[i]);
        }
        System.out.println("==========================");

        double[] list_fitness_temp = Arrays.copyOf(list_fitness, list_fitness.length);
        Arrays.sort(list_fitness_temp);

        int[] list_index = new int[BD.POPULATION_NUM_INDIVIDUALS];
        int j = 0;
        int flag_exist = 0;
        for (int i = list_fitness_temp.length - 1; i >= 0; i--) {
            if (i == list_fitness_temp.length - 1 - BD.POPULATION_NUM_INDIVIDUALS) break;
            flag_exist = 0;
            for (int k = 0; k < list_fitness.length; k++) {
                if (list_fitness[k] == list_fitness_temp[i]) {
                    for (int m = 0; m < list_index.length; m++) {
                        if (list_index[m] == k) {
                            flag_exist = 1;
                        }
                    }
                    if (flag_exist == 1) {
                        flag_exist = 0;
                        continue;
                    }
                    list_index[j] = k;
                    break;
                }
            }
            j += 1;
        }

        population = new Population(BD.POPULATION_NUM_INDIVIDUALS);
        for (int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS; i++) {
            population.getIndividuals()[i] = population_temp.getIndividuals()[list_index[i]];
        }

        for (int k = 0; k < 2; k++) {
            for (int n = 0; n <= 365; n++) {
                for (int m = 0; m < BD.flights[k][n].size(); m++) {
                    for (int a = 0; a < BD.flights[k][n].get(m).getUsed_capacity().length; a++) {
                        if (a == BD.POPULATION_NUM_INDIVIDUALS) break;
                        BD.flights[k][n].get(m).getUsed_capacity()[a] =
                                BD.flights[k][n].get(m).getUsed_capacity()[list_index[a]];
                    }
                }
            }
        }

        num_generations += 1;
    }

    Individual best_individual = population.getIndividuals()[0];

    if (best_individual.getFitness(0) < 0.01) {
        System.out.println("No hay solución");
        return null;
    }

    System.out.println("Total de envíos sin solución " + BD.shipmentsUnresolved.size());
    for (int i = 0; i < BD.shipmentsUnresolved.size(); i++) {
        if (BD.shipmentsUnresolved.get(i).getPackageQuantity() >= 25) {
            System.out.println("SALIDA: " + BD.shipmentsUnresolved.get(i).getDepartureAirport().getCode());
            System.out.println("LLEGADA: " + BD.shipmentsUnresolved.get(i).getArrivalAirport().getCode());
        }
    }

    System.out.println("EL FITNESS MEJOR ES: " + population.getIndividuals()[0].getFitness(0));

    DateFormat formater_date = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate date_array = LocalDate.parse(formater_date.format(new Date(date_simulation)));

    int day_of_year = date_array.getDayOfYear();
    System.out.println("DIA DEL AÑO: " + (day_of_year));

    int year_of_date;
    System.out.println("AÑO DE LA FECHA: 2024");

    if (date_array.getYear() == 2024) {
        year_of_date = 0;
    } else {
        year_of_date = 1;
    }

    if (type_simulation == 7) {
        for (int i = 0; i < type_simulation; i++) {
            System.out.println("DIA DEL AÑO: " + (day_of_year + i));
            if (day_of_year + i >= 365) {
                BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date + 1][i]);
            } else {
                BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date][day_of_year + i]);
            }
        }
    }

    System.out.println("funciona");

    System.out.println("NUMERO DE ITINERARIOS: " + population.getIndividuals()[0].getList_flight_schedule().size());
    System.out.println("NUMERO DE PAQUETES: " + population.getIndividuals()[0].getList_shipments().size());

    List<Integer> vuelos = new ArrayList<>();
    boolean noEncontrado = true;
    System.out.println("Tamaño: " + tamanio);
    for (int i = 0; i < tamanio; i++) {
        for (Flight flight : population.getIndividuals()[0].getList_flight_schedule().get(i).getFlights()) {
            Integer idVuelo = flight.getId();
            for (int j = 0; j < vuelos.size(); j++) {
                if (vuelos.get(j).equals(idVuelo)) {
                    BD.flightsResolved.get(j).getShipments().add(population.getIndividuals()[0].getList_shipments().get(i));
                    noEncontrado = false;
                    break;
                }
            }
            if (noEncontrado) {
                BD.flightsResolved.add(flight);
                BD.flightsResolved.get(vuelos.size()).setShipments(new ArrayList<>());
                BD.flightsResolved.get(vuelos.size()).getShipments().add(population.getIndividuals()[0].getList_shipments().get(i));
                vuelos.add(idVuelo);
            } else {
                noEncontrado = true;
            }
        }
    }
    System.out.println("ARREGLO VUELOS\n" + vuelos);

    return BD.flightsResolved;
}

@CrossOrigin
@RequestMapping(value="runSemanalv2/", method = RequestMethod.GET)
public List<Flight> weekly_genetic_algorithm_V2(/*long date_simulation,int type_simulation*/){
    
    CalendarFlightPool.generate_calendar();

    Population population = new Population(BD.POPULATION_NUM_INDIVIDUALS);

    for(int i=0; i<100; i++){
        BD.shipmentsResolved.add(BD.shipmentsTemp.get(i));
    }

    population.initialize(BD.shipmentsResolved);
    population.evaluate();

    int num_generations = 0;
    int index_best_individual = 0;

    while (num_generations != BD.NUM_GENERATIONS) {
        System.out.println("Generación número: " + num_generations);

        List<Individual[]> new_parents = new ArrayList<>();

        for (int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS / 2; i++) {
            Individual[] new_parents_group = population.selection_parents_roulette(population);
            new_parents.add(new_parents_group);
        }

        List<Individual> new_offspring = new ArrayList<>();
        for (int i = 0; i < new_parents.size(); i++) {
            Individual[] children = new_parents.get(i)[0].crossover_uniform(new_parents.get(i)[1]);
            new_offspring.add(children[0]);
            new_offspring.add(children[1]);
        }

        for (int i = 0; i < new_offspring.size(); i++) {
            new_offspring.get(i).mutation1(population);
        }

        Population population_temp = new Population(BD.POPULATION_NUM_INDIVIDUALS * 2);

        for (int i = 0, k = 0; i < new_parents.size(); i++, k += 2) {
            System.out.println("i: " + i + " k: " + k);
            population_temp.getIndividuals()[k] = new_parents.get(i)[0];
            population_temp.getIndividuals()[k + 1] = new_parents.get(i)[1];
        }

        for (int i = 0; i < new_offspring.size(); i++) {
            population_temp.getIndividuals()[i + BD.POPULATION_NUM_INDIVIDUALS] = new_offspring.get(i);
        }

        double[] list_fitness = population_temp.evaluate();

        System.out.println("==========================");
        for (int i = 0; i < list_fitness.length; i++) {
            System.out.println("Fitness de individuo " + i + ": " + list_fitness[i]);
        }
        System.out.println("==========================");

        double[] list_fitness_temp = Arrays.copyOf(list_fitness, list_fitness.length);
        Arrays.sort(list_fitness_temp);

        int[] list_index = new int[BD.POPULATION_NUM_INDIVIDUALS];
        int j = 0;
        int flag_exist = 0;
        for (int i = list_fitness_temp.length - 1; i >= 0; i--) {
            if (i == list_fitness_temp.length - 1 - BD.POPULATION_NUM_INDIVIDUALS) break;
            flag_exist = 0;
            for (int k = 0; k < list_fitness.length; k++) {
                if (list_fitness[k] == list_fitness_temp[i]) {
                    for (int m = 0; m < list_index.length; m++) {
                        if (list_index[m] == k) {
                            flag_exist = 1;
                        }
                    }
                    if (flag_exist == 1) {
                        flag_exist = 0;
                        continue;
                    }
                    list_index[j] = k;
                    break;
                }
            }
            j += 1;
        }

        population = new Population(BD.POPULATION_NUM_INDIVIDUALS);
        for (int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS; i++) {
            population.getIndividuals()[i] = population_temp.getIndividuals()[list_index[i]];
        }

        for (int k = 0; k < 2; k++) {
            for (int n = 0; n <= 365; n++) {
                for (int m = 0; m < BD.flights[k][n].size(); m++) {
                    for (int a = 0; a < BD.flights[k][n].get(m).getUsed_capacity().length; a++) {
                        if (a == BD.POPULATION_NUM_INDIVIDUALS) break;
                        BD.flights[k][n].get(m).getUsed_capacity()[a] =
                                BD.flights[k][n].get(m).getUsed_capacity()[list_index[a]];
                    }
                }
            }
        }

        num_generations += 1;
    }

    Individual best_individual = population.getIndividuals()[0];

    if (best_individual.getFitness(0) < 0.01) {
        System.out.println("No hay solución");
        return null;
    }

    System.out.println("Total de envíos sin solución " + BD.shipmentsUnresolved.size());
    for (int i = 0; i < BD.shipmentsUnresolved.size(); i++) {
        if (BD.shipmentsUnresolved.get(i).getPackageQuantity() >= 25) {
            System.out.println("SALIDA: " + BD.shipmentsUnresolved.get(i).getDepartureAirport().getCode());
            System.out.println("LLEGADA: " + BD.shipmentsUnresolved.get(i).getArrivalAirport().getCode());
        }
    }

    System.out.println("EL FITNESS MEJOR ES: " + population.getIndividuals()[0].getFitness(0));

    DateFormat formater_date = new SimpleDateFormat("yyyy-MM-dd");
    LocalDate date_array = LocalDate.parse(formater_date.format(new Date(date_simulation)));

    int day_of_year = date_array.getDayOfYear();
    System.out.println("DIA DEL AÑO: " + (day_of_year));

    int year_of_date;
    System.out.println("AÑO DE LA FECHA: 2024");

    if (date_array.getYear() == 2024) {
        year_of_date = 0;
    } else {
        year_of_date = 1;
    }

    if (type_simulation == 7) {
        for (int i = 0; i < type_simulation; i++) {
            System.out.println("DIA DEL AÑO: " + (day_of_year + i));
            if (day_of_year + i >= 365) {
                BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date + 1][i]);
            } else {
                BD.flights[year_of_date][day_of_year - 1].addAll(BD.flights[year_of_date][day_of_year + i]);
            }
        }
    }

    System.out.println("funciona");

    System.out.println("NUMERO DE ITINERARIOS: " + population.getIndividuals()[0].getList_flight_schedule().size());
    System.out.println("NUMERO DE PAQUETES: " + population.getIndividuals()[0].getList_shipments().size());

    List<Integer> vuelos = new ArrayList<>();
    boolean noEncontrado = true;
    System.out.println("Tamaño: " + tamanio);
    for (int i = 0; i < tamanio; i++) {
        for (Flight flight : population.getIndividuals()[0].getList_flight_schedule().get(i).getFlights()) {
            Integer idVuelo = flight.getId();
            for (int j = 0; j < vuelos.size(); j++) {
                if (vuelos.get(j).equals(idVuelo)) {
                    BD.flightsResolved.get(j).getShipments().add(population.getIndividuals()[0].getList_shipments().get(i));
                    noEncontrado = false;
                    break;
                }
            }
            if (noEncontrado) {
                BD.flightsResolved.add(flight);
                BD.flightsResolved.get(vuelos.size()).setShipments(new ArrayList<>());
                BD.flightsResolved.get(vuelos.size()).getShipments().add(population.getIndividuals()[0].getList_shipments().get(i));
                vuelos.add(idVuelo);
            } else {
                noEncontrado = true;
            }
        }
    }
    System.out.println("ARREGLO VUELOS\n" + vuelos);

    return BD.flightsResolved;
}




	@CrossOrigin
	@RequestMapping(value="runReplanificacion/", method = RequestMethod.GET)
	public List<FlightSchedule> genetic_algorithm_REPLANIFICACION(/*@RequestBody long date_simulation, @RequestBody int type_simulation*/){

		long date_simulation_start = 1659398400000L;
        long date_simulation_end = 1659484800000L;
		BD.readAirports();
		BD.readFlights();

		CalendarFlightPool.generate_calendar();
		
		Population population = new Population(BD.POPULATION_NUM_INDIVIDUALS);
		
		int elementsToRemove;
		int num_generations=0;
		int index_best_individual = 0;
		int flag=1;

		for(int dia=0; dia<BD.SEMANAL_DAYS; dia++){
			BD.read_list_shipment_with_date_REPLANIFICACION(date_simulation_start+dia*BD.ONE_DAY_MS);	//no inicializa BD.shipmentsTemp
			while (!BD.shipmentsWaitingList.isEmpty()) {
				elementsToRemove = Math.min(BD.SHIPMENTS_PER_ITERATION, BD.shipmentsWaitingList.size());
				for(int j=0; j<BD.SHIPMENTS_PER_ITERATION; j++){
					BD.shipmentsTemp.add(BD.shipmentsWaitingList.remove(0));
				}
				if(flag==1){
					population.initialize(BD.shipmentsTemp);
					population.evaluate();
	
					while(num_generations != BD.NUM_GENERATIONS) {
			
						List<Individual[]> new_parents = new ArrayList<Individual[]>();
						
						//System.out.println("============== HOLA ==============");
						
						for(int i = 0; i < BD.POPULATION_NUM_INDIVIDUALS/2; i++) {
							//System.out.println("============== ROULETTE ==============");
							
							//Individual[] new_parents_group = population.selection_parents_tournament(population, 4);
							Individual[] new_parents_group = population.selection_parents_roulette(population);
							new_parents.add(new_parents_group);
						}
						
						List<Individual> new_offspring = new ArrayList<Individual>();
						for(int i=0;i<new_parents.size();i++) {
							//System.out.println("============== CROSSOVER ==============");
							Individual[] children = new_parents.get(i)[0].crossover_uniform(new_parents.get(i)[1]);
							new_offspring.add(children[0]);
							new_offspring.add(children[1]);
						}
						
						for(int i=0;i<new_offspring.size();i++) {
						//	//System.out.println("============== MUTATION 1 ==============");
							new_offspring.get(i).mutation1(population);
						}
						
						Population population_temp = new Population(BD.POPULATION_NUM_INDIVIDUALS*2);
						
						for(int i=0, k=0;i<new_parents.size();i++, k+=2) {
							System.out.println("i :" + i + " k: " + k);
							population_temp.getIndividuals()[k] = new_parents.get(i)[0];
							population_temp.getIndividuals()[k+1] = new_parents.get(i)[1];
						}
						
						for(int i=0;i<new_offspring.size();i++) {
							population_temp.getIndividuals()[i+BD.POPULATION_NUM_INDIVIDUALS] = new_offspring.get(i);
						}
						
						//Seleccionar los 10 mejores
						double[] list_fitness = population_temp.evaluate();
						
						System.out.println("==========================");
						for(int i=0;i<list_fitness.length;i++) {
							System.out.println(list_fitness[i]);
						}
						System.out.println("==========================");
						
						
						double[] list_fitness_temp = Arrays.copyOf(list_fitness, list_fitness.length);
			
						Arrays.sort(list_fitness_temp);
						
						int[] list_index = new int[BD.POPULATION_NUM_INDIVIDUALS];
						
						int j=0;
						int flag_exist=0;
						for(int i=list_fitness_temp.length-1;i>=0;i--) {
							if(i ==  list_fitness_temp.length-1-BD.POPULATION_NUM_INDIVIDUALS) break;
							flag_exist = 0;
							for(int k=0;k<list_fitness.length;k++) {
								if(list_fitness[k] == list_fitness_temp[i]) {
									for(int m=0;m<list_index.length;m++) {
										if(list_index[m] == k) {
											flag_exist = 1; //significa que este indice ya existe
										}							
									}
									if(flag_exist == 1) {
										flag_exist = 0;
										continue;
									}
									list_index[j] = k;
									break;
								}
							}
							j+=1;
						}
						
			
						population = new Population(BD.POPULATION_NUM_INDIVIDUALS);
						for(int i=0;i<BD.POPULATION_NUM_INDIVIDUALS;i++) {
							population.getIndividuals()[i] = population_temp.getIndividuals()[list_index[i]];
						}
				
						for(int k=0;k<2;k++) {
							for(int n=0;n<=365;n++) {
								for(int m=0;m<BD.flights[k][n].size();m++) {
									for(int a=0;a<BD.flights[k][n].get(m).getUsed_capacity().length;a++) {
										if(a==BD.POPULATION_NUM_INDIVIDUALS) break;
											BD.flights[k][n].get(m).getUsed_capacity()[a]= 
											BD.flights[k][n].get(m).getUsed_capacity()[list_index[a]];
									}
								}
							}
						}
			
						//los guardo en una nueva poblacion // los 10 mejores descendientes
			 
						num_generations+=1;
			
					}
					
					
					Individual best_individual = population.getIndividuals()[0];
					
					if(best_individual.getFitness(0) < 0.01) {
						System.out.println("No hay solucion");
						return null;
					}
					
					System.out.println("Total de envios sin solucion "+ BD.shipmentsUnresolved.size());
					for (int i = 0; i < BD.shipmentsUnresolved.size(); i++) {
						if(BD.shipmentsUnresolved.get(i).getPackageQuantity() >= 25) {
							System.out.println("SALIDA: " + BD.shipmentsUnresolved.get(i).getDepartureAirport().getCode());
							System.out.println("LLEGADA: " + BD.shipmentsUnresolved.get(i).getArrivalAirport().getCode());
						}
					}
					
					System.out.println("EL FITNESS MEJOR ES: " +  population.getIndividuals()[0].getFitness(0));
					
					/********************************* DAYS OF THE YEAR - ARRAY ***************************************/
					DateFormat formater_date = new SimpleDateFormat("yyyy-MM-dd");
					LocalDate date_array = LocalDate.parse(formater_date.format(new Date(date_simulation_start)));
							
					int day_of_year = date_array.getDayOfYear();
					System.out.println("DIA DEL AÑO: " + (day_of_year));
					
					int year_of_date;
					System.out.println("AÑO DE LA FECHA: " + date_array.getYear());
					
					if(date_array.getYear() == 2024) { year_of_date = 0; }
					else { year_of_date = 1; }
					




					if(BD.SEMANAL_DAYS == 7) {
						for (int i = 0; i < BD.SEMANAL_DAYS; i++) {
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
	
				}
				else{
					population.initializePEDRO(BD.shipmentsTemp);
				}





			}
			
			
			
			

			



		}
		


		

		
		

		
		
		
		
        System.out.println("funciona");
		
        System.out.println("NUMERO DE ITINERARIOS: "+population.getIndividuals()[0].getList_flight_schedule().size());
        System.out.println("NUMERO DE PAQUETES: "+population.getIndividuals()[0].getList_shipments().size());
        
        //return BD.flights[year_of_date][day_of_year - 1];
        return population.getIndividuals()[0].getList_flight_schedule();
    }
	


	/*
	@RequestMapping(value="test/", method = RequestMethod.GET)
	public List<Flight> test(){
		BD.readAirports();
		BD.readFlights();
		CalendarFlightPool.generate_calendar();
		
		//System.out.println("WENAS: "+ GeneralData.list_pool_fligths[1][20].getArrival_date_time().getTime());
		
		return BD.flights[0][213];
	}
	
	
	
	@RequestMapping(value="read_files/{date_simulation}/{type_simulation}", method = RequestMethod.GET)
	public void read_files(@PathVariable("date_simulation") long date_simulation, 
							@PathVariable("type_simulation") int type_simulation){
		BD.readAirports();
		BD.readFlights();
		//BD.read_list_shipment();
		BD.read_list_shipment_with_date(date_simulation, type_simulation);
		CalendarFlightPool.generate_calendar();
		return;
	}
	
	// @CrossOrigin
	// @RequestMapping(value="get_simulation_flights/{date_simulation}/{type_simulation}", method = RequestMethod.GET)
	// public List<ObjectNode> get_simulation_fligths(@PathVariable("date_simulation") long date_simulation, 
	// 												@PathVariable("type_simulation") int type_simulation) throws JsonProcessingException{
		
	// 	// Tiempo de ejecucion del algoritmo => TA = 5min
	// 	// Salto de cada ejecucion => SA = 10min
	// 	// Proporcionalidad de tiempo => PT
	// 	// PT = 1 -> Operaciones diarias
	// 	// PT = algo que debemos descubrir -> Simulacion 5 dias
	// 	// PT = algo que debemos descubrir -> Simulacion colapso
		
	// 	BD.read_list_airports();
	// 	BD.read_list_flights();
		
	// 	// List<Flight> list_flights = this.genetic_algorithm(date_simulation, type_simulation);
			
	// 	// List<ObjectNode> result_api = JsonFormat.convertFlightToJSON(list_flights);

	// 	//ObjectMapper objectMapper = new ObjectMapper();
	// 	//objectMapper.readValues(result_api, JSONArray.class)
		
	// 	System.out.println("ESTA ES LA FECHA EN MS POR PARAMETRO: " + date_simulation);
		
		return result_api;
	}
	*/
	
}
