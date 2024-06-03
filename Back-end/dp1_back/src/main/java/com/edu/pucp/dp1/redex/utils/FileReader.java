package com.edu.pucp.dp1.redex.utils;

import java.nio.file.DirectoryStream;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Continent;
import com.edu.pucp.dp1.redex.model.Country;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.Shipment;
import com.edu.pucp.dp1.redex.model.StorageCapacity;

public class FileReader {
    public static void read_list_airports()  {
		
		GeneralData.list_aiport = new ArrayList<Airport>();
		
		try {
			File file = new File("D:\\archivos\\datos.txt");
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
		        continent.setAbbrev(split[5]);
		        
		        // ---------- Max capacity ---------- //
		        if(split[5] == "AS") {
		        	airport.setMax_capacity(geneticParameters.MAX_CAPACITY_STORAGE_AMERICA);
		        }
		        else if(split[5] == "E") {
	        		airport.setMax_capacity(geneticParameters.MAX_CAPACITY_STORAGE_EUROPE);
	        	}
		       
		        country.setContinent(continent);
		        
		        
		        airport.setTime_zone(split[6]);
		        airport.setCountry(country);
		        airport.setLatitude(split[7]);
		        airport.setLongitude(split[8]);
		        airport.setStorage(new ArrayList<StorageCapacity>());
		        //System.out.println("hola");
		        GeneralData.list_aiport.add(airport);
		        
			}
			scannerObj.close();
		}catch(Exception e) {
			System.out.println("EXCEPTION AIRPORTS: " + e.getMessage());
		}
		
	}
	
	public static void read_list_flights()  {
		
		GeneralData.list_pool_fligths_temp = new ArrayList<Flight>();
		
		try {
			File file = new File("D:\\archivos\\itinerario.txt");
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
		
	}
	
	public static void read_list_shipment(){
		GeneralData.list_shipment = new ArrayList<Shipment>();
		Calendar c = Calendar.getInstance(); 
		try {
			File file = new File("D:\\archivos\\envios.txt");
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
		        for(int i=0;i<GeneralData.list_aiport.size();i++) {
		        	if(GeneralData.list_aiport.get(i).getCode().equals(split[0].substring(0, 4))) {
		        		departure.setCountry(GeneralData.list_aiport.get(i).getCountry());
		        		departure.setTime_zone(GeneralData.list_aiport.get(i).getTime_zone());
		        		departure.setId(GeneralData.list_aiport.get(i).getId());
		        		departure.setLatitude(GeneralData.list_aiport.get(i).getLatitude());
		        		departure.setLongitude(GeneralData.list_aiport.get(i).getLongitude());
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
		        
		        for(int i=0;i<GeneralData.list_aiport.size();i++) {
		        	if(GeneralData.list_aiport.get(i).getCode().equals(split[3].substring(0, 4))) {
		        		arrival.setCountry(GeneralData.list_aiport.get(i).getCountry());
		        		arrival.setTime_zone(GeneralData.list_aiport.get(i).getTime_zone());
		        		arrival.setId(GeneralData.list_aiport.get(i).getId());
		        		arrival.setLatitude(GeneralData.list_aiport.get(i).getLatitude());
		        		arrival.setLongitude(GeneralData.list_aiport.get(i).getLongitude());
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
		        GeneralData.list_shipment.add(shipment);
			}
			scannerObj.close();
		}catch(Exception e) {
			System.out.println("EXCEPTION SHIPMENTS SIN PARAMETRO: " + e.getMessage());
		}
	}
	
	public static void read_list_shipment_with_date(long date_simulation, int type_simulation){
		long limit_date_data = 0;
		
		GeneralData.list_shipment = new ArrayList<Shipment>();
		Calendar c = Calendar.getInstance(); 
        SimpleDateFormat formatter_date = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        SimpleDateFormat formatter_date_limit = new SimpleDateFormat("dd/MM/yyyy");
        
        if(type_simulation == 1) {
        	limit_date_data = date_simulation + geneticParameters.ONE_DAY_MS + geneticParameters.ONE_DAY_MS;
        }
		else if(type_simulation == 5){
			limit_date_data = date_simulation + geneticParameters.FIVE_DAYS_MS + geneticParameters.ONE_DAY_MS;
		}
        
        /*************************** READ ALL PACK FILES ****************************/
        List<String> pack_files = new ArrayList<>();
        Set<String> fileSet = new HashSet<>();
	    try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("D:\\archivos\\pack\\"))) {
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
				File file = new File("D:\\archivos\\pack\\" + pack_files.get(j));
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
				        
				        for(int i=0;i<GeneralData.list_aiport.size();i++) {
				        	if(GeneralData.list_aiport.get(i).getCode().equals(split[0].substring(0, 4))) {
				        		departure.setCountry(GeneralData.list_aiport.get(i).getCountry());
				        		departure.setTime_zone(GeneralData.list_aiport.get(i).getTime_zone());
				        		departure.setId(GeneralData.list_aiport.get(i).getId());
				        		departure.setLatitude(GeneralData.list_aiport.get(i).getLatitude());
				        		departure.setLongitude(GeneralData.list_aiport.get(i).getLongitude());
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
				        
				        for(int i=0;i<GeneralData.list_aiport.size();i++) {
				        	if(GeneralData.list_aiport.get(i).getCode().equals(split[3].substring(0, 4))) {
				        		arrival.setCountry(GeneralData.list_aiport.get(i).getCountry());
				        		arrival.setTime_zone(GeneralData.list_aiport.get(i).getTime_zone());
				        		arrival.setId(GeneralData.list_aiport.get(i).getId());
				        		arrival.setLatitude(GeneralData.list_aiport.get(i).getLatitude());
				        		arrival.setLongitude(GeneralData.list_aiport.get(i).getLongitude());
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
				        
				        GeneralData.list_shipment.add(shipment);
			        }
				}
				scannerObj.close();
				
				System.out.println("ARCHIVO NUMERO: " + j);
		        System.out.println("TAMANO DE LA LISTA DE ENVIOS: " + GeneralData.list_shipment.size());
		        //System.out.println("FECHA LIMITE: " + limit_date_data);
			}
		}catch(Exception e) {
			System.out.println("EXCEPTION SHIPMENTS CON PARAMETRO: " + e.getMessage());
		}
		
		System.out.println("TAMANO DE LA LISTA DE ENVIOS EN TOTAL: " + GeneralData.list_shipment.size());
	}
}
