package com.edu.pucp.dp1.redex.Algorithm;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.utils.TimeZoneAirport;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

public class BD {
    public static List<Airport> airports;
	public static List<Flight> flightsTemp;
	public static List<Shipment> shipmentsTemp;
	public static List<Flight>[][] flights;
	public static List<Shipment> shipments;

	
	public static final int MIN_INITIAL_CHROMOSOME_LENGTH = 2;
	public static final int MAX_INITIAL_CHROMOSOME_LENGTH = 5;
	public static final int MAX_STOPOVERS = 20;
	
	
	public static final int POPULATION_NUM_INDIVIDUALS = 10;
	public static final int NUM_GENERATIONS = 10;
	
	public static final int MAX_CAPACITY_FLIGHT_AMERICA = 300;
	public static final int MAX_CAPACITY_FLIGHT_EUROPE = 250;
	public static final int MAX_CAPACITY_FLIGHT_TWO_CONTINENTS = 350;
	
	public static final int MAX_CAPACITY_STORAGE_AMERICA = 850;
	public static final int MAX_CAPACITY_STORAGE_EUROPE = 900;
	
	// Salto de cada ejecucion = 15 minutos
	public static final long EXECUTION_TIME = 900000;
	
	public static final long ONE_DAY_MS = 86400000;
	public static final long SEVEN_DAYS_MS = 604800000;

	/*public static void read_list_shipment(){
		shipmentsTemp = new ArrayList<Shipment>();
		Calendar c = Calendar.getInstance(); 
		try {
			File file = new File("C:/Users/henry/Downloads/dp1_backend (1)/dp1_backend/backend/src/main/java/com/pe/edu/pucp/dp1/backend/utilities/envios.txt");
			Scanner scannerObj = new Scanner(file);
			//System.out.println("entro ps");
			
			while(scannerObj.hasNextLine()) {
				String data = scannerObj.nextLine();
				//System.out.println("entro ps2");
				//System.out.println(data);
				String[] split = data.split("-");
				//System.out.println(split.length);
		        //for (int i=0; i<split.length; i++)
		            //System.out.print(split[i]);
		        //System.out.println("entro ps3");
		        Shipment shipment = new Shipment();
		        shipment.setCode(split[0]);
		        
		        Airport departure = new Airport();
		        departure.setCode(split[0].substring(0, 4));
		        //System.out.println("entro ps4");
		        for(int i=0;i<airports.size();i++) {
		        	if(airports.get(i).getCode().equals(split[0].substring(0, 4))) {
		        		departure.setCountry(airports.get(i).getCountry());
		        		departure.setTime_zone(airports.get(i).getTime_zone());
		        		departure.setId(airports.get(i).getId());
		        		departure.setLatitude(airports.get(i).getLatitude());
		        		departure.setLongitude(airports.get(i).getLongitude());
		        	}
		        }
		        //System.out.println("entro ps5");
		        shipment.setDeparturAirport(departure);
		        
		        String dateTime = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4) + " " + split[2];
		        //System.out.println(dateTime);
		        
		        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
		        Date register_date_time = formatter_date.parse(dateTime);
		        
		        //c.setTime(register_date_time);
		        //c.add(Calendar.HOUR_OF_DAY, -5);
		        //register_date_time = c.getTime();
		        shipment.setRegisterDateTime(register_date_time);
		        
		        
		        //System.out.println("entro ps6");
		        
		        Airport arrival = new Airport();
		        arrival.setCode(split[3].substring(0, 4));
		        
		        for(int i=0;i<airports.size();i++) {
		        	if(airports.get(i).getCode().equals(split[3].substring(0, 4))) {
		        		arrival.setCountry(airports.get(i).getCountry());
		        		arrival.setTime_zone(airports.get(i).getTime_zone());
		        		arrival.setId(airports.get(i).getId());
		        		arrival.setLatitude(airports.get(i).getLatitude());
		        		arrival.setLongitude(airports.get(i).getLongitude());
		        	}
		        }
		        //System.out.println("entro ps2");
		        shipment.setArrivalAirport(arrival);
		        
		        shipment.setPackageQuantity(Integer.parseInt(split[3].substring(5)));
		        
		        shipment.setDepartureTime(null);
		        shipment.setArrivalTime(null);
		        shipment.setFlightSchedule(null);
		        shipment.setClient(null);
		        shipment.setOperator(null);
		        shipment.setState(null);
		        
		        //System.out.println("hola");
		        shipmentsTemp.add(shipment);
			}
			scannerObj.close();
		}catch(Exception e) {
			System.out.println("EXCEPTION SHIPMENTS SIN PARAMETRO: " + e.getMessage());
		}		
	}

	public static void read_list_flights()  {
		
		BD.flightsTemp = new ArrayList<Flight>();
		
		try {
			File file = new File("D:\\archivos\\itinerario.txt");	//direccion
			Scanner scannerObj = new Scanner(file);

			while(scannerObj.hasNextLine()) {
				String data = scannerObj.nextLine();
				//System.out.println(data);
				String[] split = data.split("-");
				//System.out.println(split.length);
		        //for (int i=0; i<split.length; i++)
		            //System.out.print(split[i]);
		        
		        Flight flight = new Flight();
		        flight.setCode(data);
		        
		        Airport airport_1 = new Airport();
		        airport_1.setCode(split[0]);
		        int index_system=0;
		        for(int i=0;i<GeneralData.list_aiport.size();i++) {
		        	if(GeneralData.list_aiport.get(i).getCode().equals(split[0])) {
		        		airport_1.setCountry(GeneralData.list_aiport.get(i).getCountry());
		        		airport_1.setTime_zone(GeneralData.list_aiport.get(i).getTime_zone());
		        		airport_1.setLatitude(GeneralData.list_aiport.get(i).getLatitude());
		        		airport_1.setLongitude(GeneralData.list_aiport.get(i).getLongitude());
		        	}
		        	if(GeneralData.list_aiport.get(i).getCode().equals("SPIM")) {
		        		index_system=i;
					}
		        }
		        
		        flight.setDeparture_airport(airport_1);
		        
		        Airport airport_2 = new Airport();
		        airport_2.setCode(split[1]);
		        
		        for(int i=0;i<GeneralData.list_aiport.size();i++) {
		        	if(GeneralData.list_aiport.get(i).getCode().equals(split[1])) {
		        		airport_2.setCountry(GeneralData.list_aiport.get(i).getCountry());
		        		airport_2.setTime_zone(GeneralData.list_aiport.get(i).getTime_zone());
		        		airport_2.setLatitude(GeneralData.list_aiport.get(i).getLatitude());
		        		airport_2.setLongitude(GeneralData.list_aiport.get(i).getLongitude());
		        	}
		        }
		        
		        flight.setArrival_airport(airport_2);
		        
		        // ---------- Max capacity same continent ---------- //
		        if(airport_1.getCountry().getContinent().getAbbrev().equals(airport_2.getCountry().getContinent().getAbbrev())) {
		        	if(airport_1.getCountry().getContinent().getAbbrev().equals("AS")) {
		        		flight.setMax_capacity(geneticParameters.MAX_CAPACITY_FLIGHT_AMERICA);
		        	}
		        	else if(airport_1.getCountry().getContinent().getAbbrev().equals("E")) {
		        		flight.setMax_capacity(geneticParameters.MAX_CAPACITY_FLIGHT_EUROPE);
		        	}
		        }
		        // ---------- Max capacity different continent ---------- //
		        else {
		        	flight.setMax_capacity(geneticParameters.MAX_CAPACITY_FLIGHT_TWO_CONTINENTS);
		        }
		        
		        SimpleDateFormat formatter=new SimpleDateFormat("HH:mm");
		        Date departure_time = formatter.parse(split[2]);
		        Date arrival_time = formatter.parse(split[3]);
		   
		        
		        if( Integer.valueOf(split[2].substring(0, 2)) > Integer.valueOf(split[3].substring(0, 2))) {
		        	//System.out.println("========= ES OTRO DIA =========");
		        	//Date dt = new Date();
		        	Calendar c = Calendar.getInstance(); 
		        	c.setTime(arrival_time); 
		        	c.add(Calendar.DATE, 1);
		        	arrival_time = c.getTime();
		        	//arrival_time.setTime(arrival_time.getTime() + 1*24*60*60*1000);
		        }
		        
		        flight.setDeparture_date_time(departure_time);
		        flight.setArrival_date_time(arrival_time);
		        //System.out.println("hola");
		        flight.calcEstimatedTime();
		        flight.setDifference_system(TimeZoneAirport.calc_difference(flight.getDeparture_airport(), GeneralData.list_aiport.get(index_system))*60*1000);
		        GeneralData.list_pool_fligths_temp.add(flight);
		        
			}
			scannerObj.close();
		}catch(Exception e) {
			System.out.println("EXCEPTION FLIGHTS: " + e.getMessage());
		}
		
	}*/


}