package com.edu.pucp.dp1.redex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "node")
@SQLDelete(sql = "UPDATE node SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Node extends BaseEntity {

    @Column(name = "latitud", nullable = false)
    private Double latitud;

    @Column(name = "longitud", nullable = false)
    private Double longitud;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idAeropuerto", nullable = false)
    private Airport airport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idVuelo", nullable = false)
    private Flight flight;

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Node{" +
                "latitud=" + latitud +
                ", longitud=" + longitud +
                ", airport=" + (airport != null ? airport.getCodigoIATA() : "null") +
                ", flight=" + (flight != null ? flight.getFlightNumber() : "null") +
                '}';
    }
}
