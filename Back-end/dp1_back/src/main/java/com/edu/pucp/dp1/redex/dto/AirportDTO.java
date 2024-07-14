package com.edu.pucp.dp1.redex.dto;

import com.edu.pucp.dp1.redex.model.Airport;

public class AirportDTO {
    private int id;
    private String code;
    private String latitude;
    private String longitude;
    private int max_capacity;
    private int countryId;
    private int cityId;
    private int current_capacity;
    private String time_zone;

    public AirportDTO() {
    }

    public AirportDTO(int id, String code, String latitude, String longitude, int max_capacity, int countryId, int cityId, int current_capacity,String time_zone) {
        this.id = id;
        this.code = code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.max_capacity = max_capacity;
        this.countryId = countryId;
        this.cityId = cityId;
        this.current_capacity = current_capacity;
        this.time_zone = time_zone;
    }

    public static AirportDTO fromAirport(Airport airport) {
        int currentCapacity = 0;
        if (airport.getStorage() != null && !airport.getStorage().isEmpty()) {
            currentCapacity = airport.getStorage().get(0).getQuantity();
        }

        AirportDTO dto = new AirportDTO();
        dto.setId(airport.getId());
        dto.setCode(airport.getCode());
        dto.setLatitude(airport.getLatitude());
        dto.setLongitude(airport.getLongitude());
        dto.setMax_capacity(airport.getMax_capacity());
        dto.setCountryId(airport.getCountry().getId());
        dto.setCityId(airport.getCity().getId());
        dto.setCurrent_capacity(currentCapacity);
        dto.setTimeZone(airport.getTime_zone());
        return dto;
    }

    // Getters and setters...

    public int getCurrent_capacity() {
        return current_capacity;
    }

    public void setCurrent_capacity(int current_capacity) {
        this.current_capacity = current_capacity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getMax_capacity() {
        return max_capacity;
    }

    public void setMax_capacity(int max_capacity) {
        this.max_capacity = max_capacity;
    }

    public int getCountryId() {
        return countryId;
    }

    public void setCountryId(int countryId) {
        this.countryId = countryId;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getTimeZone() {
        return time_zone;
    }

    public void setTimeZone(String time_zone) {
        this.time_zone = time_zone;
    }
}
