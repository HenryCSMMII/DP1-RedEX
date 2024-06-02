package com.edu.pucp.dp1.redex.model.Algorithm;

import com.edu.pucp.dp1.redex.model.Airport;
import com.edu.pucp.dp1.redex.model.Flight;
import com.edu.pucp.dp1.redex.model.Shipment;

import java.util.ArrayList;
import java.util.List;

public class BD {
    public static List<Airport> list_aiport;
	public static List<Flight> list_pool_fligths_temp;
	public static List<Shipment> list_shipment;
	public static List<Flight>[][] list_pool_fligths;
	public static List<Shipment> list_shipments_without_solution;
}
