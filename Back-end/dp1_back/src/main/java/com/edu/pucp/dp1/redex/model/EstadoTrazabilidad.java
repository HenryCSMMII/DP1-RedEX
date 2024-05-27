package com.edu.pucp.dp1.redex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "estado_trazabilidad")
@SQLDelete(sql = "UPDATE estado_trazabilidad SET activo = 0 WHERE id = ?")
@Where(clause = "activo = 1")
@NoArgsConstructor
@AllArgsConstructor
@Getter @Setter
public class EstadoTrazabilidad extends BaseEntity {

    @Column(name = "estado", nullable = false)
    private String estado;

    // ToString method for debugging purposes
    @Override
    public String toString() {
        return "EstadoTrazabilidad{" +
                "id=" + getId() +
                ", estado='" + estado + '\'' +
                '}';
    }
}
