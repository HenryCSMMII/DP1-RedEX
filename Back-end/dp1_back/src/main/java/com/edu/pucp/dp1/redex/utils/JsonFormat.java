package com.edu.pucp.dp1.redex.utils;

import java.util.ArrayList;
import java.util.List;

import com.edu.pucp.dp1.redex.model.Flight;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonFormat {
    public static List<ObjectNode> convertFlightToJSON(List<Flight> Flights) throws JsonProcessingException {
		
		ObjectMapper mapper = new ObjectMapper();
		
		List<ObjectNode> list_flights =  new ArrayList<ObjectNode>();
		
		for(int i=0;i<Flights.size();i++) {
			
			ObjectNode jsonObject = mapper.createObjectNode();
			
			jsonObject.put("id", Flights.get(i).getId());
			jsonObject.put("code", Flights.get(i).getCode());
			
			ObjectNode position_initial = mapper.createObjectNode();
			position_initial.put("lat", Double.parseDouble(Flights.get(i).getDeparture_airport().getLatitude()));
			position_initial.put("lng", Double.parseDouble(Flights.get(i).getDeparture_airport().getLongitude()));
			
			ObjectNode position_final = mapper.createObjectNode();
			position_final.put("lat", Double.parseDouble(Flights.get(i).getArrival_airport().getLatitude()));
			position_final.put("lng", Double.parseDouble(Flights.get(i).getArrival_airport().getLongitude()));
			
			jsonObject.putPOJO("position_initial", position_initial);
			jsonObject.putPOJO("position_final", position_final);
			
			//JSONObject position_actual = new JSONObject();
			//position_actual.put("lat", Flights.get(i).getDeparture_airport().getLatitude());
			//position_actual.put("lng", Flights.get(i).getDeparture_airport().getLongitude());
			
			jsonObject.putPOJO("position_actual", position_initial);
			
			jsonObject.put("isArrived", false);
			jsonObject.put("isVisible", false);
			jsonObject.put("difference_system", Flights.get(i).getDifference_system());
			jsonObject.put("estimatedTime", Flights.get(i).getEstimated_time());
			jsonObject.put("startTime", Flights.get(i).getDeparture_date_time().getTime());
			jsonObject.put("endTime", Flights.get(i).getArrival_date_time().getTime());
			jsonObject.put("max_capacity", Flights.get(i).getMax_capacity());
			jsonObject.put("used_capacity", Flights.get(i).getUsed_capacity()[0]);
			
			//String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
			
			list_flights.add(jsonObject);
		}
		
		return list_flights;
		
	}
}
