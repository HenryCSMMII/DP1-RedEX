package com.edu.pucp.dp1.redex.model;

public class Airport {
    private String code;
    private String city;
    private String country;
    private String shortName;
    private String continent;
    private int timezoneOffset;
    private int capacity;

    public Airport(String code, String city, String country, String shortName, String continent, int timezoneOffset, int capacity) {
        this.code = code;
        this.city = city;
        this.country = country;
        this.shortName = shortName;
        this.continent = continent;
        this.timezoneOffset = timezoneOffset;
        this.capacity = capacity;
    }

    // Getters
    public String getCode() {
        return code;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getShortName() {
        return shortName;
    }

    public String getContinent() {
        return continent;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }

    public int getCapacity() {
        return capacity;
    }

    // Setters
    public void setCode(String code) {
        this.code = code;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public void setContinent(String continent) {
        this.continent = continent;
    }

    public void setTimezoneOffset(int timezoneOffset) {
        this.timezoneOffset = timezoneOffset;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Airport{" +
                "code='" + code + '\'' +
                ", city='" + city + '\'' +
                ", country='" + country + '\'' +
                ", shortName='" + shortName + '\'' +
                ", continent='" + continent + '\'' +
                ", timezoneOffset=" + timezoneOffset +
                ", capacity=" + capacity +
                '}';
    }
}
