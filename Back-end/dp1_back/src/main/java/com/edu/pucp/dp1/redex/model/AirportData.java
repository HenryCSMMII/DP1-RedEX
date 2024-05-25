package com.edu.pucp.dp1.redex.model;

public class AirportData {

    private String continent;
    private int timezoneOffset;

    public AirportData(String continent, int timezoneOffset) {
        this.continent = continent;
        this.timezoneOffset = timezoneOffset;
    }

    public AirportData() {
    }

    public String getContinent() {
        return continent;
    }

    public int getTimezoneOffset() {
        return timezoneOffset;
    }



}
