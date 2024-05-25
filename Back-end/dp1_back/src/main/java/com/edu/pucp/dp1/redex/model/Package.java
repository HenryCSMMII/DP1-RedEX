package com.edu.pucp.dp1.redex.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Package {

    String origin;
    String destination;
    String departureTime;
    private LocalDateTime shipmentDateTime;

    public String getPackageId() {
        return packageId;
    }

    public void setPackageId(String packageId) {
        this.packageId = packageId;
    }
    public LocalDateTime getShipmentDateTime() {
        return shipmentDateTime;
    }

    public void setShipmentDateTime(LocalDateTime dateTime) {
        this.shipmentDateTime = dateTime;
    }

    String packageId;
    int quantity; // Asumiendo que este es el campo para la cantidad de paquetes
    private String assignedFlightId;
    private List<Flight> escalas;

    public long getTiempoTotal() {
        return tiempoTotal;
    }

    public void setTiempoTotal(long tiempoTotal) {
        this.tiempoTotal = tiempoTotal;
    }

    public List<Flight> getEscalas() {
        return escalas;
    }

    public void setEscalas(List<Flight> escalas) {
        this.escalas = escalas;
    }

    private long tiempoTotal;


    public Package( String origin, String destination, String departureTime, int quantity , String packageId) {

        this.origin = origin;
        this.destination = destination;
        this.departureTime = departureTime;
        this.quantity = quantity;
        this.packageId= packageId;
        this.escalas = new ArrayList<>(); // Inicializar la lista aquí
    }

    // Getters y setters para todos los atributos


    public void setAssignedFlightId(String assignedFlightId) {
        this.assignedFlightId = assignedFlightId;
    }

    public String getAssignedFlightId() {
        return assignedFlightId;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
