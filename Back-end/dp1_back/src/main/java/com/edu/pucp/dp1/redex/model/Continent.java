package com.edu.pucp.dp1.redex.model;

public class Continent {
    private int id;
	private String name;
	
	public Continent() {
	}
	
	public Continent(int id, String name) {
		this.id = id;
		this.name = name;
	}
	
	public Continent(Continent continent) {
		this.id = continent.getId();
		this.name = continent.getName();
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}