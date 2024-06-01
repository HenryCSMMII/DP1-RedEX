package com.edu.pucp.dp1.redex.model;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "tiempo_simulacion")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TiempoSimulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "diaActual", nullable = false)
    private LocalDate diaActual;

    @Column(name = "tiempoActual", nullable = false)
    private LocalTime tiempoActual;
}
