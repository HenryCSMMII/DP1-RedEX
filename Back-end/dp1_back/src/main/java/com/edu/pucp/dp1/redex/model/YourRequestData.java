package com.edu.pucp.dp1.redex.model;

import java.util.Date;

public class YourRequestData {
    private int nHoras;
    private Date fecha_inicio;

    // Getters y Setters
    public int getnHoras() {
        return nHoras;
    }

    public void setnHoras(int nHoras) {
        this.nHoras = nHoras;
    }

    public Date getFecha_inicio() {
        return fecha_inicio;
    }

    public void setFecha_inicio(Date fecha_inicio) {
        this.fecha_inicio = fecha_inicio;
    }
}
