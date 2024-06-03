package com.edu.pucp.dp1.redex.model;

public class Continent {
    private int id;
	private String name;
	private String abbrev;
	
	public Continent() {

	}
	
	public Continent(int id, String name, String abbrev) {
		super();
		this.name = name;
		this.abbrev = abbrev;
	}
	
	public Continent(Continent continent) {
		this.id = continent.getId();
		this.name = continent.getName();
		this.abbrev = continent.getAbbrev();
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
	
	public String getAbbrev() {
		return abbrev;
	}
	
	public void setAbbrev(String abbrev) {
		this.abbrev = abbrev;
	}

}
