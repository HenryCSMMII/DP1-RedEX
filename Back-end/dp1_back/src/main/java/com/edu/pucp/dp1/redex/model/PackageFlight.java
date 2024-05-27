package com.edu.pucp.dp1.redex.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
@Table(name = "package_flight")
@SQLDelete(sql = "UPDATE package_flight SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class PackageFlight extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "package_id", nullable = false)
    private Paquete paquete;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", nullable = false)
    private Flight flight;

    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "estado_trazabilidad_id", nullable = false)
    private EstadoTrazabilidad estadoTrazabilidad;

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "PackageFlight{" +
                "id=" + getId() +
                ", paquete=" + (paquete != null ? paquete.getId() : "null") +
                ", flight=" + (flight != null ? flight.getId() : "null") +
                ", estadoTrazabilidad=" + (estadoTrazabilidad != null ? estadoTrazabilidad.getEstado() : "null") +
                '}';
    }
}
