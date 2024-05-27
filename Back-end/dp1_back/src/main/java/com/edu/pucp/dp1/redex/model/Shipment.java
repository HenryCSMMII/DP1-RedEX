package com.edu.pucp.dp1.redex.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Entity
@Table(name = "shipment")
@SQLDelete(sql = "UPDATE shipment SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class Shipment extends BaseEntity {

    @Column(name = "cantidad", nullable = false)
    private int cantidad;

    @Column(name = "id_origen", nullable = false)
    private int idOrigen;

    @Column(name = "id_destino", nullable = false)
    private int idDestino;

    @Column(name = "tipo", nullable = false)
    private int tipo;

    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @Column(name = "tiempo_activo", nullable = false)
    private double tiempoActivo;

    @OneToMany(mappedBy = "shipment", fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Paquete> paquetes;

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "Shipment{" +
                "id=" + getId() +
                ", cantidad=" + cantidad +
                ", idOrigen=" + idOrigen +
                ", idDestino=" + idDestino +
                ", tipo=" + tipo +
                ", fechaInicio=" + fechaInicio +
                ", fechaFin=" + fechaFin +
                ", tiempoActivo=" + tiempoActivo +
                '}';
    }
}
