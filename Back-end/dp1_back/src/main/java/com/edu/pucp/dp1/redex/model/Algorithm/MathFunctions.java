package com.edu.pucp.dp1.redex.model.Algorithm;

import java.util.List;

public class MathFunctions {
    public static double standar_deviation(List<Double> list) {
		double sum = 0.0;
		double standarDeviation = 0.0;
		double mean = 0.0;
		double res = 0.0;
		double sq = 0.0;
		
		for(int i=0;i<list.size();i++) {
			sum += list.get(i);
		}
		
		mean = sum/list.size();
		
		for(int i=0;i<list.size();i++) {
			standarDeviation += Math.pow((list.get(i) - mean), 2);
		}
		
		sq = standarDeviation/list.size();
		res=Math.sqrt(sq);
		return res;
	}
	
	public static double variance(List<Double> list) {
		double sum = 0.0;
		double standarDeviation = 0.0;
		double mean = 0.0;
		double res = 0.0;
		double sq = 0.0;
		
		for(int i=0;i<list.size();i++) {
			sum += list.get(i);
		}
		
		mean = sum/list.size();
		
		for(int i=0;i<list.size();i++) {
			standarDeviation += Math.pow((list.get(i) - mean), 2);
		}
		
		sq = standarDeviation/list.size();
		return sq;
	}

}
