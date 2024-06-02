package com.edu.pucp.dp1.redex.model.Algorithm;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;
import java.util.stream.Collectors;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.ItinerarioVuelos;
import com.edu.pucp.dp1.redex.model.Shipment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;



public class Individual {
    private List<ItinerarioVuelos> list_flight_schedule;
	private List<Shipment> list_shipments; //Su lista privada de envios porque se pueden separar los
										  //productos y depende de cada solución como se manejará
	private double fitness;
	private long all_estimated_time;
	private Random random;
	
	public Individual() {
		this.list_shipments = new ArrayList<Shipment>();
		this.list_flight_schedule = new ArrayList<ItinerarioVuelos>();
		this.fitness = -1;
		this.all_estimated_time = 0;
		this.random = new Random();
	}
	
	public List<ItinerarioVuelos> getList_flight_schedule() {
		return list_flight_schedule;
	}

	public void setList_flight_schedule(List<ItinerarioVuelos> list_flight_schedule) {
		this.list_flight_schedule = list_flight_schedule;
	}
	
	public List<Shipment> getList_shipments() {
		return list_shipments;
	}

	public void setListShipments(List<Shipment> list_shipments) {
		this.list_shipments = list_shipments;
	}

	public double getFitness(int index) {//indice del inidividuo
		if(true) {
			//long suma = 0;
			//for(int i=0;i<this.list_flight_schedule.size();i++) {
			//	suma+= this.list_flight_schedule.get(i).getEstimated_time();
			//}
			List<Double> disponibilidad;
			disponibilidad = new ArrayList<Double>();
            
            /*SUPER DUDA PEDRO (CODIGO ORIGINAL)
			for(int j=0;j<this.list_flight_schedule.size();j++) { // calculariamos que tan llenos estan los vuelos %
				for(int k=0;k<this.list_flight_schedule.get(j).getFlights().size();k++) {
					if(this.list_flight_schedule.get(j).getFlights().get(k).getUsed_capacity()[index] > this.list_flight_schedule.get(j).getFlights().get(k).getMax_capacity()) {
						this.fitness = 0.000001; //Peor fitness
						//System.out.println("NO HAY SOLUCION");
						return this.fitness;
					}
					else {
						disponibilidad.add((double)this.list_flight_schedule.get(j).getFlights().get(k).getUsed_capacity()[index]/
								(double)this.list_flight_schedule.get(j).getFlights().get(k).getMax_capacity());
					}
				}
			}*/
			
            for(int j=0;j<this.list_flight_schedule.size();j++) { // calculariamos que tan llenos estan los vuelos %
				for(int k=0;k<this.list_flight_schedule.get(j).getFlights().size();k++) {
					if(this.list_flight_schedule.get(j).getFlights().get(k).getCurrentLoad()[index] > this.list_flight_schedule.get(j).getFlights().get(k).getCapacity()) {
						this.fitness = 0.000001; //Peor fitness
						//System.out.println("NO HAY SOLUCION");
						return this.fitness;
					}
					else {
						disponibilidad.add((double)this.list_flight_schedule.get(j).getFlights().get(k).getCurrentLoad()[index]/
								(double)this.list_flight_schedule.get(j).getFlights().get(k).getCapacity());
					}
				}
			}

			double sd = MathFunctions.variance(disponibilidad);
			
			this.fitness = 1/sd;
		}
		return fitness;
	}

	public void setFitness(long fitness) {
		this.fitness = fitness;
	}

	public Individual(List<Shipment>list_shipments_original) { //Envios que tenemos por procesar
		
		this.list_shipments = new ArrayList<Shipment>();
		
		for(int i=0;i<list_shipments_original.size();i++) {
			this.list_shipments.add(i,new Shipment(list_shipments_original.get(i)));
		}
		
		this.fitness = -1;
		this.random = new Random();
	}
	
	public Individual(List<ItinerarioVuelos>list_flight_schedule_original, List<Shipment>list_shipments_original) {
		this.list_shipments = new ArrayList<Shipment>();
		this.list_flight_schedule = new ArrayList<ItinerarioVuelos>();
		
		for(int i=0;i<list_flight_schedule_original.size();i++) {
			this.list_flight_schedule.add(i,new ItinerarioVuelos(list_flight_schedule_original.get(i)));
		}
		
		for(int i=0;i<list_shipments_original.size();i++) {
			this.list_shipments.add(i,new Shipment(list_shipments_original.get(i)));
		}
		
		this.fitness = -1;
		this.random = new Random();
	}

	// ----------- POR SI ACASO ----------- //
	public Individual(Individual individual) {
		//this.list_shipments = new ArrayList<Shipment>(individual.list_shipments);
		//this.list_flight_schedule = new ArrayList<FlightSchedule>(individual.list_flight_schedule);
		
		this.list_shipments = new ArrayList<Shipment>();
		this.list_flight_schedule = new ArrayList<ItinerarioVuelos>();
		
		for(int i=0;i< individual.list_flight_schedule.size();i++) {
			this.list_flight_schedule.add(i, new ItinerarioVuelos(individual.list_flight_schedule.get(i)));
		}
		
		for(int i=0;i<individual.list_shipments.size();i++) {
			this.list_shipments.add(i,new Shipment(individual.list_shipments.get(i)));
		}
		
		this.fitness = individual.fitness;
		this.random = individual.random;
	}
	
	public void initialize() {
		
		this.list_flight_schedule = new ArrayList<ItinerarioVuelos>();
        
        //listar TODOS los envios
		BD.shipments = new ArrayList<Shipment>();

		int contador = 0;
		for(int i=0;i<list_shipments.size();i++) {
			
			ItinerarioVuelos flight_schedule = new ItinerarioVuelos();
			flight_schedule.setFlights(new ArrayList<Flight>());
			flight_schedule.setActive(true);
			flight_schedule.setEstimated_time(0);
			
			long max_time = list_shipments.get(i).calc_max_possible_time();
			long max_additional_days = list_shipments.get(i).calc_max_additional_days();
			
			/*
			System.out.println("===================================");
			System.out.println("ENVIO");
			System.out.println("==================================");
			System.out.println(list_shipments.get(i).getDepartureAirport().getCode() +" - " +list_shipments.get(i).getArrivalAirport().getCode());
			*/
			
			while(generateReverseSolution(list_shipments.get(i).getOrigen(),
					list_shipments.get(i).getDestino(), flight_schedule, 0, max_time,0, list_shipments.get(i), max_additional_days)!=1 && contador!=20) {
				flight_schedule = null;
				flight_schedule = new ItinerarioVuelos();
				
				/*
				System.out.println("===================================");
				System.out.println("TOY BUSCANDO OTRA SOLUCION");
				System.out.println("==================================");
				*/
				contador +=1;
			};
			
			if(contador == 20) {
				//System.out.println(list_shipments.get(i).getRegisterDateTime().getTime());
				System.out.println(list_shipments.get(i).getFechaInicio());
				BD.shipments.add(list_shipments.get(i));
				
			}
			contador=0;
			
			Collections.reverse(flight_schedule.getFlights());
			
			this.list_flight_schedule.add(flight_schedule);
			//System.out.println("VA UN VUELO MÁS");
		}
		
	}
	
	public int generateReverseSolution(Airport departure_airport, Airport arrival_airport, ItinerarioVuelos flight_schedule, 
			long estimated_time, long max_time, int stopovers, Shipment shipment, long max_additional_days) {
		//System.out.println("ORIGEN : " + departure_airport.getCountry().getCity() + " LLEGADA: " + arrival_airport.getCountry().getCity());
		//System.out.println(max_time);
		if(estimated_time > max_time || stopovers > Parameters.MAX_STOPOVERS) {
			return 0;
		}
		if(departure_airport.getCodigoIATA().equals(arrival_airport.getCodigoIATA())) {
			/*
			System.out.println("==========ENCONTROOOOOOOOOOOOOOOOOOOOO=====================");
			System.out.println(departure_airport.getCode());
			System.out.println(arrival_airport.getCode());
			*/
			estimated_time +=  (flight_schedule.getFlights().get(flight_schedule.getFlights().size()-1).getDepartureTime().toNanoOfDay() - shipment.getHoraInicio().toNanoOfDay())/1000000 ;
			if(estimated_time > max_time) {
				return 0;
			}
			
			flight_schedule.setEstimated_time(estimated_time);
			return 1; //Ver la restricción de escalas y el tiempo
		}
		/*
		System.out.println("================================");
		System.out.println(departure_airport.getCode());
		System.out.println(arrival_airport.getCode());
		System.out.println("================================");
		*/
		List<Flight> list_of_selected_flights = new ArrayList<Flight>();
		List<Flight> list_of_selected_flights_without_stopovers = new ArrayList<Flight>();
		
		
		Calendar c = Calendar.getInstance();
		//calendar.set();
		int index_day;
		int index_year;
		//c.setTime(shipment.getRegisterDateTime());
		c.setTime(Date.from(shipment.getHoraInicio().atDate(LocalDate.now()).atZone(ZoneId.systemDefault()).toInstant()));
		index_day = c.get(Calendar.DAY_OF_YEAR)-1;
		index_year= c.get(Calendar.YEAR)-2022;
	
		
		//int max_days = (int) Math.ceil((double)max_time/(1000*60*60*24));
		int max_days = ((int) Math.ceil((double)max_additional_days/(1000*60*60*24))) +1;
		
		
		//System.out.println("##########################################################################");
		//System.out.println(max_days);
		
		//System.out.println("AÑO: " + index_year + " DIA: " + index_day);
		
		c.add(Calendar.DATE, 1);
		int index_day_2 = c.get(Calendar.DAY_OF_YEAR)-1;
		int index_year_2= c.get(Calendar.YEAR)-2022;
		
		c.add(Calendar.DATE, 1);
		int index_day_3 = c.get(Calendar.DAY_OF_YEAR)-1;
		int index_year_3= c.get(Calendar.YEAR)-2022;
		
		int one_hour = 1*60*60*1000;

		List<Flight> list_mini_pool_flights_day3 = BD.flights[index_year_3][index_day_3]; //ultimo dia - 3 dias martes
		List<Flight> list_mini_pool_flights_day2 = BD.flights[index_year_2][index_day_2]; //penultimo dia-2 dias lunes
		List<Flight> list_mini_pool_flights_day1 = BD.flights[index_year][index_day]; //primer dia - 1 dia domingo
		
		List<Flight> list_mini_pool_flights_last_day = null;
		if(max_days==3) {
			list_mini_pool_flights_last_day = list_mini_pool_flights_day3;
		}
		else{ //En este caso es 2 ja
			list_mini_pool_flights_last_day = list_mini_pool_flights_day2;
		}
		//for(int k=0;k<max_days;k++) { //max_days - revisar
			//shipment.getRegisterDateTime().get
			//No debe escoger si es que el avion ya está lleno
			for(int i=0;i<list_mini_pool_flights_last_day.size();i++) {
				Flight selected_flight_of_pool_last_day = list_mini_pool_flights_last_day.get(i);
						
				if(flight_schedule.getFlights().size() > 0) { //Si ya hay una escala previa, se valida el tiempo de desembarque
						if(arrival_airport.getCodigoIATA().equals(selected_flight_of_pool_last_day.getDestination().getCodigoIATA())){
								//diferencia entre las escalas (milisegundos)
								long difference = (flight_schedule.getFlights().get(flight_schedule.getFlights().size()-1).getDepartureTime().toNanoOfDay() - 
										selected_flight_of_pool_last_day.getArrivalTime().toNanoOfDay())/1000000;
								difference /= (60*60*1000); // diferencia en horas
								
								if(difference >= 1 ) { //Validamos que sea mayor a 1 hora la escala
									if(shipment.getHoraInicio().toSecondOfDay() <= selected_flight_of_pool_last_day.getDepartureTime().toSecondOfDay()) {
										list_of_selected_flights.add(selected_flight_of_pool_last_day);
										if(departure_airport.getCodigoIATA().equals(selected_flight_of_pool_last_day.getOrigin().getCodigoIATA())) {
											list_of_selected_flights_without_stopovers.add(selected_flight_of_pool_last_day);
										}
									}
								}
								
								if(max_days>=2){
									
									long difference_2 = (flight_schedule.getFlights().get(flight_schedule.getFlights().size()-1).getDepartureTime().toNanoOfDay() - 
											list_mini_pool_flights_day1.get(i).getArrivalTime().toNanoOfDay())/1000000;
									difference_2/= (60*60*1000);
									
									if(difference_2>=1) {
										if(shipment.getHoraInicio().toSecondOfDay() <= list_mini_pool_flights_day1.get(i).getDepartureTime().toSecondOfDay()) {
											list_of_selected_flights.add(list_mini_pool_flights_day1.get(i));
											if(departure_airport.getCodigoIATA().equals(list_mini_pool_flights_day1.get(i).getOrigin().getCodigoIATA())) {
												list_of_selected_flights_without_stopovers.add(list_mini_pool_flights_day1.get(i));
											}
										}
									}
								}
								if(max_days>=3) {
									long difference_3 = (flight_schedule.getFlights().get(flight_schedule.getFlights().size()-1).getDepartureTime().toNanoOfDay() - 
											list_mini_pool_flights_day2.get(i).getArrivalTime().toNanoOfDay())/1000000;
									difference_3/= (60*60*1000);
									
									if(difference_3>=1) {
										if(shipment.getHoraInicio().toSecondOfDay() <= list_mini_pool_flights_day2.get(i).getDepartureTime().toSecondOfDay()) {	
											list_of_selected_flights.add(list_mini_pool_flights_day2.get(i));
											if(departure_airport.getCodigoIATA().equals(list_mini_pool_flights_day2.get(i).getOrigin().getCodigoIATA())) {
												list_of_selected_flights_without_stopovers.add(list_mini_pool_flights_day2.get(i));
											}
										}
									}
								}		
						}
				}
				else { //Si no hay escalas solo se valida la igualdad
						if(arrival_airport.getCodigoIATA().equals(selected_flight_of_pool_last_day.getDestination().getCodigoIATA())){//llega del aeropuerto que queremos?SI
							//list_of_selected_flights.add(selected_flight_of_pool_last_day);
							if(shipment.getHoraInicio().toNanoOfDay()/1000000 + max_additional_days >= list_mini_pool_flights_day1.get(i).getArrivalTime().toNanoOfDay()/1000000 + one_hour) {
								if(shipment.getHoraInicio().toSecondOfDay() <= list_mini_pool_flights_day1.get(i).getDepartureTime().toSecondOfDay()) {
									list_of_selected_flights.add(list_mini_pool_flights_day1.get(i));
									if(departure_airport.getCodigoIATA().equals(list_mini_pool_flights_day1.get(i).getOrigin().getCodigoIATA())) {//vuelod DIRECTOS
										list_of_selected_flights_without_stopovers.add(list_mini_pool_flights_day1.get(i));//primer dia dia
									}
								}
							}
							
							if(max_days>=2) {									
								if(shipment.getHoraInicio().toNanoOfDay()/1000000 + max_additional_days >= list_mini_pool_flights_day2.get(i).getArrivalTime().toNanoOfDay()/1000000 + one_hour){
									if(shipment.getHoraInicio().toSecondOfDay() <= list_mini_pool_flights_day2.get(i).getDepartureTime().toSecondOfDay()) {
										list_of_selected_flights.add(list_mini_pool_flights_day2.get(i));
										if(departure_airport.getCodigoIATA().equals(list_mini_pool_flights_day2.get(i).getOrigin().getCodigoIATA())) {//vuelod DIRECTOS
											list_of_selected_flights_without_stopovers.add(list_mini_pool_flights_day2.get(i));//penultimo dia
										}
									}
								}
							}
							if(max_days==3) {
								if(shipment.getHoraInicio().toNanoOfDay()/1000000 + max_additional_days >= selected_flight_of_pool_last_day.getArrivalTime().toNanoOfDay()/1000000 + one_hour) {
									if(shipment.getHoraInicio().toSecondOfDay() <= selected_flight_of_pool_last_day.getDepartureTime().toSecondOfDay()) {
										list_of_selected_flights.add(selected_flight_of_pool_last_day);
										if(departure_airport.getCodigoIATA().equals(selected_flight_of_pool_last_day.getOrigin().getCodigoIATA())) {//vuelod DIRECTOS
											list_of_selected_flights_without_stopovers.add(selected_flight_of_pool_last_day);//ultimo dia
										}
									}
								}
							}
						}
					}
				}
			
		
		//System.out.println(list_of_selected_flights_without_stopovers.size());
		//System.out.println(list_of_selected_flights.size());
		
		Flight flight = null;
		
		if(list_of_selected_flights.size() == 0) return 0;
		
		if(list_of_selected_flights_without_stopovers.size() >0) {
			//System.out.println(Math.random());
			if(Math.random() < (double)0.3) {
				flight = list_of_selected_flights_without_stopovers.get(random.nextInt(list_of_selected_flights_without_stopovers.size()));
				flight_schedule.getFlights().add(flight); //Luego se voltea la lista
			}
			else {
				flight = list_of_selected_flights.get(random.nextInt(list_of_selected_flights.size()));
				flight_schedule.getFlights().add(flight); //Luego se voltea la lista
			}
		}
		else {
			//System.out.println(list_of_selected_flights.size());
			flight = list_of_selected_flights.get(random.nextInt(list_of_selected_flights.size()));
			flight_schedule.getFlights().add(flight); //Luego se voltea la lista
		}
		
		//Calculamos el tiempo de espera entre escalas
		
		
		long additional_time = (flight.getArrivalTime().toNanoOfDay() - flight.getDepartureTime().toNanoOfDay())/1000000; //En ms
		
		estimated_time+=additional_time +
				TimeZoneAirport.calc_difference(flight.getOrigin(), flight.getDestination())*60*1000 //En ms
				+ 1*60*60*1000 //Tiempo de descarga de paquetes
				+ TimeZoneAirport.calc_wait_time(flight_schedule) - 1*60*60*1000;//En ms //No se considera el tiempo de descarga de paquetes (1 hora) por eso se resta 1 
				//+ desfase  + 1 + tiempo_inicio_vuelo - tiempo_ultimo_vuelo_intinerio -1 ; 
		
	
		//tiempo de registro
		
		//System.out.println("LAS raaaaaaaaaaaaaaaaaaaaaa: " + TimeZoneAirport.calc_wait_time(flight_schedule));
		
		list_of_selected_flights = null;
		list_of_selected_flights_without_stopovers = null;
		
		//System.out.println("TOY EN LA RECURSIÓN");
		return generateReverseSolution(departure_airport, flight.getOrigin(), flight_schedule, estimated_time, max_time, stopovers+1, shipment, max_additional_days);
		
	}
	
	// ---------------------- CROSSOVER ---------------------- //
	public Individual[] crossover_onepoint(Individual individual) {
		
		Individual[] children = new Individual[2];
		
		int index = random.nextInt(individual.list_shipments.size());

		List<ItinerarioVuelos> list_flight_schedule_new_1 = Stream.concat(
				this.list_flight_schedule.subList(0, index).stream(), 
				individual.list_flight_schedule.subList(index, individual.list_flight_schedule.size()).stream()).collect(Collectors.toList());
		
		List<ItinerarioVuelos> list_flight_schedule_new_2 = Stream.concat(
				individual.list_flight_schedule.subList(0, index).stream(), 
				this.list_flight_schedule.subList(index, this.list_flight_schedule.size()).stream()).collect(Collectors.toList());
		
		Individual child_1 = new Individual(list_flight_schedule_new_1, this.list_shipments);
		Individual child_2 = new Individual(list_flight_schedule_new_2, this.list_shipments);
		
		children[0] = child_1;
		children[1] = child_2;
		
		return children;
		
	}
	
	public Individual[] crossover_uniform(Individual other) {
		Individual[] children = new Individual[2];
		
		Individual child1 = new Individual();
		Individual child2 = new Individual();
		
		float selector;
		
		for (int i = 0; i < this.list_shipments.size(); i++) {
			selector = random.nextFloat();
			if (selector < 0.5) {
				//child1.list_flight_schedule.add(new FlightSchedule(this.list_flight_schedule.get(i)));
				//child1.list_shipments.add(this.list_shipments.get(i));
				
				child1.list_flight_schedule.add(this.list_flight_schedule.get(i));
				child1.list_shipments.add(this.list_shipments.get(i));
				
				//child2.list_flight_schedule.add(new FlightSchedule(other.list_flight_schedule.get(i)));
				//child2.list_shipments.add(new Shipment(other.list_shipments.get(i)));
				
				child2.list_flight_schedule.add(other.list_flight_schedule.get(i));
				child2.list_shipments.add(other.list_shipments.get(i));
			}
			else {
				//child1.list_flight_schedule.add(new FlightSchedule(other.list_flight_schedule.get(i)));
				//child1.list_shipments.add(new Shipment(other.list_shipments.get(i)));
				
				child1.list_flight_schedule.add(other.list_flight_schedule.get(i));
				child1.list_shipments.add(other.list_shipments.get(i));
				
				//child2.list_flight_schedule.add(new FlightSchedule(this.list_flight_schedule.get(i)));
				//child2.list_shipments.add(new Shipment(this.list_shipments.get(i)));
				
				child2.list_flight_schedule.add(this.list_flight_schedule.get(i));
				child2.list_shipments.add(this.list_shipments.get(i));
			}
		}

		children[0] = child1;
		children[1] = child2;
		
		return children;
	}
	
	// ---------------------- MUTATION ---------------------- //
	public void mutation1(Population population) {
		// Saca un numero random para definir que alelo va a mutar
		int index_allele = random.nextInt(this.list_shipments.size()); 
		// Saca un numero random para definir con que individuo se va a reemplazar (O INTERCAMBIAR?) DUDITA :)
		int index_individual = random.nextInt(population.getIndividuals().length);
		
		this.list_flight_schedule.set(index_allele, population.getIndividuals()[index_individual].getList_flight_schedule().get(index_allele));
	}
	
	public void calcFlightCap(int index) { //Indice del individuo
		
		this.all_estimated_time = 0;
			
		
		for(int i=0;i<this.list_flight_schedule.size();i++) {
		
			for(int k=0;k<this.list_flight_schedule.get(i).getFlights().size();k++) {
				
				Flight flight = this.list_flight_schedule.get(i).getFlights().get(k);
				
				flight.getCurrentLoad()[index] += this.list_shipments.get(i).getCantidad();
				
			}			
		
			this.all_estimated_time +=  this.list_flight_schedule.get(i).getEstimated_time();
		}

	}
}
