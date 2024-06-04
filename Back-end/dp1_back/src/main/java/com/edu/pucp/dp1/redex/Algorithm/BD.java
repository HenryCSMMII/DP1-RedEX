package com.edu.pucp.dp1.redex.Algorithm;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Continent;
import com.edu.pucp.dp1.redex.model.Country;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.model.StorageCapacity;
import com.edu.pucp.dp1.redex.utils.TimeZoneAirport;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class BD {
    public static List<Airport> airports;
	public static List<Flight> flightsTemp;
	public static List<Shipment> shipmentsTemp = new ArrayList<Shipment>();
	public static List<Flight>[][] flights;
	public static List<Shipment> shipments;
	public static List<Continent> continents;
	public static List<Country> countries;
	public static List<Shipment> shipmentsWaitingList = new ArrayList<Shipment>();
	public static List<Shipment> shipmentsResolved = new ArrayList<Shipment>();
	public static List<Flight> flightsResolved = new ArrayList<Flight>();
	
	
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

	public static final int SEMANAL_DAYS = 7;
	public static final int SHIPMENTS_PER_ITERATION = 100;

    public static void readContinents() throws IOException {

        BD.continents = new ArrayList<Continent>();
        
        try {
            File file = new File("Back-end/dp1_back/src/main/resources/input/continentes.txt");
            Scanner scannerObj = new Scanner(file);

            while (scannerObj.hasNextLine()) {
                String data = scannerObj.nextLine();
                String[] parts = data.split(";");
                if (parts.length < 2) continue;

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];

                continents.add(new Continent(id, name));
            }
            scannerObj.close();
        } catch (IOException e) {
            System.out.println("EXCEPTION CONTINENTS: " + e.getMessage());
            throw e;
        }
    }

    public static void readCountries() throws IOException {

        BD.countries = new ArrayList<Country>();

        try {
            File file = new File("Back-end/dp1_back/src/main/resources/input/paises.txt");
            Scanner scannerObj = new Scanner(file);

            while (scannerObj.hasNextLine()) {
                String data = scannerObj.nextLine();
                String[] parts = data.split(";");
                if (parts.length < 5) continue;

                int id = Integer.parseInt(parts[0]);
                String name = parts[1];
                String abbrev = parts[2];
                String continentName = parts[3];
                String city = parts[4];

                // Buscamos o creamos el continente
                Continent continent = null;
                for (Continent cont : BD.continents) {
                    if (cont.getName().equalsIgnoreCase(continentName)) {
                        continent = cont;
                        break;
                    }
                }
                if (continent == null) {
                    continent = new Continent(BD.continents.size() + 1, continentName);
                    BD.continents.add(continent);
                }
                countries.add(new Country(id, name, abbrev, continent, city));
            }
            scannerObj.close();
        } catch (IOException e) {
            System.out.println("EXCEPTION COUNTRIES: " + e.getMessage());
            throw e;
        }
    }	

	public static void readAirports()  {
		
		BD.airports = new ArrayList<Airport>();
		
		try {
			File file = new File("Back-end/dp1_back/src/main/resources/input/datos.txt");
			Scanner scannerObj = new Scanner(file);

			while(scannerObj.hasNextLine()) {
				String data = scannerObj.nextLine();
				//System.out.println(data);
				String[] split = data.split(",");
				//System.out.println(split.length);
		        //for (int i=0; i<split.length; i++)
		            //System.out.print(split[i]);
		        
		        Airport airport = new Airport();
		        airport.setId(Integer.valueOf(split[0]));
		        airport.setCode(split[1]);
		        
		        Country country = new Country();
		        country.setCity(split[2]);
		        country.setName(split[3]);
		        country.setAbbrev(split[4]);
		        
		        Continent continent = new Continent();
		        
		        if(split[5] == "AS") {
		        	airport.setMax_capacity(BD.MAX_CAPACITY_STORAGE_AMERICA);
		        }
		        else if(split[5] == "E") {
	        		airport.setMax_capacity(BD.MAX_CAPACITY_STORAGE_EUROPE);
	        	}
		       
		        continent.setName(split[6]);
                country.setContinent(continent);
		        
		        airport.setTime_zone(split[7]);
		        airport.setCountry(country);
		        airport.setLatitude(split[8]);
		        airport.setLongitude(split[9]);
		        airport.setStorage(new ArrayList<StorageCapacity>());
		        BD.airports.add(airport);
		        
			}
			scannerObj.close();
		}catch(Exception e) {
			System.out.println("EXCEPTION AIRPORTS: " + e.getMessage());
		}
		
	}
	
	public static void readFlights()  {
		
		BD.flightsTemp = new ArrayList<Flight>();
		try {
			File file = new File("Back-end/dp1_back/src/main/resources/input/itinerario.txt");
			Scanner scannerObj = new Scanner(file);

			while(scannerObj.hasNextLine()) {
				String data = scannerObj.nextLine();
				String[] split = data.split("-");
		        
		        Flight flight = new Flight();
		        flight.setCode(data);
		        
		        Airport airport_1 = new Airport();
		        airport_1.setCode(split[0]);
		        int index_system=0;

		        for(int i=0;i<BD.airports.size();i++) {
		        	if(BD.airports.get(i).getCode().equals(split[0])) {
		        		airport_1.setCountry(BD.airports.get(i).getCountry());
		        		airport_1.setTime_zone(BD.airports.get(i).getTime_zone());
		        		airport_1.setLatitude(BD.airports.get(i).getLatitude());
		        		airport_1.setLongitude(BD.airports.get(i).getLongitude());
		        	}
		        	if(BD.airports.get(i).getCode().equals("SPIM")) {
		        		index_system=i;
					}
		        }
		        
		        flight.setDeparture_airport(airport_1);
		        
		        Airport airport_2 = new Airport();
		        airport_2.setCode(split[1]);
		        
		        for(int i=0;i<BD.airports.size();i++) {
		        	if(BD.airports.get(i).getCode().equals(split[1])) {
		        		airport_2.setCountry(BD.airports.get(i).getCountry());
		        		airport_2.setTime_zone(BD.airports.get(i).getTime_zone());
		        		airport_2.setLatitude(BD.airports.get(i).getLatitude());
		        		airport_2.setLongitude(BD.airports.get(i).getLongitude());
		        	}
		        }
		        
		        flight.setArrival_airport(airport_2);
		        
		        // ---------- Max capacity same continent ---------- //
		        if(airport_1.getCountry().getContinent().getName().equals(airport_2.getCountry().getContinent().getName())) {
		        	if(airport_1.getCountry().getContinent().getName().equals("America")) {
		        		flight.setMax_capacity(BD.MAX_CAPACITY_FLIGHT_AMERICA);
		        	}
		        	else if(airport_1.getCountry().getContinent().getName().equals("Europe")) {
		        		flight.setMax_capacity(BD.MAX_CAPACITY_FLIGHT_EUROPE);
		        	}
		        }
		        // ---------- Max capacity different continent ---------- //
		        else {
		        	flight.setMax_capacity(BD.MAX_CAPACITY_FLIGHT_TWO_CONTINENTS);
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
		        flight.setDifference_system(TimeZoneAirport.calc_difference(flight.getDeparture_airport(), BD.airports.get(index_system))*60*1000);
		        BD.flightsTemp.add(flight);
		        
			}
			scannerObj.close();
		}catch(Exception e) {
			System.out.println("EXCEPTION FLIGHTS: " + e.getMessage());
		}
		
	}
	
	public static void readShipments(){
		BD.shipmentsTemp = new ArrayList<Shipment>();
		Calendar c = Calendar.getInstance(); 
		try {
			File file = new File("Back-end/dp1_back/src/main/resources/input/envios.txt");
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
		        for(int i=0;i<BD.airports.size();i++) {
		        	if(BD.airports.get(i).getCode().equals(split[0].substring(0, 4))) {
		        		departure.setCountry(BD.airports.get(i).getCountry());
		        		//departure.setTime_zone(BD.airports.get(i).getTime_zone());
		        		departure.setId(BD.airports.get(i).getId());
		        		//departure.setLatitude(BD.airports.get(i).getLatitude());
		        		//departure.setLongitude(BD.airports.get(i).getLongitude());
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
		        
		        for(int i=0;i<BD.airports.size();i++) {
		        	if(BD.airports.get(i).getCode().equals(split[3].substring(0, 4))) {
		        		arrival.setCountry(BD.airports.get(i).getCountry());
		        		//arrival.setTime_zone(BD.airports.get(i).getTime_zone());
		        		arrival.setId(BD.airports.get(i).getId());
		        		//arrival.setLatitude(BD.airports.get(i).getLatitude());
		        		//arrival.setLongitude(BD.airports.get(i).getLongitude());
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
		        BD.shipmentsTemp.add(shipment);
			}
			scannerObj.close();
		}catch(Exception e) {
			System.out.println("EXCEPTION SHIPMENTS SIN PARAMETRO: " + e.getMessage());
		}
	}
	
	public static int read_list_shipment_with_date(long date_simulation, int type_simulation){
		long limit_date_data = 0;
		
		BD.shipmentsTemp = new ArrayList<Shipment>();
		Calendar c = Calendar.getInstance(); 
        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatter_date_limit = new SimpleDateFormat("dd/MM/yyyy");
        
        if(type_simulation == 1) {
        	limit_date_data = date_simulation + BD.ONE_DAY_MS + BD.ONE_DAY_MS;
        }
		else if(type_simulation == 7){
			limit_date_data = date_simulation + BD.SEVEN_DAYS_MS + BD.ONE_DAY_MS;
		}
        
        /*************************** READ ALL PACK FILES ****************************/
        List<String> pack_files = new ArrayList<>();
        Set<String> fileSet = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("Back-end/dp1_back/src/main/resources/input/pack/"))) {
	        for (Path path : stream) {
	            if (!Files.isDirectory(path)) {
	                fileSet.add(path.getFileName()
	                    .toString());
	            }
	        }
	        pack_files = new ArrayList<>(fileSet);
		    
	    }catch(Exception e) {
	    	System.out.println("EXCEPCION FILES:" + e.getMessage());
	    }

		try {
			for (int j = 0; j < pack_files.size(); j++) {
				File file = new File("Back-end/dp1_back/src/main/resources/input/pack/" + pack_files.get(j));
				Scanner scannerObj = new Scanner(file);
				
				while(scannerObj.hasNextLine()) {
					String data = scannerObj.nextLine();
					//System.out.println(data);
					String[] split = data.split("-");
					//System.out.println(split.length);
			        //for (int i=0; i<split.length; i++)
			            //System.out.println(split[i]);
			        
			        String dateTime = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4) + " " + split[2];
			        //System.out.println(dateTime);
			        
			        Date register_date_time = formatter_date.parse(dateTime);
			        //System.out.println("FECHA REGISTRO EN MS: " + register_date_time.getTime());
			        
			        String actual_date_format = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4);
			        Date actual_date = formatter_date_limit.parse(actual_date_format);
			        
					if(actual_date.getTime() >= limit_date_data) {
						break;
					}
		        
			        if(register_date_time.getTime() >= date_simulation && register_date_time.getTime() <= limit_date_data) {
			        	//System.out.println("entro ps4");
			        	Shipment shipment = new Shipment();
				        shipment.setCode(split[0]);
				        
				        Airport departure = new Airport();
				        departure.setCode(split[0].substring(0, 4));
				        
				        for(int i=0;i<BD.airports.size();i++) {
				        	if(BD.airports.get(i).getCode().equals(split[0].substring(0, 4))) {
				        		departure.setCountry(BD.airports.get(i).getCountry());
				        		departure.setTime_zone(BD.airports.get(i).getTime_zone());
				        		departure.setId(BD.airports.get(i).getId());
				        		departure.setLatitude(BD.airports.get(i).getLatitude());
				        		departure.setLongitude(BD.airports.get(i).getLongitude());
				        	}
				        }
				        //System.out.println("entro ps5");
				        shipment.setDeparturAirport(departure);
				        
				        //String dateTime = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4) + " " + split[2];
				        //System.out.println(dateTime);
				        
				        //SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				        //Date register_date_time = formatter_date.parse(dateTime);
				        
				        //c.setTime(register_date_time);
				        //c.add(Calendar.HOUR_OF_DAY, -5);
				        //register_date_time = c.getTime();
				        shipment.setRegisterDateTime(register_date_time);
				        
				        
				        //System.out.println("entro ps6");
				        
				        Airport arrival = new Airport();
				        arrival.setCode(split[3].substring(0, 4));
				        
				        for(int i=0;i<BD.airports.size();i++) {
				        	if(BD.airports.get(i).getCode().equals(split[3].substring(0, 4))) {
				        		arrival.setCountry(BD.airports.get(i).getCountry());
				        		arrival.setTime_zone(BD.airports.get(i).getTime_zone());
				        		arrival.setId(BD.airports.get(i).getId());
				        		arrival.setLatitude(BD.airports.get(i).getLatitude());
				        		arrival.setLongitude(BD.airports.get(i).getLongitude());
				        	}
				        }
				        
				        shipment.setArrivalAirport(arrival);
				        
				        shipment.setPackageQuantity(Integer.parseInt(split[3].substring(5)));
				        
				        shipment.setDepartureTime(null);
				        shipment.setArrivalTime(null);
				        shipment.setFlightSchedule(null);
				        shipment.setClient(null);
				        shipment.setOperator(null);
				        shipment.setState(null);
				        
				        BD.shipmentsTemp.add(shipment);
			        }
				}
				scannerObj.close();
				
				System.out.println("ARCHIVO NUMERO: " + j);
		        System.out.println("TAMANO DE LA LISTA DE ENVIOS: " + BD.shipmentsTemp.size());
			}
		}catch(Exception e) {
			System.out.println("EXCEPTION SHIPMENTS CON PARAMETRO: " + e.getMessage());
		}

		int tamanio=BD.shipmentsTemp.size();
		System.out.println("TAMANO DE LA LISTA DE ENVIOS EN TOTAL: " + tamanio);
		return tamanio;
	}

	public static void read_list_shipment_with_datePEDRO(long date_simulation){
		long limit_date_data = 0;
		
		//BD.shipmentsTemp = new ArrayList<Shipment>();
		Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatter_date_limit = new SimpleDateFormat("dd/MM/yyyy");
        
		limit_date_data = date_simulation + BD.ONE_DAY_MS + BD.ONE_DAY_MS;

        /*************************** READ ALL PACK FILES ****************************/
        List<String> pack_files = new ArrayList<>();
        Set<String> fileSet = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("Back-end/dp1_back/src/main/resources/input/pack/"))) {
	        for (Path path : stream) {
	            if (!Files.isDirectory(path)) {
	                fileSet.add(path.getFileName()
	                    .toString());
	            }
	        }
	        pack_files = new ArrayList<>(fileSet);
		    
	    }catch(Exception e) {
	    	System.out.println("EXCEPCION FILES:" + e.getMessage());
	    }

		try {
			for (int j = 0; j < pack_files.size(); j++) {
				File file = new File("Back-end/dp1_back/src/main/resources/input/pack/" + pack_files.get(j));
				Scanner scannerObj = new Scanner(file);
				
				while(scannerObj.hasNextLine()) {
					String data = scannerObj.nextLine();
					//System.out.println(data);
					String[] split = data.split("-");
					//System.out.println(split.length);
			        //for (int i=0; i<split.length; i++)
			            //System.out.println(split[i]);
			        
			        String dateTime = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4) + " " + split[2];
			        //System.out.println(dateTime);
			        
			        Date register_date_time = formatter_date.parse(dateTime);
			        //System.out.println("FECHA REGISTRO EN MS: " + register_date_time.getTime());
			        
			        String actual_date_format = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4);
			        Date actual_date = formatter_date_limit.parse(actual_date_format);
			        
					if(actual_date.getTime() >= limit_date_data) {
						break;
					}
		        
			        if(register_date_time.getTime() >= date_simulation && register_date_time.getTime() <= limit_date_data) {
			        	//System.out.println("entro ps4");
			        	Shipment shipment = new Shipment();
				        shipment.setCode(split[0]);
				        
				        Airport departure = new Airport();
				        departure.setCode(split[0].substring(0, 4));
				        
				        for(int i=0;i<BD.airports.size();i++) {
				        	if(BD.airports.get(i).getCode().equals(split[0].substring(0, 4))) {
				        		departure.setCountry(BD.airports.get(i).getCountry());
				        		departure.setTime_zone(BD.airports.get(i).getTime_zone());
				        		departure.setId(BD.airports.get(i).getId());
				        		departure.setLatitude(BD.airports.get(i).getLatitude());
				        		departure.setLongitude(BD.airports.get(i).getLongitude());
				        	}
				        }
				        //System.out.println("entro ps5");
				        shipment.setDeparturAirport(departure);
				        
				        //String dateTime = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4) + " " + split[2];
				        //System.out.println(dateTime);
				        
				        //SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				        //Date register_date_time = formatter_date.parse(dateTime);
				        
				        //c.setTime(register_date_time);
				        //c.add(Calendar.HOUR_OF_DAY, -5);
				        //register_date_time = c.getTime();
				        shipment.setRegisterDateTime(register_date_time);
				        
				        
				        //System.out.println("entro ps6");
				        
				        Airport arrival = new Airport();
				        arrival.setCode(split[3].substring(0, 4));
				        
				        for(int i=0;i<BD.airports.size();i++) {
				        	if(BD.airports.get(i).getCode().equals(split[3].substring(0, 4))) {
				        		arrival.setCountry(BD.airports.get(i).getCountry());
				        		arrival.setTime_zone(BD.airports.get(i).getTime_zone());
				        		arrival.setId(BD.airports.get(i).getId());
				        		arrival.setLatitude(BD.airports.get(i).getLatitude());
				        		arrival.setLongitude(BD.airports.get(i).getLongitude());
				        	}
				        }
				        
				        shipment.setArrivalAirport(arrival);
				        
				        shipment.setPackageQuantity(Integer.parseInt(split[3].substring(5)));
				        
				        shipment.setDepartureTime(null);
				        shipment.setArrivalTime(null);
				        shipment.setFlightSchedule(null);
				        shipment.setClient(null);
				        shipment.setOperator(null);
				        shipment.setState(null);
				        
				        BD.shipmentsWaitingList.add(shipment);
			        }
				}
				scannerObj.close();
				
				System.out.println("ARCHIVO NUMERO: " + j);
		        System.out.println("TAMANO DE LA LISTA DE ENVIOS: " + BD.shipmentsWaitingList.size());
		        //System.out.println("FECHA LIMITE: " + limit_date_data);
			}
		}catch(Exception e) {
			System.out.println("EXCEPTION SHIPMENTS CON PARAMETRO: " + e.getMessage());
		}
		int tamanio=BD.shipmentsTemp.size();
		System.out.println("TAMANO DE LA LISTA DE ENVIOS EN TOTAL: " + tamanio);
		return tamanio;
	}

	public static void read_list_shipment_with_datePEDRO(long date_simulation){
		long limit_date_data = 0;
		
		//BD.shipmentsTemp = new ArrayList<Shipment>();
		Calendar c = Calendar.getInstance();
        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatter_date_limit = new SimpleDateFormat("dd/MM/yyyy");
        
		limit_date_data = date_simulation + BD.ONE_DAY_MS + BD.ONE_DAY_MS;

        /*************************** READ ALL PACK FILES ****************************/
        List<String> pack_files = new ArrayList<>();
        Set<String> fileSet = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("Back-end/dp1_back/src/main/resources/input/pack/"))) {
	        for (Path path : stream) {
	            if (!Files.isDirectory(path)) {
	                fileSet.add(path.getFileName()
	                    .toString());
	            }
	        }
	        pack_files = new ArrayList<>(fileSet);
		    
	    }catch(Exception e) {
	    	System.out.println("EXCEPCION FILES:" + e.getMessage());
	    }

		try {
			for (int j = 0; j < pack_files.size(); j++) {
				File file = new File("Back-end/dp1_back/src/main/resources/input/pack/" + pack_files.get(j));
				Scanner scannerObj = new Scanner(file);
				
				while(scannerObj.hasNextLine()) {
					String data = scannerObj.nextLine();
					//System.out.println(data);
					String[] split = data.split("-");
					//System.out.println(split.length);
			        //for (int i=0; i<split.length; i++)
			            //System.out.println(split[i]);
			        
			        String dateTime = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4) + " " + split[2];
			        //System.out.println(dateTime);
			        
			        Date register_date_time = formatter_date.parse(dateTime);
			        //System.out.println("FECHA REGISTRO EN MS: " + register_date_time.getTime());
			        
			        String actual_date_format = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4);
			        Date actual_date = formatter_date_limit.parse(actual_date_format);
			        
					if(actual_date.getTime() >= limit_date_data) {
						break;
					}
		        
			        if(register_date_time.getTime() >= date_simulation && register_date_time.getTime() <= limit_date_data) {
			        	//System.out.println("entro ps4");
			        	Shipment shipment = new Shipment();
				        shipment.setCode(split[0]);
				        
				        Airport departure = new Airport();
				        departure.setCode(split[0].substring(0, 4));
				        
				        for(int i=0;i<BD.airports.size();i++) {
				        	if(BD.airports.get(i).getCode().equals(split[0].substring(0, 4))) {
				        		departure.setCountry(BD.airports.get(i).getCountry());
				        		departure.setTime_zone(BD.airports.get(i).getTime_zone());
				        		departure.setId(BD.airports.get(i).getId());
				        		departure.setLatitude(BD.airports.get(i).getLatitude());
				        		departure.setLongitude(BD.airports.get(i).getLongitude());
				        	}
				        }
				        //System.out.println("entro ps5");
				        shipment.setDeparturAirport(departure);
				        
				        //String dateTime = split[1].substring(6) + '/' + split[1].substring(4, 6) + '/' + split[1].substring(0, 4) + " " + split[2];
				        //System.out.println(dateTime);
				        
				        //SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
				        //Date register_date_time = formatter_date.parse(dateTime);
				        
				        //c.setTime(register_date_time);
				        //c.add(Calendar.HOUR_OF_DAY, -5);
				        //register_date_time = c.getTime();
				        shipment.setRegisterDateTime(register_date_time);
				        
				        
				        //System.out.println("entro ps6");
				        
				        Airport arrival = new Airport();
				        arrival.setCode(split[3].substring(0, 4));
				        
				        for(int i=0;i<BD.airports.size();i++) {
				        	if(BD.airports.get(i).getCode().equals(split[3].substring(0, 4))) {
				        		arrival.setCountry(BD.airports.get(i).getCountry());
				        		arrival.setTime_zone(BD.airports.get(i).getTime_zone());
				        		arrival.setId(BD.airports.get(i).getId());
				        		arrival.setLatitude(BD.airports.get(i).getLatitude());
				        		arrival.setLongitude(BD.airports.get(i).getLongitude());
				        	}
				        }
				        
				        shipment.setArrivalAirport(arrival);
				        
				        shipment.setPackageQuantity(Integer.parseInt(split[3].substring(5)));
				        
				        shipment.setDepartureTime(null);
				        shipment.setArrivalTime(null);
				        shipment.setFlightSchedule(null);
				        shipment.setClient(null);
				        shipment.setOperator(null);
				        shipment.setState(null);
				        
				        BD.shipmentsWaitingList.add(shipment);
			        }
				}
				scannerObj.close();
				
				System.out.println("ARCHIVO NUMERO: " + j);
		        System.out.println("TAMANO DE LA LISTA DE ENVIOS: " + BD.shipmentsWaitingList.size());
		        //System.out.println("FECHA LIMITE: " + limit_date_data);
			}
		}catch(Exception e) {
			System.out.println("EXCEPTION SHIPMENTS CON PARAMETRO: " + e.getMessage());
		}
		System.out.println("TAMANO DE LA LISTA DE ENVIOS EN TOTAL: " + BD.shipmentsTemp.size());
	}
}