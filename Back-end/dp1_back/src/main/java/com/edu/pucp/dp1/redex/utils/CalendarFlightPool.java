package com.edu.pucp.dp1.redex.utils;

import java.util.ArrayList;
import java.util.Calendar;

import com.edu.pucp.dp1.redex.model.Flight;

public class CalendarFlightPool {
    public static void generate_calendar() {
		
		GeneralData.list_pool_fligths = new ArrayList[2][366];
		int year=52;
		int day=0;
		
		List<Flight> list_flight_temp;
		
		
		for(int i=0;i<2; i++) {
			day=0;
			for(int k=0;k<=365;k++) {
				
				Calendar c = Calendar.getInstance(); 
	        	
				list_flight_temp = new ArrayList<Flight>();
				
				for(int n=0;n<GeneralData.list_pool_fligths_temp.size();n++) {
					Date date_temp = new Date(0);
					
					Flight flight = new Flight(GeneralData.list_pool_fligths_temp.get(n));
					
					c.setTime(date_temp);
					//if(i==0) c.set(2022, 0, 0);
					//else c.set(2023, 0, 0);
					c.add(Calendar.YEAR, year);
		        	c.add(Calendar.DATE, day);
		        	//c.add(Calendar.HOUR_OF_DAY, -5);
		        	
		        	/*System.out.println(c.get(Calendar.DAY_OF_YEAR));*/
		        	
		        	date_temp = c.getTime();
		        	
		        	flight.setDeparture_date_time(new Date(flight.getDeparture_date_time().getTime() + date_temp.getTime()));
		        	flight.setArrival_date_time(new Date(flight.getArrival_date_time().getTime() + date_temp.getTime()));
		        	//arrival_time = c.getTime();
					
		        	list_flight_temp.add(flight);
					//LocalDate localDate = new LocalDate(year, 0 ,0).set
				}
				GeneralData.list_pool_fligths[i][k] = list_flight_temp;
				day+=1;
				//c.add(Calendar.DATE, 1);
			}
			year+=1;
		}
	}
}
