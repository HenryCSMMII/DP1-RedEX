package com.edu.pucp.dp1.redex.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "airport")
@SQLDelete(sql = "UPDATE airport SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter

public class Airport extends BaseEntity{


    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "country", nullable = false)
    private String country;

    @Column(name = "shortName", nullable = false)
    private String shortName;

    @Column(name = "continent", nullable = false)
    private String continent;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "timezoneOffset", nullable = false)
    private int timezoneOffset;

    @Column(name = "capacity", nullable = false)
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
