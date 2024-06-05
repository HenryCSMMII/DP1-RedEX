package com.edu.pucp.dp1.redex.model;

public class Country {
    private int id;
	private static int increment = 0;
	private String name;
	private String abbrev;
	private Continent continent;
	private String city;
	
	public Country() {
		Country.increment += 1;
		this.id = Country.increment;
	}
	
	public Country(int id, String name, String abbrev, Continent continent, String city) {
		super();
		this.id = id;
		this.name = name;
		this.abbrev = abbrev;
		this.continent = continent;
		this.city = city;
	}
	
	public Country(Country country) {
		this.name = country.getName();
		this.abbrev = country.getAbbrev();
		if(country.getContinent() != null) {
			this.continent = new Continent(country.getContinent());
		}
		this.city = country.getCity();
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
	
	public Continent getContinent() {
		return continent;
	}
	
	public void setContinent(Continent continent) {
		this.continent = continent;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}

}
