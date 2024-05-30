package com.edu.pucp.dp1.redex.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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
public class Airport extends BaseEntity {

    @Column(name = "codigoIATA", nullable = false)
    private String codigoIATA;

    @Column(name = "latitude", nullable = false)
    private Double latitude;

    @Column(name = "longitude", nullable = false)
    private Double longitude;

    @Column(name = "capacity", nullable = false)
    private int capacity;

    @Column(name = "currentCapacity", nullable = false)
    private int currentCapacity;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "city_id", nullable = false)
    @JsonManagedReference
    private City city;

    public Airport(String codigoIATA, City city, Double latitude, Double longitude, int capacity, int currentCapacity) {
        this.codigoIATA = codigoIATA;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
        this.capacity = capacity;
        this.currentCapacity = currentCapacity;
    }
    
    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Airport{" +
                "codigoIATA='" + codigoIATA + '\'' +
                ", city=" + (city != null ? city.getNombre() : "null") +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", timezoneOffset=" + (city != null ? city.getZonahoraria() : "null") +
                ", capacity=" + capacity +
                ", currentCapacity=" + currentCapacity +
                '}';
    }
}
